package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bcappdevelopers.schoolhub.student.adapters.ClubListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClubListFragment extends Fragment {

    private static final String TAG = "CLUB LIST FRAGMENT";

    private RecyclerView rvClubList;
    private ClubListAdapter adapter;
    private List<Club> allClubs;

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

        rvClubList = view.findViewById(R.id.rvClubList);
        allClubs = new ArrayList<>();
        adapter = new ClubListAdapter(getContext(), allClubs);

        Log.i(TAG, "INSIDE OF CLUB FRAG");

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        rvClubList.setAdapter(adapter);
        //4. set the layout manager on rv
        rvClubList.setLayoutManager(new LinearLayoutManager(getContext()));
        queryClubs();
    }

    private void queryClubs() {
        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);
        query.findInBackground(new FindCallback<Club>() {
            @Override
            public void done(List<Club> clubs, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting club announcements", e);
                    return;
                }
                for (Club club : clubs) {
                    Log.i(TAG, "Club: " + club.getClubName());
                }
                allClubs.addAll(clubs);
                adapter.notifyDataSetChanged();
            }
        });
    }
}