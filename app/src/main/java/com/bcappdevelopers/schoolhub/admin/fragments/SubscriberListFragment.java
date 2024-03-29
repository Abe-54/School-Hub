package com.bcappdevelopers.schoolhub.admin.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.admin.adapters.SubscriberListAdapter;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.ClubListAdapter;
import com.parse.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubscriberListFragment extends Fragment {

    private static final String TAG = "SUB LIST FRAGMENT";

    private RecyclerView rvSubList;
    private SubscriberListAdapter adapter;
    private List<ParseObject> allSubs;
    private FrameLayout progressOverlay;
    private SwipeRefreshLayout swipeContainer;

    public SubscriberListFragment() {
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
        return inflater.inflate(R.layout.fragment_subscriber_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSubList = view.findViewById(R.id.rvSubList);
        progressOverlay = view.findViewById(R.id.progress_overlay_sub_list);
        allSubs = new ArrayList<>();
        adapter = new SubscriberListAdapter(getContext(), allSubs);

        setVisible();

        Log.i(TAG, "INSIDE OF CLUB FRAG");
//
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.include4);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).setTitle("Subscribers");

        //STEPS
        //0. create layout for 1 row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on rv
        rvSubList.setAdapter(adapter);
        //4. set the layout manager on rv
        rvSubList.setLayoutManager(new LinearLayoutManager(getContext()));
        querySubscribers();

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
                setVisible();
                querySubscribers();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.burgendy);
    }

    private void querySubscribers() {
        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");

        userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                ParseQuery<ParseObject> subscriberQuery = user.getParseObject("ownsClub").getRelation("usersInClub").getQuery();

                subscriberQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> subscribers, ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Issues getting subcribers", e);
                            return;
                        }
                        for (ParseObject subscriber : subscribers) {
                            Log.i(TAG, "Found: " + subscriber.getString("username"));
                        }
                        allSubs.addAll(subscribers);
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
}