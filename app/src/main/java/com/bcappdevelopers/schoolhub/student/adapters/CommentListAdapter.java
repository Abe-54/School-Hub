package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Comment;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentListAdapter extends  RecyclerView.Adapter<CommentListAdapter.ViewHolder>  {

    private static final String TAG = "CommentListAdapter";
    private final List<Comment> comments;
    private Context context;

    public CommentListAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);

        return new CommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivUserComment;
        private TextView tvComment;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);

            ivUserComment = itemView.findViewById(R.id.ivUserComment);
            tvComment = itemView.findViewById(R.id.tvComment);
        }

        public void bind(Comment comment) {

            tvComment.setText(comment.getCommentContents());

            ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");

            userQuery.getInBackground(comment.getCommentCreator().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting profile data", e);
                        return;
                    }
                    ParseFile image = user.getParseFile("profilePicture");
                    if(image != null) {
                        Glide.with(context)
                                .load(image.getUrl())
                                .centerCrop()
                                .transform(new CropCircleWithBorderTransformation())
                                .into(ivUserComment);
                    }
                }
            });
        }
    }
}
