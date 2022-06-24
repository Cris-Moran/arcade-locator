package com.example.arcadefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arcadefinder.ViewModels.LoginViewModel;
import com.example.arcadefinder.ViewModels.RegisterViewModel;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "Register Activity";

    EditText etNewUsername;
    EditText etNewPassword;
    Button btnRegister;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnRegister = findViewById(R.id.btnRegister);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etNewUsername.getText().toString();
                String password = etNewPassword.getText().toString();
                boolean registered = registerViewModel.registerUser(username, password);
                if (registered) {
                    Toast.makeText(RegisterActivity.this, "Registered! Welcome!", Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}