package com.bcappdevelopers.schoolhub;

import android.app.Application;

import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.models.Comment;
import com.bcappdevelopers.schoolhub.models.Profile;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Announcement.class);
        ParseObject.registerSubclass(Club.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
