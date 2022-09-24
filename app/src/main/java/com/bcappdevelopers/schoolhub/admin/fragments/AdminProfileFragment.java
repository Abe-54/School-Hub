package com.bcappdevelopers.schoolhub.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bcappdevelopers.schoolhub.LoginActivity;
import com.bcappdevelopers.schoolhub.PostFeedActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.RecyclerViewActionListener;
import com.bcappdevelopers.schoolhub.admin.AdminHomeActivity;
import com.bcappdevelopers.schoolhub.admin.ClubSettingsActivity;
import com.bcappdevelopers.schoolhub.admin.CreatePostActivity;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.AnnouncementAdapter;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.parse.Parse.getApplicationContext;


public class AdminProfileFragment extends Fragment implements RecyclerViewActionListener {

    private static final String TAG = "CLUB PROFILE ACTIVITY";

    TextView tvClubName;
    TextView tvClubDescription;
    ImageView ivClubProfile;
    Button btnEventArchive;
    Button btnSignOut;
    ImageButton btnClubSettings;
    RecyclerView rvAdminProfileAnnouncements;
    FrameLayout progressOverlay;
    SwipeRefreshLayout swipeContainer;

    private AnnouncementAdapter adapter;
    private List<ParseObject> allAnnouncements;

    boolean isLiked = false;
    int likedIcon;

    boolean isDisliked = false;
    int dislikedICon;

    ParseObject clubObject;
    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        likedIcon = R.drawable.outline_thumb_up_24;
        dislikedICon = R.drawable.outline_thumb_down_24;

        tvClubName = view.findViewById(R.id.tvAdminClubName);
        tvClubDescription = view.findViewById(R.id.tvAdminClubDescription);
        ivClubProfile = view.findViewById(R.id.ivAdminClubProfile);
        btnEventArchive = view.findViewById(R.id.btnAdminEventsArchive);
        btnClubSettings = view.findViewById(R.id.btnSettings);
        btnSignOut = view.findViewById(R.id.btnAdminSignOut);
        rvAdminProfileAnnouncements = view.findViewById(R.id.rvAdminProfileAnnouncements);
        progressOverlay = view.findViewById(R.id.progress_overlay_admin_profile);

        allAnnouncements = new ArrayList<>();
        adapter = new AnnouncementAdapter(getContext(), allAnnouncements, this);

        QueryClubData();

        rvAdminProfileAnnouncements.setAdapter(adapter);
        //4. set the layout manager on rv
        rvAdminProfileAnnouncements.setLayoutManager(new LinearLayoutManager(getContext()));
        
        btnEventArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: going to event archive");
            }
        });

        btnClubSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: going to settings");
                Intent i = new Intent(getContext(), ClubSettingsActivity.class);
                i.putExtra("Club", clubObject);
                getContext().startActivity(i);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
                Log.i(TAG, "LOGGING OUT");
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerSubList);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(false);
                adapter.clear();
                QueryClubData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.burgendy);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        QueryClubData();
    }

    private void QueryClubData() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");

        userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting user data", e);
                    return;
                }

                ParseQuery<ParseObject> clubQuery = ParseQuery.getQuery("Clubs");

                clubQuery.getInBackground(user.getParseObject("ownsClub").getObjectId(), new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject club, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting club data", e);
                            return;
                        }
                        clubObject = club;

                        Log.i(TAG, "Found: " + club.getString("clubName"));

                        tvClubName.setText(club.getString("clubName"));
                        tvClubDescription.setText(club.getString("clubDescription"));

                        ParseFile clubImage = club.getParseFile("clubImage");

                        if(clubImage != null) {
                            Glide.with(getContext())
                                    .load(clubImage.getUrl())
                                    .centerCrop()
                                    .transform(new CropCircleWithBorderTransformation())
                                    .into(ivClubProfile);
                        }

                        QueryClubAnnouncements(clubObject);
                    }
                });
            }
        });
    }

    private void QueryClubAnnouncements(ParseObject clubObject) {

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date sevenDaysAgo = cal.getTime();

        ParseQuery<Club> query = ParseQuery.getQuery(Club.class);

        query.getInBackground(clubObject.getObjectId(), new GetCallback<Club>() {
            @Override
            public void done(Club club, ParseException e) {

                ParseQuery<ParseObject> announcementQuery = club.getRelation("clubAnnouncements").getQuery();
                announcementQuery.include("madeBy");
                announcementQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> announcements, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting club announcements", e);
                            return;
                        }

                        for (ParseObject announcement : announcements) {
                            if(announcement.getCreatedAt().after(sevenDaysAgo)) {
                                Log.i(TAG, "found announcement: " + announcement.getString("eventName"));
                                allAnnouncements.add(announcement);
                            }
                        }

                        Collections.sort(allAnnouncements, new Comparator<ParseObject>() {
                            @Override
                            public int compare(ParseObject date, ParseObject date1) {
                                return date.getCreatedAt().toString().compareTo(date1.getCreatedAt().toString());
                            }
                        });

                        Collections.reverse(allAnnouncements);
                        setInvisible();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void setupAnnouncements(ParseObject announcement, ImageButton btnLike, ImageButton btnDislike) {

    }

    @Override
    public void onViewClicked(int clickedViewId, int clickedItemPosition, ParseObject announcement, ImageButton btnLike, ImageButton btnDislike) {

        //setting total likes & dislikes
        int totalLiked = (int) announcement.getNumber("likeCounter");
        int totalDisliked = (int) announcement.getNumber("dislikeCounter");

        switch (clickedViewId) {
            case R.id.btnAnnouncementLike:
                break;
            case R.id.btnAnnouncementDislike:
                break;
            case R.id.cvAnnouncementContainer:
                Intent i = new Intent(getContext(), PostFeedActivity.class);
                i.putExtra("Announcement", Parcels.wrap(announcement));
                getContext().startActivity(i);
                break;
        }
    }
}