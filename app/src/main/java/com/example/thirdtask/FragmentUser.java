package com.example.thirdtask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thirdtask.data.Message;
import com.example.thirdtask.data.MessageHistory;
import com.example.thirdtask.data.MessagesHistoryStorageSharePreferenceImpl;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentUser extends Fragment {

    public static String NOTIFICATION = "NOTIFICATION";
    static String NOTIFICATION_DATA = "data";

    private Callback mCallback;
    private UserMessagesArrayAdapter userMessagesArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_fragment_user, container, false);
        ListView mUserMessagesHistory = view.findViewById(R.id.user_message_history);
        userMessagesArrayAdapter = new UserMessagesArrayAdapter(getContext(), 0, new ArrayList<Message>());
        mUserMessagesHistory.setAdapter(userMessagesArrayAdapter);
        mUserMessagesHistory.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        view.findViewById(R.id.user_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onMessageSent(((TextView) view.findViewById(R.id.user_message)).getText().toString());
                }
            }
        });
        initBroadcast();
        return view;
    }

    private void initBroadcast() {
        FragmentUserMessageCatcherBroadcast fragmentUserMessageCatcherBroadcast = new FragmentUserMessageCatcherBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION);
        getActivity().registerReceiver(fragmentUserMessageCatcherBroadcast, filter);
    }

    public UserMessagesArrayAdapter getUserMessagesArrayAdapter() {
        return userMessagesArrayAdapter;
    }

    public interface Callback {

        void onMessageSent(String message);
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public static class FragmentUserMessageCatcherBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(NOTIFICATION_DATA)) {
                String messageText = intent.getStringExtra(NOTIFICATION_DATA);
                Toast.makeText(context, "message recieved: " + messageText, Toast.LENGTH_SHORT).show();
                Log.e("hello mzia", "hello mzia");
            }
        }
    }

    class UserMessagesArrayAdapter extends ArrayAdapter<Message> {

        private Context context;
        private String user;

        public UserMessagesArrayAdapter(Context context, int resource, List<Message> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_message_item, parent, false);
            final Message message = getItem(position);
            TextView textView = view.findViewById(R.id.message);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            if (user.equals(message.getFromUser())) {
                params.gravity = Gravity.RIGHT;
            } else {
                params.gravity = Gravity.LEFT;
            }
            textView.setLayoutParams(params);
            textView.setText(message.getMessage() == null ? "" : message.getMessage());
            return view;
        }

        public void adapterDataUpdate(MessageHistory messageHistory) {
            if (messageHistory == null) {
                messageHistory = new MessagesHistoryStorageSharePreferenceImpl().get(getContext());
            }
            userMessagesArrayAdapter.clear();
            userMessagesArrayAdapter.addAll(messageHistory.getMessages());
            userMessagesArrayAdapter.notifyDataSetChanged();
        }
    }


}
