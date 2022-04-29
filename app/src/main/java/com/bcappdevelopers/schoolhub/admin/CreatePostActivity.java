package com.bcappdevelopers.schoolhub.admin;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.parse.*;
import org.parceler.Parcels;

import java.io.File;

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "Create Post Activity";

    private EditText etDescription;
    private EditText etTitle;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        etDescription = findViewById(R.id.etPostDescription);
        btnPost = findViewById(R.id.btnPost);
        etTitle = findViewById(R.id.etPostTitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!etDescription.getText().toString().isEmpty() && !etTitle.toString().isEmpty()) {
                    saveAnnouncement(etDescription.getText().toString(), etTitle.getText().toString(), ParseUser.getCurrentUser(), null);
                    Intent i = new Intent(CreatePostActivity.this, AdminHomeActivity.class);
                    CreatePostActivity.this.startActivity(i);
                }
            }
        });
    }

    private void saveAnnouncement(String description, String title, ParseUser currentUser, File photoFile) {
        Announcement announcement = new Announcement();
        announcement.setDescription(description);
        announcement.setEvenName(title);
//        announcement.setImage(new ParseFile(photoFile));
        announcement.setClub(currentUser.getParseObject("ownsClub"));

        announcement.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "error while saving", e);
                    Toast.makeText(CreatePostActivity.this, "error while saving", Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "Announcement save was successful");
                Log.i(TAG, "Announcement contents: Title: " + title + " Description: " + description);
                etDescription.setText("");
            }
        });
    }
}