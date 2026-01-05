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
import com.example.myapplication.utils.PhoneUtils;
import com.example.myapplication.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etMobile, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String mobile = PhoneUtils.normalize(etMobile.getText().toString().trim());
        String password = etPassword.getText().toString().trim();

        if (mobile.isEmpty()) {
            etMobile.setError("Enter mobile number");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Enter password");
            return;
        }

        User user = dbHelper.loginUser(mobile, password);

        if (user != null) {
            session.createSession(user.getId(), user.getMobileNumber());
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
        }
    }
}