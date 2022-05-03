package com.bcappdevelopers.schoolhub.student.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bcappdevelopers.schoolhub.LoginActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.student.adapters.ClubProfileAdapter;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileFragment extends Fragment {

    private static final String TAG = "STUDENT PROFILE FRAGMENT";

    private RecyclerView rvClubList;
    private TextView tvName;
    private ImageView ivProfileIcon;
    private ClubProfileAdapter adapter;
    private List<ParseObject> allClubs;
    private Button signOutButton;
    private FrameLayout progressOverlay;
    private SwipeRefreshLayout swipeContainer;

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
        ivProfileIcon = view.findViewById(R.id.ivStudentIcon);
        signOutButton = view.findViewById(R.id.btnSignOut);
        progressOverlay = view.findViewById(R.id.progress_overlay_student_profile);
        allClubs = new ArrayList<>();
        adapter = new ClubProfileAdapter(getContext(), allClubs);

        setVisible();

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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerStudentProfile);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                adapter.clear();
                setVisible();
                queryData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.burgendy);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
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

                        ParseFile image = user.getParseFile("profilePicture");
                        if(image != null) {
                            Glide.with(getContext())
                                    .load(image.getUrl())
                                    .centerCrop()
                                    .transform(new CropCircleWithBorderTransformation())
                                    .into(ivProfileIcon);
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
                                    Log.i(TAG, "Found Club " + clubs.getString("clubName") + " for user: " + user.getString("username"));
                                }

                                allClubs.addAll(objects);
                                setInvisible();

                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });
    }

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
        signOutButton.setClickable(true);
    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        signOutButton.setClickable(false);
    }
}