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

    private static final String TAG = "CLUB LIST FRAGMENT";

    private RecyclerView rvClubList;

    public ClubListFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_club_list, container, false);
    }
}