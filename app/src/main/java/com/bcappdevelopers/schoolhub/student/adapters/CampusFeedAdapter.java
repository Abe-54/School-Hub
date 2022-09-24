package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.CampusFeedModel;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

import java.util.List;

public class CampusFeedAdapter extends RecyclerView.Adapter<CampusFeedAdapter.ViewHolder> {

    private static final String TAG = "ClubListAdapter";
    private final List<CampusFeedModel> campusFeeds;
    private Context context;

    public CampusFeedAdapter(Context context, List<CampusFeedModel> campusFeeds) {
        this.context = context;
        this.campusFeeds = campusFeeds;
    }

    @NonNull
    @Override
    public CampusFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false);
        return new CampusFeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampusFeedAdapter.ViewHolder holder, int position) {
        CampusFeedModel campusFeedModel = campusFeeds.get(position);
        holder.bind(campusFeedModel);
    }

    @Override
    public int getItemCount() {
        return campusFeeds.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        campusFeeds.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<CampusFeedModel> list) {
        campusFeeds.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAnnouncementCreator;
        private TextView tvAnnouncementDescription;
        private ImageView ivAnnouncementImage;
        private CardView cvAnnouncementContainer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAnnouncementCreator = itemView.findViewById(R.id.tvProfileName);
            tvAnnouncementDescription = itemView.findViewById(R.id.tvAnnouncementTitle);
            ivAnnouncementImage = itemView.findViewById(R.id.ivAnnouncement);
            cvAnnouncementContainer = itemView.findViewById(R.id.cvAnnouncementContainer);
        }

        public void bind(CampusFeedModel campusFeedModel) {


            String title = campusFeedModel.getTitle();
            String desc = campusFeedModel.getDescription();
            //String date = campusFeedModel.getDate();

            tvAnnouncementCreator.setText("Bloomfield College");
            tvAnnouncementDescription.setText(title);

            int radius = 30;
            int margin = 10;

            Glide.with(context)
                    .load(R.mipmap.college_profile_icon)
                    .centerCrop()
                    .transform(new CropCircleWithBorderTransformation())
                    .into(ivAnnouncementImage);

            cvAnnouncementContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(campusFeedModel.getLinkToArticle()));
                    context.startActivity(i);
                }
            });
        }
    }
}
