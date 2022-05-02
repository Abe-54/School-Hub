package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Announcement;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;
import com.parse.ParseObject;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import org.parceler.Parcels;

import java.util.List;

public class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {

    private static final String TAG = "ClubListAdapter";
    private final List<Club> clubs;
    private Context context;

    public ClubListAdapter(Context context, List<Club> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ClubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_list_item,parent,false);
        return new ClubListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubListAdapter.ViewHolder holder, int position) {
        Club club = clubs.get(position);
        holder.bind(club);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        clubs.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Club> list) {
        clubs.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivClubImage;
        private TextView tvClubName;
        private TextView tvClubDescription;
        private ConstraintLayout rlClubItemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivClubImage = itemView.findViewById(R.id.ivClubImage);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvClubDescription = itemView.findViewById(R.id.tvClubDescription);
            rlClubItemContainer = itemView.findViewById(R.id.rlClubItemContainer);
        }

        public void bind(com.bcappdevelopers.schoolhub.models.Club club) {
            tvClubName.setText(club.getClubName());
            tvClubDescription.setText(club.getClubDescription());

            int radius = 30;
            int margin = 10;

            ParseFile image = club.getClubImage();
            if(image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .transform(new CropCircleWithBorderTransformation())
                        .into(ivClubImage);
            }

            rlClubItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ClubProfileActivity.class);
                    i.putExtra("Club", Parcels.wrap(club));
                    context.startActivity(i);
                }
            });
        }
    }
}
