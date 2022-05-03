package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.parse.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClubNewsFragment extends Fragment {

    private static final String TAG = "CLUB NEWS FRAGMENT";
    private static final String COLLEGE = "Bloomfield College";

    private RecyclerView rvClubAnnoucements;
    private AnnouncementAdapter adapter;
    private List<ParseObject> allAnnouncements;
    private FrameLayout progressOverlay;
    private SwipeRefreshLayout swipeContainer;

    public ClubNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_club_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvClubAnnoucements = view.findViewById(R.id.rvClubAnnouncements);
        progressOverlay = view.findViewById(R.id.progress_overlay_club_news);
        allAnnouncements = new ArrayList<>();
        adapter = new AnnouncementAdapter(getContext(), allAnnouncements);

        setVisible();

        Log.i(TAG, "INSIDE OF CLUB FRAG");

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        rvClubAnnoucements.setAdapter(adapter);
        //4. set the layout manager on rv
        rvClubAnnoucements.setLayoutManager(new LinearLayoutManager(getContext()));
        queryAnnoucnements();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerClubNews);
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
                queryAnnoucnements();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.burgendy);
    }

    private void queryAnnoucnements() {
        ParseQuery<Announcement> query = ParseQuery.getQuery(Announcement.class);
        query.include("madeBy");
        query.findInBackground(new FindCallback<Announcement>() {
            @Override
            public void done(List<Announcement> announcements, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting campus announcements", e);
                    return;
                }
                for (Announcement announcement : announcements) {

                    if(announcement.getParseObject("madeBy") == null){
                        Log.i(TAG, "No Announcement Object");
                        return;
                    }

                    ParseQuery<ParseObject> usersInClub = announcement.getEventClub().getRelation("usersInClub").getQuery();
                    usersInClub.include("inClub");

                    usersInClub.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> users, ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issues getting club users", e);
                                return;
                            }

                            for(ParseObject user : users) {
                                if(user.getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) == 0 &&
                                        announcement.getEventClub().getString("clubName").compareTo(COLLEGE) != 0) {
                                    Log.i(TAG, "User: " + user.getString("username") + " is in Club " + announcement.getEventClub().getString("clubName"));
                                    allAnnouncements.add(announcement);
                                }
                            }

                            Collections.sort(allAnnouncements, new Comparator<ParseObject>() {
                                @Override
                                public int compare(ParseObject date, ParseObject date1) {
                                    return date.getCreatedAt().toString().compareTo(date1.getCreatedAt().toString());
                                }
                            });

                            Collections.reverse(allAnnouncements);

                            setInvisible();

                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}