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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

    private List<ParseObject> allAnnouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        announcement = Parcels.unwrap(getIntent().getParcelableExtra("Announcement"));
        club = Parcels.unwrap(getIntent().getParcelableExtra("Club"));

        if(announcement != null) {
            clubName = announcement.getEventClub().getString("clubName");
        } else {
            clubName = club.getClubName();
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

        queryData();
    }

    private void queryData() {
        if(announcement != null) {
            ParseQuery<Announcement> query = ParseQuery.getQuery(Announcement.class);
            query.include("madeBy");
            query.findInBackground(new FindCallback<Announcement>() {
                @Override
                public void done(List<Announcement> announcements, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting club announcements", e);
                        return;
                    }
                    for (Announcement singleAnnouncement : announcements) {
                        if (singleAnnouncement.getEventClub() != null && singleAnnouncement.getEventClub().getString("clubName").compareTo(clubName) == 0) {
                            Log.i(TAG, "announcements: " + singleAnnouncement.getEventDescription() + ", created by: " + singleAnnouncement.getEventClub().getString("clubName"));
                            allAnnouncements.add(singleAnnouncement);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } else if(club != null) {
            ParseQuery<Club> clubQuery = ParseQuery.getQuery(Club.class);
            clubQuery.findInBackground(new FindCallback<Club>() {
                @Override
                public void done(List<Club> clubs, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting club", e);
                        return;
                    }
                    for (Club singleClub : clubs) {
                        ParseQuery<ParseObject> announcmentQuery = singleClub.getRelation("clubAnnouncements").getQuery();

                        announcmentQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Issues getting profile data", e);
                                    return;
                                }
                                for (ParseObject singleAnnnouncement : objects) {
                                    Log.i(TAG, "Found Announcement " + singleAnnnouncement.getString("eventDescription") + " from " + singleAnnnouncement.getParseObject("madeBy"));
                                    allAnnouncements.add(singleAnnnouncement);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }
    }
}