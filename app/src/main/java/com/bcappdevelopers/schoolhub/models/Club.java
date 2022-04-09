package com.bcappdevelopers.schoolhub.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Clubs")
public class Club extends ParseObject {

    public static final String KEY_CLUB_NAME = "clubName";
    public static final String KEY_CLUB_DESCRIPTION = "clubDescription";
    public static final String KEY_CLUB_IMAGE = "clubImage";

    public String getClubName() {
        return getString(KEY_CLUB_NAME);
    }

    public void setClubName(String clubName) {
        put(KEY_CLUB_NAME, clubName);
    }

    public String getClubDescription() {
        return getString(KEY_CLUB_DESCRIPTION);
    }

    public void setClubDescription(String clubDescription) {
        put(KEY_CLUB_DESCRIPTION, clubDescription);
    }

    public ParseFile getClubImage() {
        return getParseFile(KEY_CLUB_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_CLUB_NAME, parseFile);
    }
}
