package com.example.mymessenger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID = "currentUserId";
    private static final String EXTRA_OTHER_USER_ID = "otherUserId";
    private ImageView imageView;
    private EditText etTextMessage;
    private RecyclerView recyclerViewMessages;
    private View userStatus;
    private TextView tvTitle;
    private MessageAdapter messageAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        observeViewModel();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(
                        etTextMessage.getText().toString().trim(),
                        currentUserId,
                        otherUserId);
                viewModel.sendMessage(message);
            }
        });


    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(ChatActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getMessageSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                etTextMessage.setText("");
            }
        });

        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getLastName());
                tvTitle.setText(userInfo);
                int bgResId;
                if(user.getOnline()){
                    bgResId = R.drawable.circle_green;
                }else{
                    bgResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this, bgResId);
                userStatus.setBackground(background);
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    private void init() {
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        imageView = findViewById(R.id.imageView);
        etTextMessage = findViewById(R.id.etTextMessage);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        userStatus = findViewById(R.id.userStatus);
        tvTitle = findViewById(R.id.tvTitle);
        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

    }

    static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        return new Intent(context, ChatActivity.class)
                .putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
                .putExtra(EXTRA_OTHER_USER_ID, otherUserId);
    }
}