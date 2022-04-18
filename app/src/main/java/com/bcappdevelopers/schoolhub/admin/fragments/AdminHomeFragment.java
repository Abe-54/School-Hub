package com.bcappdevelopers.schoolhub.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.student.adapters.NewsPageAdapter;
import com.bcappdevelopers.schoolhub.CampusNewsFragment;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;

public class AdminHomeFragment extends Fragment {

    private static final String TAG = "ADMIN HOME FRAGMENT";

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.vpAdminNewsPlaceholder);
        TabLayout tabLayout = view.findViewById(R.id.adminNewsTabBar);

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);

        NewsPageAdapter adapter = new NewsPageAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new CampusNewsFragment(), "Campus News");

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
    }
}