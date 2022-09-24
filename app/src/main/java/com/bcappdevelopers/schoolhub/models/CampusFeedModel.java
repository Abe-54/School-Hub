package com.bcappdevelopers.schoolhub.models;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CampusFeedModel implements Parcelable {

    public String title;
    public String description;
    public String linkToArticle;
    public String date;
    public String guid;

    public CampusFeedModel() {

    }

    private CampusFeedModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        linkToArticle = in.readString();
        date = in.readString();
        guid = in.readString();
    }

    public static final Creator<CampusFeedModel> CREATOR = new Creator<CampusFeedModel>() {
        @Override
        public CampusFeedModel createFromParcel(Parcel in) {
            return new CampusFeedModel(in);
        }

        @Override
        public CampusFeedModel[] newArray(int size) {
            return new CampusFeedModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLinkToArticle(){
        return linkToArticle;
    }

    public void setLinkToArticle(String link) {
        this.linkToArticle = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
    }
}
