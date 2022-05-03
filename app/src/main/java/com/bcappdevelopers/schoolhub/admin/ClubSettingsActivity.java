package com.bcappdevelopers.schoolhub.admin;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.bcappdevelopers.schoolhub.R;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.parceler.Parcels;

public class ClubSettingsActivity extends AppCompatActivity {

    private static final String TAG = "CLUB SETTINGS ACTIVITY";

    private ImageView profileImage;
    private Button btnChangeProfileImage;
    private EditText etChangeDescription;
    private Button btnSubmitChanges;
    private Button btnCancelChanges;

    ParseObject club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_settings);

        club = getIntent().getParcelableExtra("Club");

        profileImage = findViewById(R.id.ivClubImageEdit);
        btnChangeProfileImage = findViewById(R.id.btnEditImage);
        etChangeDescription = findViewById(R.id.etEditDescirption);
        btnSubmitChanges = findViewById(R.id.btnSubmitChanges);
        btnCancelChanges = findViewById(R.id.btnCancelChanges);

        ParseFile clubImage = club.getParseFile("clubImage");

        if(clubImage != null) {
            Glide.with(ClubSettingsActivity.this)
                    .load(clubImage.getUrl())
                    .centerCrop()
                    .transform(new CropCircleWithBorderTransformation())
                    .into(profileImage);
        }

        etChangeDescription.setText(club.getString("clubDescription"));

        btnChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etChangeDescription.getText().toString().isEmpty()) {
                    SubmitChanges(etChangeDescription.getText().toString());
                }
            }
        });

        btnCancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void SubmitChanges(String newDescription) {
        club.put("clubDescription", newDescription);
        club.saveInBackground();
        finish();
    }
}