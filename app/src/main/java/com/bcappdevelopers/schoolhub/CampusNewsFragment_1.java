package com.bcappdevelopers.schoolhub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bcappdevelopers.schoolhub.RSS.RSSDownloader;
import com.parse.*;
import org.jetbrains.annotations.NotNull;

import static com.parse.Parse.getApplicationContext;

public class CampusNewsFragment_1 extends Fragment implements RecyclerViewActionListener {

    private static final String TAG = "CAMPUS NEWS FRAGMENT";
    private static final String COLLEGE = "Bloomfield College";
    private static final String COLLEGE_NEWS_URL = "https://bloomfield.edu/about-us/news/feed";

    private RecyclerView rvCampusAnnoucements;
    //private FrameLayout progressOverlay;

    boolean isLiked = false;
    int likedIcon;

    boolean isDisliked = false;
    int dislikedICon;

    public CampusNewsFragment_1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_campus_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        likedIcon = R.drawable.outline_thumb_up_24;
        dislikedICon = R.drawable.outline_thumb_down_24;
        rvCampusAnnoucements = view.findViewById(R.id.rvCampusAnnouncements);

        Log.i(TAG, "INSIDE OF CAMPUS FRAG getting from RSS");

        //4. set the layout manager on rv
        rvCampusAnnoucements.setLayoutManager(new LinearLayoutManager(getContext()));

        new RSSDownloader(getContext(),COLLEGE_NEWS_URL,rvCampusAnnoucements).execute();

//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerCampusNews);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                swipeContainer.setRefreshing(false);
//                new RSSDownloader(getContext(),COLLEGE_NEWS_URL,rvCampusAnnoucements).execute();
//                setVisible();
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(R.color.burgendy);

    }
//
//    public void setInvisible() {
//        progressOverlay.setVisibility(View.INVISIBLE);
//    }
//    public void setVisible() {
//        progressOverlay.setVisibility(View.VISIBLE);
//    }

    @Override
    public void setupAnnouncements(ParseObject announcement, ImageButton btnLike, ImageButton btnDislike) {
        //TODO: redo setup loaded announcements
    }
    @Override
    public void onViewClicked(int clickedViewId, int clickedItemPosition, ParseObject announcement, ImageButton btnLike, ImageButton btnDislike) {

        //TODO: redo like/dislike logic, current version is full of bugs

        Log.i(TAG, "current announcement: " + announcement.getString("eventName"));

        //setting total likes & dislikes
        int totalLiked = (int) announcement.getNumber("likeCounter");
        int totalDisliked = (int) announcement.getNumber("dislikeCounter");

        switch (clickedViewId) {
            case R.id.btnAnnouncementLike:
                break;
            case R.id.btnAnnouncementDislike:
                break;
            case R.id.cvAnnouncementContainer:
//                Intent i = new Intent(getContext(), PostFeedActivity.class);
//                i.putExtra("Announcement", Parcels.wrap(announcement));
//                getContext().startActivity(i);
                break;
        }

        //Setting like/dislike button image
        btnLike.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), likedIcon));
        btnDislike.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
    }
}
