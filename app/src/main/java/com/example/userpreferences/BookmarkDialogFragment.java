package com.example.userpreferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class BookmarkDialogFragment extends DialogFragment {
    private OnCreateBookmarkListener bookmarkListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.input_name)
                .setView(R.layout.fragment_bookmark_dialog)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    EditText tvName = getDialog().findViewById(R.id.et_bookmark_name);
                    if (!tvName.getText().toString().isEmpty()) {
                        bookmarkListener.onSendBookmarkName("Bookmark_" + tvName.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dismiss())
                .create();
    }

    interface OnCreateBookmarkListener {
        void onSendBookmarkName(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bookmarkListener = (OnCreateBookmarkListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement an interface OnCreateBookmarkListener");
        }
    }
}
