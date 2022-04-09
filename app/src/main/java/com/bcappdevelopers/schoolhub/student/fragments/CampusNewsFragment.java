package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.student.adapters.CampusAnnouncementAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CampusNewsFragment extends Fragment {

    private static final String TAG = "CAMPUS NEWS FRAGMENT";

    private RecyclerView rvCampusAnnoucements;
    private CampusAnnouncementAdapter adapter;
    private List<Announcement> allAnnouncements;

    public CampusNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campus_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCampusAnnoucements = view.findViewById(R.id.rvCampusAnnouncements);
        allAnnouncements = new ArrayList<>();
        adapter = new CampusAnnouncementAdapter(getContext(), allAnnouncements);

        Log.i(TAG, "INSIDE OF CAMPUS FRAG");

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        rvCampusAnnoucements.setAdapter(adapter);
        //4. set the layout manager on rv
        rvCampusAnnoucements.setLayoutManager(new LinearLayoutManager(getContext()));
        queryAnnoucnements();
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
                    if(announcement.getEventUser() != null && announcement.getEventUser().getUsername().compareTo("admin") == 0) {
                        Log.i(TAG, "announcements: " + announcement.getEventDescription() + ", created by: " + announcement.getEventUser().getUsername());
                        allAnnouncements.add(announcement);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
