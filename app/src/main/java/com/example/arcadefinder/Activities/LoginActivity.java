package com.example.arcadefinder.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arcadefinder.Models.LoginModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.LoginViewModel;
import com.parse.ParseUser;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView tvRegisterHere;
    TextView tvContinueGuest;
    LoginViewModel loginViewModel;
    boolean firstObservation = true;
    boolean locationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvContinueGuest = findViewById(R.id.tvContinueGuest);


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginModel().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                ParseUser currentUser = loginModel.getUser();
                if (currentUser != null) {
                    // Check for location permission
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // No location permissions, need to request them
                        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                        requestLocationPermission.launch(permissions);
                    } else {
                        // Already have permission
                        locationPermission = true;
                        goMainActivity();
                    }
                } else if (firstObservation) {
                    // Activity has been created, don't want to say failed to log in
                    firstObservation = false;
                } else {
                    // Failed to log in
                    Toast.makeText(LoginActivity.this, "Failed to log in", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginViewModel.logIn(username, password);
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to register activity
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        tvContinueGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Logged in as guest", Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("locationPermission", locationPermission);
        editor.apply();
        startActivity(i);
        finish();
    }


    // TODO: Request permission before going into MapFragment
    ActivityResultLauncher<String[]> requestLocationPermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            if (result.get(Manifest.permission.ACCESS_FINE_LOCATION)) {
                locationPermission = true;
            } else {
                // Permission was denied
                locationPermission = false;
            }
            Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
            goMainActivity();
        }
    });
}