package com.bcappdevelopers.schoolhub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    String subscribeBtnText = "";
    Boolean alreadySubscribed = false;

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

        if(announcement != null) {
            clubName = announcement.getEventClub().getString("clubName");
            clubDescription = announcement.getEventClub().getString("clubDescription");
            clubImage = announcement.getEventClub().getParseFile("clubImage");
        } else if(club != null) {
           clubName = club.getClubName();
            clubDescription = club.getClubDescription();
            clubImage = club.getClubImage();
        } else if(profile != null) {
            clubName = profile.getString("clubName");
            clubDescription = profile.getString("clubDescription");
            clubImage = profile.getParseFile("clubImage");
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
        }


        btnSubScribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(alreadySubscribed){
                btnSubScribe.setText("Unsubscribe");
                Log.i(TAG, "Button text should say unsubscribe " + alreadySubscribed);
                //      remove a user from relation?          user.remove("inClub").whatQuery(); parseRelation.java for research
                //      need to use pointers in Back4App, literally pointer instead of relations
                alreadySubscribed = false;
            } else {
                btnSubScribe.setText("Subscribe");
                Log.i(TAG, "Button text should say subscribe " + alreadySubscribed);
                alreadySubscribed = true;
            }


            }
        });
    }

    private void queryUsers() {

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");

        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(List<ParseUser> outerQueryResults, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting profile data", e);
                    return;
                }
                for (ParseObject user : outerQueryResults) {
                    if(user.hasSameId(ParseUser.getCurrentUser())) {
                        ParseQuery<ParseObject> clubList = user.getRelation("inClub").getQuery();


                        clubList.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Issues getting profile data", e);
                                    return;
                                }
                                for (ParseObject clubs : objects) {
                                    if(clubs.getString("clubName").compareTo(clubName) == 0) {
                                        Log.i(TAG, "Found Club " + clubs.getString("clubName") + " for user: " + user.getString("username"));
                                        btnSubScribe.setText("Unsubscribe");
                                        Log.i(TAG, "set is subscribed to true");
                                    }
                                }
                            }
                        });
                    }
                }
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
}