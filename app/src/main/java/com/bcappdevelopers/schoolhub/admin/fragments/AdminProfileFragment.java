package com.bcappdevelopers.schoolhub.admin.fragments;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AdminProfileFragment extends Fragment {

    private static final String TAG = "CLUB PROFILE ACTIVITY";

    TextView tvClubName;
    TextView tvClubDescription;
    ImageView ivClubProfile;
    Button btnEventArchive;
    ImageButton btnClubSettings;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvClubName = view.findViewById(R.id.tvAdminClubName);
        tvClubDescription = view.findViewById(R.id.tvAdminClubDescription);
        ivClubProfile = view.findViewById(R.id.ivAdminClubProfile);
        btnEventArchive = view.findViewById(R.id.btnAdminEventsArchive);
        btnClubSettings = view.findViewById(R.id.btnSettings);

        QueryClubData();
        
        btnEventArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: going to event archive");
            }
        });

        btnClubSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: going to settings");
            }
        });

    }

    private void QueryClubData() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");

        userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting user data", e);
                    return;
                }

                ParseQuery<ParseObject> clubQuery = ParseQuery.getQuery("Clubs");

                clubQuery.getInBackground(user.getParseObject("ownsClub").getObjectId(), new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject club, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting club data", e);
                            return;
                        }
                        Log.i(TAG, "Found: " + club.getString("clubName"));

                        tvClubName.setText(club.getString("clubName"));
                        tvClubDescription.setText(club.getString("clubDescription"));

                        ParseFile clubImage = club.getParseFile("clubImage");

                        if(clubImage != null) {
                            Glide.with(getContext())
                                    .load(clubImage.getUrl())
                                    .centerCrop()
                                    .transform(new CropCircleWithBorderTransformation())
                                    .into(ivClubProfile);
                        }
                    }
                });
            }
        });
    }
}