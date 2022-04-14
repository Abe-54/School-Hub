package com.bcappdevelopers.schoolhub.admin.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bcappdevelopers.schoolhub.R;

public class ComposeAlertFragment extends DialogFragment {


    public static class EditNameDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

        private EditText mEditText;

        public EditNameDialogFragment() {
            // Empty constructor is required for DialogFragment
            // Make sure not to add arguments to the constructor
            // Use `newInstance` instead as shown below
        }

        public interface EditNameDialogListener {
            void onFinishEditDialog(String inputText);
        }

        public static EditNameDialogFragment newInstance(String title) {
            EditNameDialogFragment frag = new EditNameDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //  return inflater.inflate(R.layout.fragment_edit_name, container);
            return null;
        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // Get field from view
           // mEditText = (EditText) view.findViewById(R.id.txt_your_name);
            // Fetch arguments from bundle and set title
            String title = getArguments().getString("title", "Enter Name");
            getDialog().setTitle(title);
            // Show soft keyboard automatically and request focus to field
            mEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // Fires whenever the textfield has an action performed
        // In this case, when the "Done" button is pressed
        // REQUIRES a 'soft keyboard' (virtual keyboard)
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                // Return input text back to activity through the implemented listener
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(mEditText.getText().toString());
                // Close the dialog and return back to the parent activity
                dismiss();
                return true;
            }
            return false;
        }
    }
}