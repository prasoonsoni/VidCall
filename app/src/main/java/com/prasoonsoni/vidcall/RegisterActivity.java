package com.prasoonsoni.vidcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    TextInputLayout emailLayout, passwordLayout, nameLayout;
    EditText emailText, passwordText, nameText;
    TextView loginButton;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Please Wait");
        pd.setMessage("Creating Account");
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String name = nameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            nameText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nameLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty()){
                        nameLayout.setError("Name cannot be empty.");
                    }
                }
            });
            emailText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emailLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty()){
                        emailLayout.setError("E-Mail cannot be empty.");
                    }
                }
            });
            passwordText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    passwordLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty()){
                        passwordLayout.setError("Password cannot be empty.");
                    }
                }
            });
            if(email.isEmpty() && password.isEmpty() && name.isEmpty()){
                nameLayout.setError("Name cannot be empty.");
                emailLayout.setError("E-Mail cannot be empty.");
                passwordLayout.setError("Password cannot be empty.");
            } else if(email.isEmpty()){
                emailLayout.setError("E-Mail cannot be empty.");
            } else if(password.isEmpty()){
                passwordLayout.setError("Password cannot be empty.");
            } else if(name.isEmpty()){
                nameLayout.setError("Name cannot be empty.");
            } else {
                pd.show();
                HashMap<String, String> data = new HashMap<>();
                data.put("name", name);
                data.put("email", email);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully, Please Verify your E-Mail ID.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Data Adding to database failed.", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Verification link sending failed.", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                }
                            }
                        });

                    } else {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "Register Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });


    }
}