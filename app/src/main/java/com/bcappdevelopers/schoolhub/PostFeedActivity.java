package com.bcappdevelopers.schoolhub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bcappdevelopers.schoolhub.models.Club;
import com.bcappdevelopers.schoolhub.models.Comment;
import com.bcappdevelopers.schoolhub.student.adapters.ClubListAdapter;
import com.bcappdevelopers.schoolhub.student.adapters.CommentListAdapter;
import com.parse.*;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostFeedActivity extends AppCompatActivity {

    private static final String TAG = "POST FEED ACTIVITY";

    private TextView tvPost;
    private EditText etComment;
    private ImageButton btnPostComment;
    private RecyclerView rvPostComments;

    private ParseObject announcementPost;

    private CommentListAdapter adapter;
    private List<Comment> allComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_feed_screen);

        tvPost = findViewById(R.id.tvPost);
        etComment = findViewById(R.id.etComment);
        btnPostComment = findViewById(R.id.btnPostComment);
        rvPostComments = findViewById(R.id.rvPostComments);

        if(getIntent() != null) {
            announcementPost = Parcels.unwrap(getIntent().getParcelableExtra("Announcement"));
        }

        allComments = new ArrayList<>();
        adapter = new CommentListAdapter(PostFeedActivity.this, allComments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvPostComments.setAdapter(adapter);
        rvPostComments.setLayoutManager(new LinearLayoutManager(PostFeedActivity.this));
        queryComments();

        tvPost.setText(announcementPost.getString("eventDescription"));

        if(etComment.getText().toString().length() != 0) {
            btnPostComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postComment(etComment.getText().toString());
                    etComment.getText().clear();
                    allComments.clear();
                    queryComments();
                }
            });
        }
    }

    private void postComment(String commentContent) {
        Comment comment = new Comment();

        comment.setCommentCreator(ParseUser.getCurrentUser());
        comment.setCommentContents(commentContent);
        comment.setPost(announcementPost);

        comment.saveInBackground();
    }

    private void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting club announcements", e);
                    return;
                }
                for (Comment comment : comments) {

                    if(comment.getPost().getObjectId().compareTo(announcementPost.getObjectId()) == 0) {
                        allComments.add(comment);
                        Log.i(TAG, "Comment made by: " + comment.getCommentCreator());
                        Log.i(TAG, "Comment: " + comment.getCommentContents() + "\n");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
