package com.example.arcadefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView tvRegisterHere;
    TextView tvContinueGuest;
    LoginViewModel loginViewModel;

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
        loginViewModel.getUser().observe(this, new Observer<ParseUser>() {
            @Override
            public void onChanged(ParseUser parseUser) {
                if (parseUser != null) {
                    Log.i(TAG, "Already logged in! User is: " + ParseUser.getCurrentUser().getUsername());
                    goMainActivity();
                }
            }
        });

//        if (ParseUser.getCurrentUser() != null) {
//            Log.i(TAG, "Already logged in! User is: " + ParseUser.getCurrentUser().getUsername());
//            goMainActivity();
//        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginViewModel.logIn(username, password);
                goMainActivity();
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to register activity
                Log.i(TAG, "onClick register text");
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        tvContinueGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick continue as guest");
                goMainActivity();
            }
        });
    }

//    private void loginUser(String username, String password) {
//        Log.i(TAG, "Attempting to login user" + username);
//        ParseUser.logInInBackground(username, password, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Issue with login", e);
//                    return;
//                }
//                goMainActivity();
//                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
//            }
//        });
//    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}