package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.utils.OtpUtils;
import com.example.myapplication.utils.PhoneUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText etMobile, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void registerUser() {
        String mobile = PhoneUtils.normalize(etMobile.getText().toString().trim());
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (mobile.isEmpty()) {
            etMobile.setError("Enter mobile number");
            return;
        }

        if (!PhoneUtils.isValid(mobile)) {
            etMobile.setError("Invalid mobile number");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Enter password");
            return;
        }

        if (password.length() < 4) {
            etPassword.setError("Password must be at least 4 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (dbHelper.isUserExists(mobile)) {
            Toast.makeText(this, "User already exists with this number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate OTP and go to verification
        String otp = OtpUtils.generateOtp();
        Toast.makeText(this, "Your OTP is: " + otp, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("password", password);
        intent.putExtra("otp", otp);
        startActivity(intent);
    }
}