package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.models.Chat;
import com.example.myapplication.models.Message;
import com.example.myapplication.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocalChatDB";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_MOBILE = "mobile_number";
    private static final String COL_PASSWORD = "password";
    private static final String COL_CREATED_AT = "created_at";

    // Chats Table
    private static final String TABLE_CHATS = "chats";
    private static final String COL_CHAT_ID = "id";
    private static final String COL_USER1_ID = "user1_id";
    private static final String COL_USER2_ID = "user2_id";
    private static final String COL_LAST_MESSAGE = "last_message";
    private static final String COL_LAST_MESSAGE_TIME = "last_message_time";
    private static final String COL_CHAT_CREATED_AT = "created_at";

    // Messages Table
    private static final String TABLE_MESSAGES = "messages";
    private static final String COL_MESSAGE_ID = "id";
    private static final String COL_MSG_CHAT_ID = "chat_id";
    private static final String COL_SENDER_ID = "sender_id";
    private static final String COL_RECEIVER_ID = "receiver_id";
    private static final String COL_MESSAGE_TEXT = "message_text";
    private static final String COL_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MOBILE + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_CREATED_AT + " INTEGER)";
        db.execSQL(createUsersTable);

        // Create Chats Table
        String createChatsTable = "CREATE TABLE " + TABLE_CHATS + " (" +
                COL_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER1_ID + " INTEGER, " +
                COL_USER2_ID + " INTEGER, " +
                COL_LAST_MESSAGE + " TEXT, " +
                COL_LAST_MESSAGE_TIME + " INTEGER, " +
                COL_CHAT_CREATED_AT + " INTEGER)";
        db.execSQL(createChatsTable);

        // Create Messages Table
        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MSG_CHAT_ID + " INTEGER, " +
                COL_SENDER_ID + " INTEGER, " +
                COL_RECEIVER_ID + " INTEGER, " +
                COL_MESSAGE_TEXT + " TEXT, " +
                COL_TIMESTAMP + " INTEGER)";
        db.execSQL(createMessagesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ==================== USER OPERATIONS ====================

    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MOBILE, user.getMobileNumber());
        values.put(COL_PASSWORD, user.getPassword());
        values.put(COL_CREATED_AT, user.getCreatedAt());
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User loginUser(String mobile, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS,
                null,
                COL_MOBILE + "=? AND " + COL_PASSWORD + "=?",
                new String[]{mobile, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setMobileNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_MOBILE)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean isUserExists(String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_MOBILE + "=?",
                new String[]{mobile},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserByMobile(String mobile) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS,
                null,
                COL_MOBILE + "=?",
                new String[]{mobile},
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setMobileNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_MOBILE)));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS,
                null,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setMobileNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_MOBILE)));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)));
        }
        cursor.close();
        db.close();
        return user;
    }

    // ==================== CHAT OPERATIONS ====================

    public long createChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER1_ID, chat.getUser1Id());
        values.put(COL_USER2_ID, chat.getUser2Id());
        values.put(COL_LAST_MESSAGE, chat.getLastMessage());
        values.put(COL_LAST_MESSAGE_TIME, chat.getLastMessageTime());
        values.put(COL_CHAT_CREATED_AT, chat.getCreatedAt());
        long id = db.insert(TABLE_CHATS, null, values);
        db.close();
        return id;
    }

    public Chat getChatBetweenUsers(int user1Id, int user2Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Chat chat = null;

        String query = "SELECT * FROM " + TABLE_CHATS + " WHERE " +
                "(" + COL_USER1_ID + "=? AND " + COL_USER2_ID + "=?) OR " +
                "(" + COL_USER1_ID + "=? AND " + COL_USER2_ID + "=?)";

        Cursor cursor = db.rawQuery(query,
                new String[]{String.valueOf(user1Id), String.valueOf(user2Id),
                        String.valueOf(user2Id), String.valueOf(user1Id)});

        if (cursor.moveToFirst()) {
            chat = new Chat();
            chat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHAT_ID)));
            chat.setUser1Id(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER1_ID)));
            chat.setUser2Id(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER2_ID)));
            chat.setLastMessage(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_MESSAGE)));
            chat.setLastMessageTime(cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_MESSAGE_TIME)));
            chat.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CHAT_CREATED_AT)));
        }
        cursor.close();
        db.close();
        return chat;
    }

    public List<Chat> getAllChatsForUser(int userId) {
        List<Chat> chatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CHATS + " WHERE " +
                COL_USER1_ID + "=? OR " + COL_USER2_ID + "=? " +
                "ORDER BY " + COL_LAST_MESSAGE_TIME + " DESC";

        Cursor cursor = db.rawQuery(query,
                new String[]{String.valueOf(userId), String.valueOf(userId)});

        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            chat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHAT_ID)));
            chat.setUser1Id(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER1_ID)));
            chat.setUser2Id(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER2_ID)));
            chat.setLastMessage(cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_MESSAGE)));
            chat.setLastMessageTime(cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_MESSAGE_TIME)));
            chat.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CHAT_CREATED_AT)));

            // Get other user's mobile
            int otherUserId = (chat.getUser1Id() == userId) ? chat.getUser2Id() : chat.getUser1Id();
            User otherUser = getUserById(otherUserId);
            if (otherUser != null) {
                chat.setOtherUserMobile(otherUser.getMobileNumber());
            }

            chatList.add(chat);
        }
        cursor.close();
        db.close();
        return chatList;
    }

    public void updateChatLastMessage(int chatId, String message, long time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LAST_MESSAGE, message);
        values.put(COL_LAST_MESSAGE_TIME, time);
        db.update(TABLE_CHATS, values, COL_CHAT_ID + "=?",
                new String[]{String.valueOf(chatId)});
        db.close();
    }

    // ==================== MESSAGE OPERATIONS ====================

    public long sendMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MSG_CHAT_ID, message.getChatId());
        values.put(COL_SENDER_ID, message.getSenderId());
        values.put(COL_RECEIVER_ID, message.getReceiverId());
        values.put(COL_MESSAGE_TEXT, message.getMessageText());
        values.put(COL_TIMESTAMP, message.getTimestamp());
        long id = db.insert(TABLE_MESSAGES, null, values);
        db.close();

        // Update chat's last message
        updateChatLastMessage(message.getChatId(), message.getMessageText(), message.getTimestamp());

        return id;
    }

    public List<Message> getMessagesForChat(int chatId) {
        List<Message> messageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES,
                null,
                COL_MSG_CHAT_ID + "=?",
                new String[]{String.valueOf(chatId)},
                null, null, COL_TIMESTAMP + " ASC");

        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MESSAGE_ID)));
            message.setChatId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_MSG_CHAT_ID)));
            message.setSenderId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SENDER_ID)));
            message.setReceiverId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECEIVER_ID)));
            message.setMessageText(cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGE_TEXT)));
            message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
            messageList.add(message);
        }
        cursor.close();
        db.close();
        return messageList;
    }
}