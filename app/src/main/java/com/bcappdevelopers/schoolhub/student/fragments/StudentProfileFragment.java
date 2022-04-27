package com.bcappdevelopers.schoolhub.student.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.LoginActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.models.Profile;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bcappdevelopers.schoolhub.student.adapters.ClubListAdapter;
import com.bcappdevelopers.schoolhub.student.adapters.ProfileAdapter;
import com.parse.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {

    private static final String TAG = "STUDENT PROFILE FRAGMENT";

    private RecyclerView rvClubList;
    private TextView tvName;
    private ProfileAdapter adapter;
    private List<ParseObject> allClubs;
    private Button signOutButton;

    private boolean allowRefresh = true;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvClubList = view.findViewById(R.id.rvClubListProfileScreen);
        tvName = view.findViewById(R.id.tvStudentName);
        signOutButton = view.findViewById(R.id.btnSignOut);
        allClubs = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), allClubs);

        Log.i(TAG, "INSIDE OF PROFILE FRAG");

        tvName.setText(ParseUser.getCurrentUser().getUsername());

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        rvClubList.setAdapter(adapter);
        //4. set the layout manager on rv
        rvClubList.setLayoutManager(new LinearLayoutManager(getContext()));
//        queryData();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
                Log.i(TAG, "LOGGING OUT");
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // Refresh your fragment here
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        allClubs.clear();
        queryData();
    }

    private void queryData() {

        ParseQuery<ParseUser> userObject = ParseQuery.getQuery("_User");

        userObject.findInBackground(new FindCallback<ParseUser>() {
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
                                    Log.i(TAG, "Found Club " + clubs.getString("clubName") + " for user: " + user.getString("username"));
                                }

                                allClubs.addAll(objects);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }
}