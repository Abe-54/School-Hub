package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.PostFeedActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private static final String TAG = "AnnouncementAdapter";
    private final List<ParseObject> announcements;
    private Context context;

    public AnnouncementAdapter(Context context, List<ParseObject> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false);

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseObject announcement = announcements.get(position);

        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        announcements.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ParseObject> list) {
        announcements.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cvAnnouncementContainer;
        private ImageView ivUserImage;
        private TextView tvProfileName;
        private TextView tvAnnouncementDescription;
        private TextView tvLikeCounter;
        private TextView tvDislikeCounter;
        private ImageButton btnLike;
        private ImageButton btnDislike;

        boolean isLiked = false;
        int likedIcon;

        boolean isDisliked = false;
        int dislikedICon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvAnnouncementContainer = itemView.findViewById(R.id.cvAnnouncementContainer);
            ivUserImage = itemView.findViewById(R.id.ivAnnouncement);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvAnnouncementDescription = itemView.findViewById(R.id.tvAnnouncementTitle);
            tvLikeCounter = itemView.findViewById(R.id.tvLikeCount);
            tvDislikeCounter = itemView.findViewById(R.id.tvDislikeCount);
            btnLike = itemView.findViewById(R.id.btnAnnouncementLike);
            btnDislike = itemView.findViewById(R.id.btnAnnouncementDislike);

            likedIcon = R.drawable.outline_thumb_up_24;
            dislikedICon = R.drawable.outline_thumb_down_24;
        }

        public void bind(ParseObject announcement) {

            //setting total likes & dislikes
            int totalLiked = (int) announcement.getNumber("likeCounter");
            int totalDisliked = (int) announcement.getNumber("dislikeCounter");

            //setting component text values
            tvProfileName.setText(announcement.getParseObject("madeBy").getString("clubName"));
            tvAnnouncementDescription.setText(announcement.getString("eventName"));
            tvLikeCounter.setText(totalLiked + "");
            tvDislikeCounter.setText(totalDisliked + "");

            ParseQuery<ParseObject> clubAdminQuery = ParseQuery.getQuery("Clubs");

            //Querying club announcements
            clubAdminQuery.getInBackground(announcement.getParseObject("madeBy").getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject club, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting profile data", e);
                        return;
                    }
                    ParseFile image = club.getParseFile("clubImage");
                    if(image != null) {
                        Glide.with(context)
                                .load(image.getUrl())
                                .centerCrop()
                                .transform(new CropCircleWithBorderTransformation())
                                .into(ivUserImage);
                    }
                }
            });

            SetupAnnouncementButtons(announcement);

            //Enabling click announcement to go to post feed screen
            cvAnnouncementContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostFeedActivity.class);
                    i.putExtra("Announcement", Parcels.wrap(announcement));
                    context.startActivity(i);
                }
            });

            //setting up like button
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isLiked){
                        isLiked = false;
                        likedIcon = R.drawable.outline_thumb_up_24;
                        if(totalLiked > 0) {
                            announcement.put("likeCounter", totalLiked - 1);
                        }

                        ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);

                    } else {
                        isLiked = true;
                        likedIcon = R.drawable.filled_thumb_up_24;

                        announcement.put("likeCounter", totalLiked + 1);

                        if(totalDisliked > 0 &&  isDisliked == true) {
                            announcement.put("dislikeCounter", totalDisliked - 1);
                            dislikedICon = R.drawable.outline_thumb_down_24;
                            isDisliked = false;
                            ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);
                            ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
                        }

                        ParseUser.getCurrentUser().getRelation("likedAnnouncements").add(announcement);

                    }

                    ParseUser.getCurrentUser().saveInBackground();
                    announcement.saveInBackground();
                    notifyDataSetChanged();
                }
            });

            //setting up dislike button
            btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isDisliked){
                        isDisliked = false;
                        dislikedICon = R.drawable.outline_thumb_down_24;
                        if(totalDisliked > 0) {
                            announcement.put("dislikeCounter", totalDisliked - 1);
                        }
                        ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
                    } else {
                        isDisliked = true;
                        dislikedICon = R.drawable.filled_thumb_down_24;

                        announcement.put("dislikeCounter", totalDisliked + 1);

                        if(totalLiked > 0 &&  isLiked == true) {
                            announcement.put("likeCounter", totalLiked - 1);
                            likedIcon = R.drawable.outline_thumb_up_24;
                            isLiked = false;
                            ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);
                            ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
                        }

                        ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").add(announcement);
                    }

                    ParseUser.getCurrentUser().saveInBackground();
                    announcement.saveInBackground();
                    notifyDataSetChanged();
                }
            });

            //Setting like/dislike button image
            btnLike.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), likedIcon));
            btnDislike.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
        }

        private void SetupAnnouncementButtons(ParseObject announcement) {

            ParseQuery<ParseObject> likedAnnouncementQuery = ParseUser.getCurrentUser().getRelation("likedAnnouncements").getQuery();

            //Querying to get Current Users likes & dislikes
            likedAnnouncementQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> announcements, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting user data", e);
                        return;
                    }

                    Collections.sort(announcements, new Comparator<ParseObject>() {
                        @Override
                        public int compare(ParseObject date, ParseObject date1) {
                            return date.getCreatedAt().toString().compareTo(date1.getCreatedAt().toString());
                        }
                    });

                    Collections.reverse(announcements);

                    for (int i = 0; i < announcements.size(); i++) {
                        if(announcements.get(i).getObjectId().compareTo(announcement.getObjectId()) == 0) {
                            Log.i(TAG, "found liked announcement: " + announcements.get(i).getString("eventName"));
                            likedIcon = R.drawable.filled_thumb_up_24;
                            isLiked = true;
                        }
                    }

                    btnLike.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), likedIcon));
                }
            });

            ParseQuery<ParseObject> dislikedAnnouncementQuery = ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").getQuery();

            dislikedAnnouncementQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> announcements, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting announcements", e);
                        return;
                    }

                    Collections.sort(announcements, new Comparator<ParseObject>() {
                        @Override
                        public int compare(ParseObject date, ParseObject date1) {
                            return date.getCreatedAt().toString().compareTo(date1.getCreatedAt().toString());
                        }
                    });

                    Collections.reverse(announcements);

                    for (int i = 0; i < announcements.size(); i++) {
                        if(announcements.get(i).getObjectId().compareTo(announcement.getObjectId()) == 0) {
                            Log.i(TAG, "found disliked announcement: " + announcements.get(i).getString("eventName"));
                            dislikedICon = R.drawable.filled_thumb_down_24;
                            isDisliked = true;
                        }
                    }

                    btnDislike.setImageDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
                }
            });
        }
    }
}

