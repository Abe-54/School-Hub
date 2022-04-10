package com.bcappdevelopers.schoolhub.models;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Profile extends ParseObject {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_IN_CLUB = "inClub";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username){
        put(KEY_USERNAME, username);
    }


}
