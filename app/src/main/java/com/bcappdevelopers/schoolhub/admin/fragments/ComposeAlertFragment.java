package com.bcappdevelopers.schoolhub.admin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.bcappdevelopers.schoolhub.R;
import com.bcappdevelopers.schoolhub.admin.CreateEventActivity;
import com.bcappdevelopers.schoolhub.admin.CreatePostActivity;
import com.bcappdevelopers.schoolhub.admin.SubscriberProfileActivity;
import org.parceler.Parcels;

public class ComposeAlertFragment extends DialogFragment {

    public ComposeAlertFragment() {
    }

    public static ComposeAlertFragment newInstance(String title) {
        ComposeAlertFragment frag = new ComposeAlertFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Create Post",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getContext(), CreatePostActivity.class);
                getContext().startActivity(i);
            }
        });

        alertDialogBuilder.setNeutralButton("Create An Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getContext(), CreateEventActivity.class);
                getContext().startActivity(i);
            }
        });

        return alertDialogBuilder.create();
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}