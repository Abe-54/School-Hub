package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class ClubProfileAdapter extends RecyclerView.Adapter<ClubProfileAdapter.ViewHolder>{

    private static final String TAG = "Profile Adapter";
    private final List<ParseObject> clubs;
    private Context context;

    public ClubProfileAdapter(Context context, List<ParseObject> clubs) {
        this.clubs = clubs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_list_item,parent,false);
        return new ClubProfileAdapter.ViewHolder(view);
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
        private ConstraintLayout rlClubItemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivClubImage = itemView.findViewById(R.id.ivClubImage);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvClubDescription = itemView.findViewById(R.id.tvClubDescription);
            rlClubItemContainer = itemView.findViewById(R.id.rlClubItemContainer);
        }

        public void bind(ParseObject clubObject) {
            tvClubName.setText(clubObject.getString("clubName"));
            tvClubDescription.setText(clubObject.getString("clubDescription"));

            int radius = 30;
            int margin = 10;

            ParseFile image = clubObject.getParseFile("clubImage");
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
                    i.putExtra("ClubObject", Parcels.wrap(clubObject));
                    context.startActivity(i);
                }
            });
        }
    }
}
