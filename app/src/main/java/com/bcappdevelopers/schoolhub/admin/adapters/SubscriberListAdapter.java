package com.bcappdevelopers.schoolhub.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.ClubProfileActivity;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.admin.SubscriberProfileActivity;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.student.adapters.ClubListAdapter;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class SubscriberListAdapter extends RecyclerView.Adapter<SubscriberListAdapter.ViewHolder>{

    private static final String TAG = "SubListAdapter";
    private final List<ParseObject> subs;
    private Context context;

    public SubscriberListAdapter(Context context, List<ParseObject> subs) {
        this.context = context;
        this.subs = subs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subscriber_list_item,parent,false);
        return new SubscriberListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ParseObject subscriber = subs.get(position);
        holder.bind(subscriber);
    }

    @Override
    public int getItemCount() {
        return subs.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        subs.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ParseObject> list) {
        subs.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSubImage;
        private TextView tvSubName;
        private Button btnManageMember;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivSubImage = itemView.findViewById(R.id.ivSubImage);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            btnManageMember = itemView.findViewById(R.id.btnManageMember);
        }

        public void bind(ParseObject subscriberObject) {
            tvSubName.setText(subscriberObject.getString("username"));

            ParseFile image = subscriberObject.getParseFile("profilePicture");
            if(image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .transform(new CropCircleWithBorderTransformation())
                        .into(ivSubImage);
            }

            btnManageMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, SubscriberProfileActivity.class);
                    i.putExtra("Subscriber", Parcels.wrap(subscriberObject));
                    context.startActivity(i);
                }
            });
        }
    }
}
