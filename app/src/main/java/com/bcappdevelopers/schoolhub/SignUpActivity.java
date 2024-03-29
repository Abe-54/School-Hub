package com.bcappdevelopers.schoolhub;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.StudentHomeActivity;
import com.parse.*;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    //TAG FOR DEBUGGING
    public static final String TAG = "Signup";
    public static final String COLLEGE = "Bloomfield College";

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
                Log.i(TAG,"onClickSignupButton");

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                Boolean acceptedConditions = cbConfirmation.isChecked();

                if(canSignup(username, email, password, confirmPassword, acceptedConditions)) {
                    signupStudent(username, email, password, acceptedConditions);
                }
            }
        });

        tvAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotToLogin();
            }
        });
    }

    private void signupStudent(String username, String email, String password, Boolean acceptedConditions){

        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("readTerms", acceptedConditions);
        user.put("isStudent", true);

        Log.i(TAG,"Attempting to signup user " + username);

        user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "signed up");
                   // user.getRelation("inClub").add();
                    //TODO add user to School Club on Signup

//                    ParseQuery<Club> clubQuery = ParseQuery.getQuery(Club.class);

//                    clubQuery.findInBackground(new FindCallback<Club>() {
//                        @Override
//                        public void done(List<Club> clubs, ParseException e) {
//                            if (e != null) {
//                                Log.e(TAG, "Issues getting club list", e);
//                                return;
//                            }
//
//                            for (Club club : clubs) {
//                                if(club.getClubName().compareTo(COLLEGE) == 0) {
//                                    user.getRelation("inClub").add(club);
//                                    club.getRelation("usersInClub").add(user);
//                                }
//                            }
//                        }
//                    });

//                    clubQuery.getInBackground("XEpcbSclri", new GetCallback<Club>() {
//                        @Override
//                        public void done(Club club, ParseException e) {
//                            user.getRelation("inClub").add(club);
//                            club.getRelation("usersInClub").add(user);
//                        }
//                    });

                    goHome();
                } else {
                    Log.e(TAG, "Issue With Login", e);
                    Toast.makeText(SignUpActivity.this, "Issue With Login ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private boolean canSignup(String username, String email, String password, String confirmPassword, Boolean confirmation) {
        //TODO replace toast with red error text

        if(username.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "username cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if(email.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "email cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }else if(password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "password cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }else if(confirmPassword.isEmpty() ) {
            Toast.makeText(SignUpActivity.this, "Confirmation Password cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }else if(!confirmPassword.equals(password)) {
            Toast.makeText(SignUpActivity.this, "Passwords must match!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!confirmation) {
            Toast.makeText(SignUpActivity.this, "Please accept terms & conditions", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void goHome() {
        Intent i = new Intent(this, StudentHomeActivity.class);

        ParseQuery<Club> clubQuery = ParseQuery.getQuery(Club.class);

        clubQuery.findInBackground(new FindCallback<Club>() {
            @Override
            public void done(List<Club> clubs, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting club list", e);
                    return;
                }
                 for (Club club : clubs) {
                     if(club.getClubName().compareTo(COLLEGE) == 0) {
                         ParseUser.getCurrentUser().getRelation("inClub").add(club);
                         club.getRelation("usersInClub").add(ParseUser.getCurrentUser());
                         Log.i(TAG, "Adding " + club.getClubName() + " to new user" );
                         club.saveInBackground();
                     }
                 }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
        startActivity(i);
        finish();
    }

    private void gotToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
