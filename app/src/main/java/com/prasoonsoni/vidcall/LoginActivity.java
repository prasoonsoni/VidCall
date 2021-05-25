package com.prasoonsoni.vidcall;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout emailLayout, passwordLayout;
    EditText emailText, passwordText;
    TextView forgetButton, registerButton;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        forgetButton = findViewById(R.id.forgetButton);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Please Wait");
        pd.setMessage("Logging in");
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        registerButton.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
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
                        passwordLayout.setError("Password cannot be empty");
                    }
                }
            });
            if(email.isEmpty() && password.isEmpty()){
                emailLayout.setError("E-Mail cannot be empty.");
                passwordLayout.setError("Password cannot be empty");
            } else if(email.isEmpty()){
                emailLayout.setError("E-Mail cannot be empty.");
            } else if(password.isEmpty()){
                passwordLayout.setError("Password cannot be empty");
            } else {
                pd.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Successfully Logged in.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Please verify your E-Mail ID", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            mAuth.signOut();
                        }
                    } else {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                });
            }
        });

    }
}