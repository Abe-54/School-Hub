package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClubListFragment extends Fragment {

    private static final String TAG = "CLUB NEWS FRAGMENT";

    private RecyclerView rvClubAnnoucements;

    public ClubListFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_club_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvClubAnnoucements = view.findViewById(R.id.rvClubAnnouncements);

        Log.i(TAG, "INSIDE OF CLUB FRAG");

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        //4. set the layout manager on rv
        queryAnnoucnements();
    }

    private void queryAnnoucnements() {
        ParseQuery<Announcement> query = ParseQuery.getQuery(Announcement.class);
        query.include("madeBy");
        query.findInBackground(new FindCallback<Announcement>() {
            @Override
            public void done(List<Announcement> announcements, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting club announcements", e);
                    return;
                }
                for (Announcement announcement : announcements) {
                    if(announcement.getEventUser() != null && announcement.getEventUser().getUsername().compareTo("admin") != 0) {
                        Log.i(TAG, "announcements: " + announcement.getEventDescription() + ", created by: " + announcement.getEventUser().getUsername());
                    }
                }
            }
        });
    }
}