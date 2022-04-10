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
import com.parse.ParseObject;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private static final String TAG = "Profile Adapter";
    private final List<ParseObject> clubs;
    private Context context;

    public ProfileAdapter(Context context, List<ParseObject> clubs) {
        this.clubs = clubs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_list_item,parent,false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ParseObject club = clubs.get(position);
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

        public void bind(ParseObject club) {
            tvClubName.setText(club.getString("clubName"));
            tvClubDescription.setText(club.getString("clubDescription"));

            int radius = 30;
            int margin = 10;

            ParseFile image = club.getParseFile("clubImage");
            if(image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .transform(new CropCircleWithBorderTransformation())
                        .into(ivClubImage);
            }
        }
    }
}
