package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;

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

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivClubImage;
        private TextView tvClubName;
        private TextView tvClubDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivClubImage = itemView.findViewById(R.id.ivClubImage);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvClubDescription = itemView.findViewById(R.id.tvClubDescription);
        }

        public void bind(com.bcappdevelopers.schoolhub.models.Club club) {
            tvClubName.setText(club.getClubName());
            tvClubDescription.setText(club.getClubDescription());

            ParseFile image = club.getClubImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivClubImage);
            }
        }
    }
}
