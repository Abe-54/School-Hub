package com.bcappdevelopers.schoolhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    //TAG FOR DEBUGGING
    public static final String TAG = "Signup";

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignUp;
    private CheckBox cbConfirmation;
    private TextView tvAlreadyMember;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        etUsername = findViewById(R.id.etUsernameField);
        etEmail = findViewById(R.id.etEmailField);
        etPassword = findViewById(R.id.etPasswordField);
        etConfirmPassword = findViewById(R.id.etPasswordFieldVerify);
        btnSignUp = findViewById(R.id.btnSignup_signupActivity);
        cbConfirmation = findViewById(R.id.cbTermsAgreement);
        tvAlreadyMember = findViewById(R.id.tvAlreadyMemeberLink);

//        click listener for button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClickLoginButton");

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                Boolean acceptedConditions = cbConfirmation.isActivated();


                signupStudent(username, email, password, acceptedConditions);
            }
        });

        tvAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotToLogin();
            }
        });
    }

    private void signupStudent(String username, String email, String password, Boolean confirmation){
        //TODO: finish method
        //TODO: post to back4app and login student

        Toast.makeText(this, "SIGNED UP", Toast.LENGTH_SHORT).show();
    }

    private void gotToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
