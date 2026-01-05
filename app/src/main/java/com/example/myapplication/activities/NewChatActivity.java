package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.models.Chat;
import com.example.myapplication.models.User;
import com.example.myapplication.utils.PhoneUtils;
import com.example.myapplication.utils.SessionManager;

public class NewChatActivity extends AppCompatActivity {

    private EditText etMobile;
    private Button btnStartChat;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Chat");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        etMobile = findViewById(R.id.etMobile);
        btnStartChat = findViewById(R.id.btnStartChat);

        btnStartChat.setOnClickListener(v -> startChat());
    }

    private void startChat() {
        String mobile = PhoneUtils.normalize(etMobile.getText().toString().trim());

        if (mobile.isEmpty()) {
            etMobile.setError("Enter mobile number");
            return;
        }

        if (!PhoneUtils.isValid(mobile)) {
            etMobile.setError("Invalid mobile number");
            return;
        }

        // Check if trying to chat with self
        if (mobile.equals(session.getMobile())) {
            Toast.makeText(this, "You cannot chat with yourself", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user exists
        User otherUser = dbHelper.getUserByMobile(mobile);

        if (otherUser == null) {
            Toast.makeText(this, "User not registered on LocalChat", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentUserId = session.getUserId();
        int otherUserId = otherUser.getId();

        // Check if chat already exists
        Chat existingChat = dbHelper.getChatBetweenUsers(currentUserId, otherUserId);

        int chatId;
        if (existingChat != null) {
            chatId = existingChat.getId();
        } else {
            // Create new chat
            Chat newChat = new Chat(currentUserId, otherUserId);
            chatId = (int) dbHelper.createChat(newChat);
        }

        // Open chat activity
        Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
        intent.putExtra("chat_id", chatId);
        intent.putExtra("other_user_id", otherUserId);
        intent.putExtra("other_user_mobile", mobile);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}