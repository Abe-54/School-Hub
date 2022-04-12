package com.bcappdevelopers.schoolhub;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

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

    private List<ParseObject> allAnnouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        announcement = Parcels.unwrap(getIntent().getParcelableExtra("Announcement"));
        club = Parcels.unwrap(getIntent().getParcelableExtra("Club"));
        profile = Parcels.unwrap(getIntent().getParcelableExtra("ClubObject"));

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