package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import org.jetbrains.annotations.NotNull;

public class CampusNewsFragment extends Fragment {

    private RecyclerView rvCampusAnnoucements;

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

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        //4. set the layout manager on rv
    }
}