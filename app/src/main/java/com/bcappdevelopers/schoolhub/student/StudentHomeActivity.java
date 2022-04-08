package com.bcappdevelopers.schoolhub.student;


import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.student.fragments.AmenetiesFragment;
import com.bcappdevelopers.schoolhub.student.fragments.fragment_club_list;
import com.bcappdevelopers.schoolhub.student.fragments.fragment_student_home;
import com.bcappdevelopers.schoolhub.student.fragments.fragment_student_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StudentHomeActivity extends AppCompatActivity {

    private static final String TAG = "STUDENT HOME ACTIVITY";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
       //TODO: Check Flixter for fetch Data
        // Log.i(TAG,"Announcement: " + announcement.getEventName() + "Description: " + announcement.getEventDescription());

        bottomNavigationView = findViewById(R.id.studentBottomNav);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment studentHome = new fragment_student_home();
        final Fragment clubList = new fragment_club_list();
        final Fragment profileScreen = new fragment_student_profile();
        final Fragment amenetiesScreen = new AmenetiesFragment();

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.homeButton:
                                fragment = studentHome;
                                break;
                            case R.id.clubsList:
                                fragment = clubList;
                                break;
                            case R.id.amenetiesMap:
                                fragment = amenetiesScreen;
                                break;
                            case R.id.profilebutton:
                                fragment = profileScreen;
                                break;
                            default:
                                fragment = studentHome;
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
