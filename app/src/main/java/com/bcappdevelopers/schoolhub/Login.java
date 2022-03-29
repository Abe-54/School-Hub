package com.bcappdevelopers.schoolhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.ParseException;

public class Login extends AppCompatActivity {

    public static final String TAG = "Login";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private boolean isStudent;
    private boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(ParseUser.getCurrentUser() != null){
            goToHome();
        }


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClickLoginButton");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username,password);
            }
        });
    }

    private void loginUser(String username,String password){
        Log.i(TAG,"Attempting to login user " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue With Login", e);
                    Toast.makeText(Login.this, "Issue With Login ", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToHome();
                Toast.makeText(Login.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
    }

