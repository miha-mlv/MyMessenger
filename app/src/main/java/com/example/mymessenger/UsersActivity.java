package com.example.mymessenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID="currentUserId";
    private UsersViewModel viewModel;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter adapter;
    private String currentUsersId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        init();
        observeViewModel();

        adapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                startActivity(ChatActivity.newIntent(
                        UsersActivity.this,
                        currentUsersId,
                        user.getId()

                ));
            }
        });
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    startActivity(LoginActivity.newIntent(UsersActivity.this));
                    finish();
                }
            }
        });

        viewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        adapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(adapter);
        currentUsersId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
    }

    public static Intent newIntent(Context context, String currentUsersId) {
        return new Intent(context, UsersActivity.class)
                .putExtra(EXTRA_CURRENT_USER_ID, currentUsersId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
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
}