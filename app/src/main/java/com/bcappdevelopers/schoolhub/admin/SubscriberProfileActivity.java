package com.bcappdevelopers.schoolhub.admin;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.R;
import com.parse.*;
import org.parceler.Parcels;

import java.util.List;

public class SubscriberProfileActivity extends AppCompatActivity {

    private static final String TAG = "Sub Profile Activity";

    private TextView tvSubsciberName;
    private TextView tvEmailAddress;
    private TextView tvDateJoined;
    private Button btnRemoveMember;
    private Button btnResetPassword;

    private ParseObject subscriber;
    private ParseObject currentClub;

    private String ownedClubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_profile);

        currentClub = ParseUser.getCurrentUser().getParseObject("ownsClub");
        QueryOwnedClubData();

        subscriber = Parcels.unwrap(getIntent().getParcelableExtra("Subscriber"));

        tvSubsciberName = findViewById(R.id.tvSubscriberName);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        tvDateJoined = findViewById(R.id.tvDateJoined);
        btnRemoveMember = findViewById(R.id.btnRemoveMember);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        tvSubsciberName.setText(subscriber.getString("username"));
        tvEmailAddress.setText(subscriber.getString("emailContact"));

        QuerySubcriberClubs();

        btnRemoveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFromSubcriberClubList(subscriber, currentClub);
                RemoveFromClubMemberList(subscriber, currentClub);
                Log.i(TAG, "Removed Member: " + subscriber.getString("username"));
                Intent i = new Intent(SubscriberProfileActivity.this, AdminHomeActivity.class);
                SubscriberProfileActivity.this.startActivity(i);
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void RemoveFromSubcriberClubList(ParseObject subscriber, ParseObject club) {
        subscriber.getRelation("inClub").remove(club);

        subscriber.saveInBackground();
    }

    private void RemoveFromClubMemberList(ParseObject subscriber, ParseObject club) {
        club.getRelation("usersInClub").remove(subscriber);

        club.saveInBackground();
    }

    private void QuerySubcriberClubs() {
        ParseQuery<ParseUser> subClubQuery = new ParseQuery("_User");

        subClubQuery.getInBackground(subscriber.getObjectId(), new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser subscriber, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ERROR GETTING SUB DATA", e);
                }

                ParseQuery<ParseObject> clubList = subscriber.getRelation("inClub").getQuery();

                clubList.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting profile data", e);
                            return;
                        }
                        for (ParseObject clubs : objects) {
                            if(clubs.getString("clubName").equals(ownedClubName) ) {
                                Log.i(TAG, "Found Club " + clubs.getString("clubName") + " for user: " + subscriber.getString("username"));

                            }
                        }
                    }
                });

            }
        });
    }

    private void QueryOwnedClubData() {
        ParseQuery<ParseObject> ownedClubData = new ParseQuery("Clubs");

        ownedClubData.getInBackground( currentClub.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject club, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ERROR GETTING CLUB DATA", e);
                }
                Log.i(TAG, "Owned Club: " + club.getString("clubName"));
                ownedClubName = club.getString("clubName");
            }
        });
    }
}