package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ChatListAdapter;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.models.Chat;
import com.example.myapplication.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChatListAdapter.OnChatClickListener {

    private RecyclerView rvChats;
    private TextView tvEmpty;
    private FloatingActionButton fabNewChat;
    private ChatListAdapter adapter;
    private List<Chat> chatList;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        rvChats = findViewById(R.id.rvChats);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabNewChat = findViewById(R.id.fabNewChat);

        chatList = new ArrayList<>();
        adapter = new ChatListAdapter(chatList, this);
        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(adapter);

        fabNewChat.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NewChatActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChats();
    }

    private void loadChats() {
        int userId = session.getUserId();
        chatList = dbHelper.getAllChatsForUser(userId);
        adapter.updateList(chatList);

        if (chatList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvChats.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvChats.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChatClick(Chat chat) {
        int otherUserId = (chat.getUser1Id() == session.getUserId())
                ? chat.getUser2Id() : chat.getUser1Id();

        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("chat_id", chat.getId());
        intent.putExtra("other_user_id", otherUserId);
        intent.putExtra("other_user_mobile", chat.getOtherUserMobile());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            session.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}