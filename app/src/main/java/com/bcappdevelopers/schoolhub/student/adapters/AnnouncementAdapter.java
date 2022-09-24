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
import com.bcappdevelopers.schoolhub.RecyclerViewActionListener;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.parceler.Parcels;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private static final String TAG = "AnnouncementAdapter";
    private final List<ParseObject> announcements;
    private Context context;
    private RecyclerViewActionListener mListener;


    public interface AnnouncementAdapterCallback{
        void likeAnnouncement(String textViewvalue);
    }
    public AnnouncementAdapter(Context context, List<ParseObject> announcements, RecyclerViewActionListener mListener) {
        this.context = context;
        this.announcements = announcements;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: like button clicked");
                mListener.onViewClicked(view.getId(), holder.getAdapterPosition(), announcements.get(holder.getAdapterPosition()), holder.btnLike, holder.btnDislike);
            }
        });
        holder.btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: dislike button clicked");
                mListener.onViewClicked(view.getId(), holder.getAdapterPosition(), announcements.get(holder.getAdapterPosition()), holder.btnLike, holder.btnDislike);
            }
        });
        holder.cvAnnouncementContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: announcement container clicked");
                mListener.onViewClicked(view.getId(), holder.getAdapterPosition(), announcements.get(holder.getAdapterPosition()), holder.btnLike, holder.btnDislike);
            }
        });


        return holder;
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

            //SetupAnnouncementButtons(announcement);

            //Enabling click announcement to go to post feed screen
           /* cvAnnouncementContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostFeedActivity.class);
                    i.putExtra("Announcement", Parcels.wrap(announcement));
                    context.startActivity(i);
                }
            });*/

            //setting up like button
           /* btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if(isLiked){
//                        isLiked = false;
//                        likedIcon = R.drawable.outline_thumb_up_24;
//                        if(totalLiked > 0) {
//                            announcement.put("likeCounter", totalLiked - 1);
//                        }
//
//                        ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);
//
//                    } else {
//                        isLiked = true;
//                        likedIcon = R.drawable.filled_thumb_up_24;
//
//                        announcement.put("likeCounter", totalLiked + 1);
//
//                        if(totalDisliked > 0 &&  isDisliked == true) {
//                            announcement.put("dislikeCounter", totalDisliked - 1);
//                            dislikedICon = R.drawable.outline_thumb_down_24;
//                            isDisliked = false;
//                            ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);
//                            ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
//                        }
//
//                        ParseUser.getCurrentUser().getRelation("likedAnnouncements").add(announcement);
//
//                    }
//
//                    ParseUser.getCurrentUser().saveInBackground();
//                    announcement.saveInBackground();
//                    notifyDataSetChanged();
                }
            });

            //setting up dislike button
            btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
//                    if(isDisliked){
//                        isDisliked = false;
//                        dislikedICon = R.drawable.outline_thumb_down_24;
//                        if(totalDisliked > 0) {
//                            announcement.put("dislikeCounter", totalDisliked - 1);
//                        }
//                        ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
//                    } else {
//                        isDisliked = true;
//                        dislikedICon = R.drawable.filled_thumb_down_24;
//
//                        announcement.put("dislikeCounter", totalDisliked + 1);
//
//                        if(totalLiked > 0 &&  isLiked == true) {
//                            announcement.put("likeCounter", totalLiked - 1);
//                            likedIcon = R.drawable.outline_thumb_up_24;
//                            isLiked = false;
//                            ParseUser.getCurrentUser().getRelation("likedAnnouncements").remove(announcement);
//                            ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").remove(announcement);
//                        }
//
//                        ParseUser.getCurrentUser().getRelation("dislikedAnnouncements").add(announcement);
//                    }
//
//                    ParseUser.getCurrentUser().saveInBackground();
//                    announcement.saveInBackground();
//                    notifyDataSetChanged();
                }
            });

//            //Setting like/dislike button image
//            btnLike.setImageDrawable(
//                    ContextCompat.getDrawable(getApplicationContext(), likedIcon));
//            btnDislike.setImageDrawable(
//                    ContextCompat.getDrawable(getApplicationContext(), dislikedICon));*/
        }


    }
}

