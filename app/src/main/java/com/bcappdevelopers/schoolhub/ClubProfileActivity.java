package com.bcappdevelopers.schoolhub;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.StudentHomeActivity;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bcappdevelopers.schoolhub.student.fragments.StudentProfileFragment;
import com.bumptech.glide.Glide;
import com.parse.*;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

public class ClubProfileActivity extends AppCompatActivity {

    private static final String TAG = "CLUB PROFILE ACTIVITY";

    TextView tvClubName;
    TextView tvClubScreenDescription;
    TextView tvIsMember;
    ImageView ivClubProfile;
    RecyclerView rvClubScreenAnnouncement;
    Button btnSubScribe;
    Button btnEventArchive;

    String clubName;
    AnnouncementAdapter adapter;
    Announcement announcement;
    Club club;
    ParseObject profile;

    String currentUserObjectID = "";
    Boolean alreadySubscribed = false;

    ParseObject currentClub = null;

    private List<ParseObject> allAnnouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        announcement = Parcels.unwrap(getIntent().getParcelableExtra("Announcement"));
        club = Parcels.unwrap(getIntent().getParcelableExtra("Club"));
        profile = Parcels.unwrap(getIntent().getParcelableExtra("ClubObject"));
       // member = Parcels.unwrap(getIntent().getParcelableExtra());

        String clubDescription = "";
        ParseFile clubImage = null;
        currentUserObjectID = ParseUser.getCurrentUser().getObjectId();

        if(announcement != null) {
            clubName = announcement.getEventClub().getString("clubName");
            clubDescription = announcement.getEventClub().getString("clubDescription");
            clubImage = announcement.getEventClub().getParseFile("clubImage");
            currentClub = announcement.getEventClub();
        } else if(club != null) {
           clubName = club.getClubName();
            clubDescription = club.getClubDescription();
            clubImage = club.getClubImage();
            currentClub = club;
        } else if(profile != null) {
            clubName = profile.getString("clubName");
            clubDescription = profile.getString("clubDescription");
            clubImage = profile.getParseFile("clubImage");
            currentClub = profile;
        }

        rvClubScreenAnnouncement = findViewById(R.id.rvClubScreenAnnouncements);
        tvClubName = findViewById(R.id.tvClubScreenName);
        tvClubScreenDescription = findViewById(R.id.tvClubScreenDescription);
        tvIsMember = findViewById(R.id.tvIsMember);
        ivClubProfile = findViewById(R.id.ivClubProfile);
        btnSubScribe = findViewById(R.id.btnSubscribe);
        btnEventArchive = findViewById(R.id.btnEventArchive);

        allAnnouncements = new ArrayList<>();
        adapter = new AnnouncementAdapter(this, allAnnouncements);
        rvClubScreenAnnouncement.setAdapter(adapter);
        rvClubScreenAnnouncement.setLayoutManager(new LinearLayoutManager(this));

        tvClubName.setText(clubName);
        tvClubScreenDescription.setText(clubDescription);

        if(clubImage != null) {
            Glide.with(this)
                    .load(clubImage.getUrl())
                    .centerCrop()
                    .transform(new CropCircleWithBorderTransformation())
                    .into(ivClubProfile);
        }

        queryData();
        queryUsers();

        if(btnSubScribe.getText().toString().compareTo( "Unsubscribe") == 0){
           alreadySubscribed = !alreadySubscribed;
            if(alreadySubscribed = true) {
                btnSubScribe.setText("Unsubscribe");
                Log.i(TAG, "Button text should  say unsubscribe AlreadySubscribed:" + alreadySubscribed);
            } else {
                btnSubScribe.setText("Subscribe");
                Log.i(TAG, "Button text should  say subscribe AlreadySubscribed:" + alreadySubscribed);
            }
        }


        btnSubScribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(alreadySubscribed == true){
                unsubscribe();

                } else {
                subscribe();
                }
            }
        });
    }

    private void queryUsers() {

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");

        userQuery.getInBackground(currentUserObjectID, new GetCallback<ParseUser>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting profile data", e);
                    return;
                }

                ParseQuery<ParseObject> clubList = user.getRelation("inClub").getQuery();

                clubList.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting profile data", e);
                            return;
                        }
                        for (ParseObject clubs : objects) {
                            if(clubs.getString("clubName").equals(clubName) ) {
                                Log.i(TAG, "Found Club " + clubs.getString("clubName") + " for user: " + user.getString("username"));
                                btnSubScribe.setText("Unsubscribe");
                                Log.i(TAG, "Is alreadySubscribed set to true? Answer:" + alreadySubscribed);
                            }
                        }
                        if(btnSubScribe.getText().toString().equals( "Unsubscribe")){
                            alreadySubscribed = !alreadySubscribed;
                            if(alreadySubscribed == true) {
                                btnSubScribe.setText("Unsubscribe");
                                Log.i(TAG, "Button text should  say unsubscribe AlreadySubscribed:" + alreadySubscribed);
                            } else {
                                btnSubScribe.setText("Subscribe");
                                Log.i(TAG, "Button text should  say subscribe AlreadySubscribed:" + alreadySubscribed);
                            }
                        }
                    }
                });
            }
        });
    }

    private void queryData() {
        ParseQuery<ParseObject> clubQuery = ParseQuery.getQuery("announcements");
        clubQuery.include("madeBy");
        clubQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> announcements, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting club", e);
                    return;
                }
                for (ParseObject singleAnnouncement : announcements) {
                    if(singleAnnouncement.getParseObject("madeBy").getString("clubName").compareTo(clubName) == 0) {
                        Log.i(TAG, "announcements: " + singleAnnouncement.getString("eventName"));
                        allAnnouncements.add(singleAnnouncement);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void subscribe(){
        ParseRelation<ParseObject> inClub = ParseUser.getCurrentUser().getRelation("inClub");
        inClub.add(currentClub);

        ParseRelation<ParseObject> usersInClub = currentClub.getRelation("usersInClub");
        usersInClub.add(ParseUser.getCurrentUser());

        Toast.makeText(getApplicationContext(), "User Subscribed",Toast.LENGTH_SHORT).show();
        ParseUser.getCurrentUser().saveInBackground();

        alreadySubscribed = true;
        btnSubScribe.setText("Unsubscribe");

    }

    private void unsubscribe(){

        ParseRelation<ParseObject> inClub = ParseUser.getCurrentUser().getRelation("inClub");
        inClub.remove(currentClub);

        ParseRelation<ParseObject> usersInClub = currentClub.getRelation("usersInClub");
        usersInClub.remove(ParseUser.getCurrentUser());

        Toast.makeText(getApplicationContext(), "User Unsubscribed",Toast.LENGTH_SHORT).show();
        ParseUser.getCurrentUser().saveInBackground();

        alreadySubscribed = false;
        btnSubScribe.setText("Subscribe");


    }
}