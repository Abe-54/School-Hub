package com.bcappdevelopers.schoolhub.student.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bcappdevelopers.schoolhub.R;


public class AmenetiesFragment extends Fragment {

    public AmenetiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragement_ameneties_map, container, false);
    }
}