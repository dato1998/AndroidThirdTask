package com.example.thirdtask.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MessagesHistoryStorageSharePreferenceImpl {
    private static final String MESSAGE_HISTORY_KEY = "message_history_key";
    private static final String MESSAGE_HISTORY = "message_history";

    public void save(Context context, Message message) {
        MessageHistory messageHistory = this.get(context);
        if (messageHistory == null) {
            messageHistory = new MessageHistory();
        }
        messageHistory.addMessage(message);
        String value = new Gson().toJson(messageHistory);
        SharedPreferences sharedPref = getInstance(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MESSAGE_HISTORY_KEY, value);
        editor.apply();
    }

    public MessageHistory get(Context context) {
        SharedPreferences sharedPref = getInstance(context);
        String messageHistoryString = sharedPref.getString(MESSAGE_HISTORY_KEY, null);
        MessageHistory messageHistory = null;
        if (messageHistoryString == null) {
            messageHistory = new MessageHistory();
            messageHistory.setMessages(new ArrayList<Message>());
        } else {
            messageHistory = new Gson().fromJson(sharedPref.getString(MESSAGE_HISTORY_KEY, null), MessageHistory.class);
        }
        return messageHistory;
    }

    private SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(MESSAGE_HISTORY, Context.MODE_PRIVATE);
    }
}
