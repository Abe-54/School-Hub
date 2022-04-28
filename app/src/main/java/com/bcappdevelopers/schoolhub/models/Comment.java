package com.bcappdevelopers.schoolhub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comments")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT_CONTENTS = "commentContents";
    public static final String KEY_COMMENT_CREATOR = "commentCreator";
    public static final String KEY_COMMENT_POST= "post";

    public String getCommentContents() {
        return getString(KEY_COMMENT_CONTENTS);
    }

    public void setCommentContents(String comment) {
        put(KEY_COMMENT_CONTENTS, comment);
    }

    public ParseUser getCommentCreator() {
        return getParseUser(KEY_COMMENT_CREATOR);
    }

    public void setCommentCreator(ParseUser user){
        put(KEY_COMMENT_CREATOR, user);
    }

    public ParseObject getPost(){
        return getParseObject(KEY_COMMENT_POST);
    }

    public void setPost(ParseObject post) {
        put(KEY_COMMENT_POST, post);
    }
}
