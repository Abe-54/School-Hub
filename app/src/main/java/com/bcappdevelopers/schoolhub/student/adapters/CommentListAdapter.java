package com.bcappdevelopers.schoolhub.student.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.models.Comment;
import com.bumptech.glide.Glide;
import com.parse.*;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

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
        private TextView tvCommentProfileName;
        private TextView tvCommentLikeCounter;
        private TextView tvCommentDislikeCounter;
        private ImageButton btnLikeComment;
        private ImageButton btnDislikeComment;

        boolean isLiked = false;
        int likedIcon;

        boolean isDisliked = false;
        int dislikedICon;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);

            ivUserComment = itemView.findViewById(R.id.ivUserComment);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCommentProfileName = itemView.findViewById(R.id.tvCommentProfileName);
            tvCommentLikeCounter = itemView.findViewById(R.id.tvCommentLikeCounter);
            tvCommentDislikeCounter = itemView.findViewById(R.id.tvCommentDislikeCounter);
            btnLikeComment = itemView.findViewById(R.id.btnLikeComment);
            btnDislikeComment = itemView.findViewById(R.id.btnDislikeComment);

            likedIcon = R.drawable.outline_thumb_up_24;
            dislikedICon = R.drawable.outline_thumb_down_24;
        }

        public void bind(Comment comment) {

            tvComment.setText(comment.getCommentContents());

            int totalLiked = (int) comment.getNumber("likeCounter");
            int totalDisliked = (int) comment.getNumber("dislikeCounter");

            tvCommentLikeCounter.setText(totalLiked + "");
            tvCommentDislikeCounter.setText(totalDisliked + "");

            ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");

            userQuery.getInBackground(comment.getCommentCreator().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting profile data", e);
                        return;
                    }

                    tvCommentProfileName.setText(user.getUsername());

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

            userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issues getting user data", e);
                        return;
                    }

                    ParseQuery<ParseObject> likedCommentsQuery = user.getRelation("likedComments").getQuery();

                    likedCommentsQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> comments, ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issues getting announcements", e);
                                return;
                            }

                            for (ParseObject userFoundComment : comments) {
                                if(userFoundComment.getObjectId().compareTo(comment.getObjectId()) == 0) {
                                    Log.i(TAG, "found liked comment: " + userFoundComment.getString("commentContents"));
                                    likedIcon = R.drawable.filled_thumb_up_24;
                                    isLiked = true;
                                }
                            }

                            btnLikeComment.setImageDrawable(
                                    ContextCompat.getDrawable(getApplicationContext(), likedIcon));
                        }
                    });

                    ParseQuery<ParseObject> dislikedCommentsQuery = user.getRelation("dislikedComments").getQuery();

                    dislikedCommentsQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> comments, ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issues getting announcements", e);
                                return;
                            }

                            for (ParseObject userFoundComment : comments) {
                                if(userFoundComment.getObjectId().compareTo(comment.getObjectId()) == 0) {
                                    Log.i(TAG, "found disliked announcement: " + userFoundComment.getString("commentContents"));
                                    dislikedICon = R.drawable.filled_thumb_down_24;
                                    isDisliked = true;
                                }
                            }

                            btnDislikeComment.setImageDrawable(
                                    ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
                        }
                    });
                }
            });

            btnLikeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isLiked){
                        isLiked = false;
                        likedIcon = R.drawable.outline_thumb_up_24;
                        if(totalLiked > 0) {
                            comment.put("likeCounter", totalLiked - 1);
                        }

                        ParseUser.getCurrentUser().getRelation("likedComments").remove(comment);

                    } else {
                        isLiked = true;
                        likedIcon = R.drawable.filled_thumb_up_24;

                        comment.put("likeCounter", totalLiked + 1);

                        if(totalDisliked > 0 &&  isDisliked == true) {
                            comment.put("dislikeCounter", totalDisliked - 1);
                            dislikedICon = R.drawable.outline_thumb_down_24;
                            isDisliked = false;
                            ParseUser.getCurrentUser().getRelation("likedComments").remove(comment);
                            ParseUser.getCurrentUser().getRelation("dislikedComments").remove(comment);
                        }

                        ParseUser.getCurrentUser().getRelation("likedComments").add(comment);

                    }

                    ParseUser.getCurrentUser().saveInBackground();
                    comment.saveInBackground();
                    notifyDataSetChanged();
                }
            });

            btnDislikeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isDisliked){
                        isDisliked = false;
                        dislikedICon = R.drawable.outline_thumb_down_24;
                        if(totalDisliked > 0) {
                            comment.put("dislikeCounter", totalDisliked - 1);
                        }
                        ParseUser.getCurrentUser().getRelation("dislikedComments").remove(comment);
                    } else {
                        isDisliked = true;
                        dislikedICon = R.drawable.filled_thumb_down_24;

                        comment.put("dislikeCounter", totalDisliked + 1);

                        if(totalLiked > 0 &&  isLiked == true) {
                            comment.put("likeCounter", totalLiked - 1);
                            likedIcon = R.drawable.outline_thumb_up_24;
                            isLiked = false;
                            ParseUser.getCurrentUser().getRelation("likedComments").remove(comment);
                            ParseUser.getCurrentUser().getRelation("dislikedComments").remove(comment);
                        }

                        ParseUser.getCurrentUser().getRelation("dislikedComments").add(comment);
                    }

                    ParseUser.getCurrentUser().saveInBackground();
                    comment.saveInBackground();
                    notifyDataSetChanged();
                }
            });

            btnLikeComment.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), likedIcon));
            btnDislikeComment.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), dislikedICon));
        }
    }
}
