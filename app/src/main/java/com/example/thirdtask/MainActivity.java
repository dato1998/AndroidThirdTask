package com.example.thirdtask;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thirdtask.data.Message;
import com.example.thirdtask.data.MessageHistory;
import com.example.thirdtask.data.MessagesHistoryStorageSharePreferenceImpl;

public class MainActivity extends AppCompatActivity {
    private static final String MESSAGE_FROM_MZIA = "MESSAGE_FROM_MZIA";
    private static final String MESSAGE_FROM_ZEZVA = "MESSAGE_FROM_ZEZVA";
    private FragmentUser fragmentZezva;
    private FragmentUser fragmentMzia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentZezva = (FragmentUser) getSupportFragmentManager().findFragmentById(R.id.fzezva);
        fragmentZezva.getUserMessagesArrayAdapter().setUser(MESSAGE_FROM_ZEZVA);
        fragmentMzia = (FragmentUser) getSupportFragmentManager().findFragmentById(R.id.fmzia);
        fragmentMzia.getUserMessagesArrayAdapter().setUser(MESSAGE_FROM_MZIA);
        fragmentMzia.setCallback(new FragmentUser.Callback() {
            @Override
            public void onMessageSent(String message) {
                Intent intent = new Intent();
                intent.setAction(FragmentUser.NOTIFICATION);
                intent.putExtra(MESSAGE_FROM_MZIA, message);
                MessagesHistoryStorageSharePreferenceImpl storage = new MessagesHistoryStorageSharePreferenceImpl();
                Message msg = new Message();
                msg.setMessage(message);
                msg.setFromUser(MESSAGE_FROM_MZIA);
                storage.save(getApplicationContext(), msg);
                sendBroadcast(intent);
                onResume();
            }
        });

        fragmentZezva.setCallback(new FragmentUser.Callback() {
            @Override
            public void onMessageSent(String message) {
                Intent intent = new Intent();
                intent.setAction(FragmentUser.NOTIFICATION);
                intent.putExtra(MESSAGE_FROM_ZEZVA, message);
                MessagesHistoryStorageSharePreferenceImpl storage = new MessagesHistoryStorageSharePreferenceImpl();
                Message msg = new Message();
                msg.setMessage(message);
                msg.setFromUser(MESSAGE_FROM_ZEZVA);
                storage.save(getApplicationContext(), msg);
                sendBroadcast(intent);
                onResume();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessagesHistoryStorageSharePreferenceImpl storage = new MessagesHistoryStorageSharePreferenceImpl();
        MessageHistory messageHistory = storage.get(this);
        fragmentMzia.getUserMessagesArrayAdapter().adapterDataUpdate(messageHistory);
        fragmentZezva.getUserMessagesArrayAdapter().adapterDataUpdate(messageHistory);
    }
}