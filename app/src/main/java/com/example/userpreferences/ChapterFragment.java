package com.example.userpreferences;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChapterFragment extends Fragment {
    private TextView tvChapter;
    private String dataChapter;

    public void setDataChapter(String dataChapter) {
        this.dataChapter = dataChapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        tvChapter = view.findViewById(R.id.tv_chapter);
        tvChapter.setText(dataChapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvChapter.setText(dataChapter);
    }

    public void updateChapter(){
        tvChapter.setText(dataChapter);
    }
}