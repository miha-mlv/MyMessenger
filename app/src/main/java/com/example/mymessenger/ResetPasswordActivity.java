package com.example.mymessenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String EXTRA_KEY_EMAIL="email";
    private EditText etEmail;
    private Button btnResetPassword;
    private ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
        observeViewModel();

        String email = getIntent().getStringExtra(EXTRA_KEY_EMAIL);
        etEmail.setText(email);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();

                // reset password
                viewModel.resetPassword(email);
            }
        });

    }

    private void observeViewModel(){
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            "Ссылка для смены пароля направлена по этому email",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                }
            }
        });
    }

    private void init(){
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
    }

    public static Intent newIntent(Context context, String email){
        return new Intent(context, ResetPasswordActivity.class)
                .putExtra(EXTRA_KEY_EMAIL, email);
    }
}