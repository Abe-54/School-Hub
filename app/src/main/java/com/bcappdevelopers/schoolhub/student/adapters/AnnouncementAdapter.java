package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private static final String TAG = "CampusAnnouncementAdapter" ;
    private final List<Announcement> announcements;
    private Context context;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
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
        Announcement announcement = announcements.get(position);

        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivUserImage;
        private TextView tvProfileName;
        private TextView tvAnnouncementDescription;
        private TextView tvUpVoteCount;
        private TextView tvDownVoteCount;
        private Button btnUpVote;
        private Button btnDownVote;
        private Button btnComment;
        private Button btnShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserImage = itemView.findViewById(R.id.ivClubImage);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvAnnouncementDescription = itemView.findViewById(R.id.tvAnnouncementDescription);
            tvUpVoteCount = itemView.findViewById(R.id.tvUpVoteCount);
            tvDownVoteCount = itemView.findViewById(R.id.tvDownVoteCount);
            btnUpVote = itemView.findViewById(R.id.btnUpVote);
            btnDownVote = itemView.findViewById(R.id.btnDownVote);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);
        }

        public void bind(com.bcappdevelopers.schoolhub.models.Announcement announcement) {
            tvProfileName.setText(announcement.getEventUser().getUsername());
            tvAnnouncementDescription.setText(announcement.getEventDescription());
            tvUpVoteCount.setText("0");
            tvDownVoteCount.setText("0");
        }
    }
}

