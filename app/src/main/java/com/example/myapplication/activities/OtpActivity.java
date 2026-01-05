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
import com.example.myapplication.models.User;
import com.example.myapplication.utils.SessionManager;

public class OtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerify;
    private TextView tvResend;
    private String mobile, password, otp;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        mobile = getIntent().getStringExtra("mobile");
        password = getIntent().getStringExtra("password");
        otp = getIntent().getStringExtra("otp");

        etOtp = findViewById(R.id.etOtp);
        btnVerify = findViewById(R.id.btnVerify);
        tvResend = findViewById(R.id.tvResend);

        btnVerify.setOnClickListener(v -> verifyOtp());

        tvResend.setOnClickListener(v -> {
            Toast.makeText(this, "Your OTP is: " + otp, Toast.LENGTH_LONG).show();
        });
    }

    private void verifyOtp() {
        String enteredOtp = etOtp.getText().toString().trim();

        if (enteredOtp.isEmpty()) {
            etOtp.setError("Enter OTP");
            return;
        }

        if (enteredOtp.equals(otp)) {
            // Create user account
            User user = new User(mobile, password);
            long userId = dbHelper.registerUser(user);

            if (userId > 0) {
                session.createSession((int) userId, mobile);
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }
}