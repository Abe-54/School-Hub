package com.bcappdevelopers.schoolhub.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("announcements")
public class Announcement extends ParseObject {

    public static final String KEY_DESCRIPTION = "eventDescription";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_EVENT = "eventName";
    public static final String KEY_LOCATION = "eventLocationAtSchool";

    public static final String KEY_CREATED_KEY = "createdAt";

    public String getEventDescription (){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public String getLocationAtSchool (){
        return getString(KEY_LOCATION);
    }

    public void setLocationAtSchool(String locationAtSchool){
        put(KEY_LOCATION, locationAtSchool);
    }

    public ParseUser getEventName() {
        return getParseUser(KEY_EVENT);
    }

    public void setUser(ParseUser user){
        put(KEY_EVENT, user);
    }
}
