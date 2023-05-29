package com.example.userpreferences;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DirFragment extends Fragment {
    private OnChangeChapterListener fragmentSendDataListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dir, container, false);
        ListView listView = view.findViewById(R.id.list_chapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            String selectedItem = (String)parent.getItemAtPosition(position);
            fragmentSendDataListener.onChangeChapter(selectedItem);
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnChangeChapterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement an interface OnChangeChapterListener");
        }
    }

    interface OnChangeChapterListener {
        void onChangeChapter(String data);
        void onResumeDirFragment();
        void onPauseDirFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentSendDataListener.onResumeDirFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentSendDataListener.onPauseDirFragment();
    }
}