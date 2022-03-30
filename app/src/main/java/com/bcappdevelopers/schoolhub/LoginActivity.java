package com.bcappdevelopers.schoolhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bcappdevelopers.schoolhub.admin.AdminHomeActivity;
import com.bcappdevelopers.schoolhub.student.StudentHomeActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    //TAG FOR DEBUGGING
    public static final String TAG = "Login";

    //SCREEN COMPONENTS VARIABLES
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //automatically login the user if it exists
        if(ParseUser.getCurrentUser() != null){
            goToHome();
        }

        //assigns components to actual components
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignUp_loginActivity);

        //click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClickLoginButton");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username,password);
            }
        });

        //click listener for signup button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onClickSignupButton");
                gotToSignup();
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
                    Toast.makeText(LoginActivity.this, "Issue With Login ", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToHome();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {
        Intent i;

        //Decides which side of the app the user goes to
        if(ParseUser.getCurrentUser().getBoolean("isAdmin")) {
            i = new Intent(this, AdminHomeActivity.class);
        } else {
            i = new Intent(this, StudentHomeActivity.class);
        }
        startActivity(i);
        finish();

    }

    private void gotToSignup() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
    }
}

