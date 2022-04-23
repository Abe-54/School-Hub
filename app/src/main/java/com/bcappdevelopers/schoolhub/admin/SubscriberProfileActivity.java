package com.bcappdevelopers.schoolhub.admin;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.bcappdevelopers.schoolhub.R;
import com.parse.*;
import org.parceler.Parcels;

public class SubscriberProfileActivity extends AppCompatActivity {

    private static final String TAG = "Sub Profile Activity";

    private TextView tvSubsciberName;
    private TextView tvEmailAddress;
    private TextView tvDateJoined;
    private Button btnRemoveMember;
    private Button btnResetPassword;

    ParseObject subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_profile);

        subscriber = Parcels.unwrap(getIntent().getParcelableExtra("Subscriber"));

        tvSubsciberName = findViewById(R.id.tvSubscriberName);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        tvDateJoined = findViewById(R.id.tvDateJoined);
        btnRemoveMember = findViewById(R.id.btnRemoveMember);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        tvSubsciberName.setText(subscriber.getString("username"));

        //tvDateJoined.setText(subscriber.getDate(""));

        QuerySubscriber();

        btnRemoveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void QuerySubscriber() {
        ParseQuery subscriberQuery = new ParseQuery("_User");
        subscriberQuery.include("useMasterKey");
        subscriberQuery.getInBackground(subscriber.getObjectId(), new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser object, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "ERROR GETTING SUB DATA", e);
                }

                Log.i(TAG, "done: subscriber name: " + object.getUsername());
            }
        });
    }
}