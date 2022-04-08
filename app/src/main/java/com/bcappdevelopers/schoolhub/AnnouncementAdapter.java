package com.bcappdevelopers.schoolhub;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcappdevelopers.schoolhub.admin.Post;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.parse.ParseFile;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private static final String TAG = "AnnouncementAdapter" ;
    private final List<Announcement> announcements;
    private Context context;
    private Object Announcement;


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

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //  tvUsername = itemView.findViewById(R.id.tvUsername);
            //ivImage = itemView.findViewById(R.id.ivImage);
          //  tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(com.bcappdevelopers.schoolhub.models.Announcement announcement) {
           // tvDescription.setText(announcement.getEventDescription());
            //tvUsername.setText(announcement.getUser().getUsername());

           // ParseFile image = announcement.getImage();
          //  if (image != null) {
                //Glide.with(context).load(image.getUrl()).into(ivImage);
            }

        }
    }

