package com.example.userpreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DirFragment.OnChangeChapterListener, BookmarkDialogFragment.OnCreateBookmarkListener {
    final String PREF_NAME = "UserPreferences";
    final String CHAPTER = "CHAPTER";
    final DirFragment dirFragment = new DirFragment();
    final ChapterFragment chapterFragment = new ChapterFragment();
    private MaterialToolbar toolbar;
    private AssetFileReader fileReader;
    private SharedPreferences sharedPreferences;

    private String currentChapter;
    private String currentBookmark;

    final MenuItem.OnMenuItemClickListener menuMarkItemListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.add_bookmark) {
                BookmarkDialogFragment bookmarkDialog = new BookmarkDialogFragment();
                bookmarkDialog.show(getSupportFragmentManager(), "BookmarkDialogFragment");
                return true;
            }
            if (item.getItemId() == R.id.delete_bookmark) {
                if(!currentBookmark.isEmpty())
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(currentBookmark);
                    editor.apply();
                }
                updateBookmarksList();
                toolbar.getMenu().getItem(0).getSubMenu().getItem(1).setEnabled(false);
                return true;
            }
            String filename = sharedPreferences.getString("Bookmark_" + item.getTitle().toString(), "");
            if (!filename.isEmpty()){
                currentBookmark = "Bookmark_" + item.getTitle().toString();
                onChangeChapter(filename);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileReader = new AssetFileReader(getApplicationContext());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_fragment, dirFragment)
                .commit();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        toolbar = findViewById(R.id.top_app_bar);
        toolbar.getMenu().getItem(0).getSubMenu().getItem(0).setOnMenuItemClickListener(menuMarkItemListener);
        toolbar.getMenu().getItem(0).getSubMenu().getItem(1).setOnMenuItemClickListener(menuMarkItemListener);

        updateBookmarksList();
    }

    @Override
    public void onChangeChapter(String filename) {
        filename = filename.replace(" ", "");
        currentChapter = filename;

        HashMap<String, String> map = (HashMap<String, String>) sharedPreferences.getAll();
        if(map.containsValue(currentChapter)){
            for (String key : map.keySet())
            {
                if (map.get(key).equals(currentChapter) && key.contains("Bookmark"))  {
                    currentBookmark = key;
                    break;
                }
            }
        }
        updateFragmentChapter();
    }

    @Override
    public void onResumeDirFragment() {
        toolbar.getMenu().getItem(0).getSubMenu().getItem(0).setEnabled(false);
        toolbar.getMenu().getItem(0).getSubMenu().getItem(1).setEnabled(false);
        currentBookmark = "";
    }

    @Override
    public void onPauseDirFragment() {
        toolbar.getMenu().getItem(0).getSubMenu().getItem(0).setEnabled(true);
        if(currentBookmark != null && !currentBookmark.isEmpty()){
            toolbar.getMenu().getItem(0).getSubMenu().getItem(1).setEnabled(true);
        }
    }

    private void updateBookmarksList() {
        toolbar.getMenu().getItem(0).getSubMenu().removeGroup(R.id.group_bookmarks);

        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            if (entry.getKey().contains("Bookmark_")) {
                toolbar.getMenu().getItem(0).getSubMenu()
                        .add(R.id.group_bookmarks, 0, 0, entry.getKey().substring(9))
                        .setOnMenuItemClickListener(menuMarkItemListener);
            }
        }
    }

    private void updateFragmentChapter() {
        chapterFragment.setDataChapter(fileReader.readFile(currentChapter));
        if (chapterFragment.isVisible()) {
            chapterFragment.updateChapter();
            if(!currentBookmark.isEmpty()){
                toolbar.getMenu().getItem(0).getSubMenu().getItem(1).setEnabled(true);
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_fragment, chapterFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHAPTER, currentChapter);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentChapter = sharedPreferences.getString(CHAPTER, "");
        if (!currentChapter.isEmpty()) {
            updateFragmentChapter();
        }
    }

    @Override
    public void onSendBookmarkName(String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(data, currentChapter);
        editor.apply();
        updateBookmarksList();
    }
}