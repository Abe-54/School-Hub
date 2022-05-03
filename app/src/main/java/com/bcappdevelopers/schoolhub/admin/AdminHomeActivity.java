package com.bcappdevelopers.schoolhub.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.admin.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    private static final String TAG = "ADMIN HOME ACTIVITY";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        bottomNavigationView = findViewById(R.id.adminBottomNavView);

        // handle navigation selection
        // Please RENAME the fragment destinations
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.homeButton:
                        fragment = new AdminHomeFragment();
                        break;
                    case R.id.subscribersList:
                        fragment = new SubscriberListFragment();
                        break;
                    case R.id.createPostEvent:
                        showPostDialog();
                        fragment = null;
                        break;
                    /*case R.id.clubMailBox:
                        fragment = new AdminMailFragment();
                        break;*/
                    case R.id.clubProfile:
                        fragment = new AdminProfileFragment();
                        break;
                    default:
                        fragment = new AdminHomeFragment();
                        break;
                }

                if(fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.adminPlaceholder, fragment).commit();
                }

                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.homeButton);
    }

    private void showPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeAlertFragment createPostDialouge = ComposeAlertFragment.newInstance("Choose The Type of Post:");
        createPostDialouge.show(fm, "fragment_choose_post_type");
    }
}