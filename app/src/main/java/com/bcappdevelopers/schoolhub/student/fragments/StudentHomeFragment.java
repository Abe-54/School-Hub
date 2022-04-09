package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StudentHomeFragment extends Fragment {

    private static final String TAG = "STUDENT HOME FRAGMENT";

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.vpNewsPlaceholder);
        TabLayout tabLayout = view.findViewById(R.id.newsTabBar);

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);

        NewsPageAdapter adapter = new NewsPageAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new CampusNewsFragment(), "Campus News");
        adapter.addFrag(new ClubNewsFragment(), "Clubs News");

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
    }
}