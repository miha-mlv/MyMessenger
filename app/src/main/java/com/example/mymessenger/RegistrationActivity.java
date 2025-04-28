package com.example.mymessenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etName, etLastName, etYear;
    private Button btnSignUp;
    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        observeViewModel();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getTrimmedValue(etEmail);
                String password = getTrimmedValue(etPassword);
                String name = getTrimmedValue(etName);
                String lastName = getTrimmedValue(etLastName);
                int age = Integer.parseInt(getTrimmedValue(etYear));

                //sign up
                viewModel.signUp(email, password, name, lastName, age);
            }
        });
    }

    private void observeViewModel(){
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null){
                    Toast.makeText(
                            RegistrationActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){

                    startActivity(UsersActivity.newIntent(
                            RegistrationActivity.this,
                            firebaseUser.getUid())
                    );
                    finish();
                }
            }
        });
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etLastName = findViewById(R.id.etLastName);
        etYear = findViewById(R.id.etYear);
        btnSignUp = findViewById(R.id.btnSignUp);

        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}