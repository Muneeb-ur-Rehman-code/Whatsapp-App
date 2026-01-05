package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.MessageAdapter;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.models.Message;
import com.example.myapplication.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    private int chatId;
    private int otherUserId;
    private String otherUserMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        chatId = getIntent().getIntExtra("chat_id", -1);
        otherUserId = getIntent().getIntExtra("other_user_id", -1);
        otherUserMobile = getIntent().getStringExtra("other_user_mobile");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(otherUserMobile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList, session.getUserId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);

        loadMessages();

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        messageList = dbHelper.getMessagesForChat(chatId);
        adapter = new MessageAdapter(messageList, session.getUserId());
        rvMessages.setAdapter(adapter);

        if (!messageList.isEmpty()) {
            rvMessages.scrollToPosition(messageList.size() - 1);
        }
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();

        if (text.isEmpty()) {
            return;
        }

        Message message = new Message(chatId, session.getUserId(), otherUserId, text);
        long id = dbHelper.sendMessage(message);

        if (id > 0) {
            message.setId((int) id);
            adapter.addMessage(message);
            rvMessages.scrollToPosition(messageList.size() - 1);
            etMessage.setText("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}