package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.PostFeedActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.parceler.Parcels;

import java.util.List;

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
        private TextView tvUpVoteCount;
        private TextView tvDownVoteCount;
        private Button btnUpVote;
        private Button btnDownVote;
        private Button btnShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvAnnouncementContainer = itemView.findViewById(R.id.cvAnnouncementContainer);
            ivUserImage = itemView.findViewById(R.id.ivAnnouncement);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvAnnouncementDescription = itemView.findViewById(R.id.tvAnnouncementDescription);
            tvUpVoteCount = itemView.findViewById(R.id.tvUpVoteCount);
            tvDownVoteCount = itemView.findViewById(R.id.tvDownVoteCount);
            btnUpVote = itemView.findViewById(R.id.btnUpVote);
            btnDownVote = itemView.findViewById(R.id.btnDownVote);
            btnShare = itemView.findViewById(R.id.btnShare);
        }

        public void bind(ParseObject announcement) {

            tvProfileName.setText(announcement.getParseObject("madeBy").getString("clubName"));
            tvAnnouncementDescription.setText(announcement.getString("eventDescription"));
            tvUpVoteCount.setText("0");
            tvDownVoteCount.setText("0");

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
        }
    }
}

