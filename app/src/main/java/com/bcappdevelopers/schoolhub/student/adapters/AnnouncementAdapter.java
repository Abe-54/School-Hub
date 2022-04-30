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

            int totalLiked = (int) announcement.getNumber("likeCounter");
            int totalDisliked = (int) announcement.getNumber("dislikeCounter");

            tvProfileName.setText(announcement.getParseObject("madeBy").getString("clubName"));
            tvAnnouncementDescription.setText(announcement.getString("eventName"));
            tvLikeCounter.setText(totalLiked + "");
            tvDislikeCounter.setText(totalDisliked + "");

            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("Clubs");

            userQuery.getInBackground(announcement.getParseObject("madeBy").getObjectId(), new GetCallback<ParseObject>() {
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

            cvAnnouncementContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostFeedActivity.class);
                    i.putExtra("Announcement", Parcels.wrap(announcement));
                    context.startActivity(i);
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isLiked){
                        isLiked = false;
                        likedIcon = R.drawable.outline_thumb_up_24;
                        if(totalLiked > 0) {
                            announcement.put("likeCounter", totalLiked - 1);
                        }
                        announcement.saveInBackground();
                        notifyDataSetChanged();
                    } else {
                        isLiked = true;
                        isDisliked = false;
                        likedIcon = R.drawable.filled_thumb_up_24;
                        dislikedICon = R.drawable.outline_thumb_down_24;
                        announcement.put("likeCounter", totalLiked + 1);
                        if(totalDisliked > 0 && !isDisliked ) {
                            announcement.put("dislikeCounter", totalDisliked - 1);
                        }
                        announcement.saveInBackground();
                        notifyDataSetChanged();
                    }

                }
            });

            btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isDisliked ){
                        isDisliked = false;
                        dislikedICon = R.drawable.outline_thumb_down_24;
                        if(totalDisliked > 0) {
                            announcement.put("dislikeCounter", totalDisliked - 1);
                        }
                        announcement.saveInBackground();
                        notifyDataSetChanged();
                    } else {
                            isDisliked = true;
                            isLiked = false;
                            dislikedICon = R.drawable.filled_thumb_down_24;
                            likedIcon = R.drawable.outline_thumb_up_24;
                            announcement.put("dislikeCounter", totalDisliked + 1);
                            if(totalLiked > 0 && !isLiked) {
                                announcement.put("likeCounter", totalLiked - 1);
                            }
                            announcement.saveInBackground();
                            notifyDataSetChanged();
                    }
                }
            });

            btnLike.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), likedIcon));
            btnDislike.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
        }
    }
}

