package com.bcappdevelopers.schoolhub.student;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.student.fragments.AmenetiesFragment;
import com.bcappdevelopers.schoolhub.student.fragments.ClubListFragment;
import com.bcappdevelopers.schoolhub.student.fragments.StudentHomeFragment;
import com.bcappdevelopers.schoolhub.student.fragments.StudentProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentHomeActivity extends AppCompatActivity {

    private static final String TAG = "STUDENT HOME ACTIVITY";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        bottomNavigationView = findViewById(R.id.studentBottomNav);

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.homeButton:
                                fragment = new StudentHomeFragment();
                                break;
                            case R.id.clubList:
                                fragment = new ClubListFragment();
                                break;
                            case R.id.ammenitiesMap:
                                fragment = new AmenetiesFragment();
                                break;
                            case R.id.studentProfile:
                                fragment = new StudentProfileFragment();
                                break;
                            default:
                                fragment = new StudentProfileFragment();
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.placeholder, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.homeButton);

    }
}
