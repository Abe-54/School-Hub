package com.bcappdevelopers.schoolhub;

import android.widget.ImageButton;
import com.parse.ParseObject;

public interface RecyclerViewActionListener {
    void onViewClicked(int clickedViewId, int clickedItemPosition, ParseObject parseObject, ImageButton btnLike, ImageButton btnDislike);
    void setupAnnouncements(ParseObject parseObject, ImageButton likeBtn, ImageButton dislikeBtn);
}
