package com.example.arcadefinder.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arcadefinder.Models.RegisterModel;
import com.example.arcadefinder.R;
import com.example.arcadefinder.ViewModels.RegisterViewModel;
import com.parse.ParseUser;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "Register Activity";

    EditText etNewUsername;
    EditText etNewPassword;
    Button btnRegister;
    RegisterViewModel registerViewModel;
    boolean firstObservation = true;
    boolean locationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnRegister = findViewById(R.id.btnRegister);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.getRegisterModel().observe(this, new Observer<RegisterModel>() {
            @Override
            public void onChanged(RegisterModel registerModel) {
                ParseUser user = registerModel.getUser();
                if (user != null) {
                    Toast.makeText(RegisterActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                } else if (firstObservation) {
                    // When activity is created user will be null, don't want to say failed to register
                    firstObservation = false;
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etNewUsername.getText().toString();
                String password = etNewPassword.getText().toString();
                registerViewModel.registerUser(username, password);
            }
        });
    }

    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finishAffinity();
        finish();
    }
}