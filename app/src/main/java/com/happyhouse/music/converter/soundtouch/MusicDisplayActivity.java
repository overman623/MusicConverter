package com.happyhouse.music.converter.soundtouch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.happyhouse.music.converter.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MusicDisplayActivity extends AppCompatActivity {

    public static final String EXTRA_SOURCE_PATH = "SOURCE_NAME";

    private final ArrayList<BgmStorageListItem> items = new ArrayList<BgmStorageListItem>();
    private BgmListAdapter adapter;

    private ListView listView;
    private Button selectTrack;
    private TextView textDirection;
    private TextView textEmpty;

    private File storageRoot;
    private File currentDirectory;
    private File selectedFile;
    private int selectedPosition = -1;

    private static final FilenameFilter AUDIO_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            String lower = name.toLowerCase(Locale.US);
            return lower.endsWith(".mp3") || lower.endsWith(".wav");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_display);

        storageRoot = Environment.getExternalStorageDirectory();
        listView = findViewById(R.id.list_music);
        selectTrack = findViewById(R.id.btn_bgm_select);
        textDirection = findViewById(R.id.text_direction);
        textEmpty = findViewById(R.id.text_empty);
        Button backButton = findViewById(R.id.btn_back);

        adapter = new BgmListAdapter();
        listView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUp();
            }
        });
        selectTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile == null) {
                    Toast.makeText(MusicDisplayActivity.this, R.string.select_track_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent();
                data.putExtra(EXTRA_SOURCE_PATH, selectedFile.getAbsolutePath());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        setSelectEnabled(false);

        File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        String start = music != null && music.isDirectory()
                ? music.getAbsolutePath()
                : storageRoot.getAbsolutePath();
        updateSongList(start);
    }

    @Override
    public void onBackPressed() {
        if (currentDirectory != null && !isStorageRoot(currentDirectory)) {
            goUp();
            return;
        }
        super.onBackPressed();
    }

    private void goUp() {
        if (currentDirectory == null || isStorageRoot(currentDirectory)) {
            finish();
            return;
        }
        File parent = currentDirectory.getParentFile();
        if (parent == null) {
            finish();
            return;
        }
        updateSongList(parent.getAbsolutePath());
    }

    private boolean isStorageRoot(File directory) {
        return storageRoot != null && storageRoot.getAbsolutePath().equals(directory.getAbsolutePath());
    }

    private void updateSongList(String filePath) {
        items.clear();
        selectedFile = null;
        selectedPosition = -1;
        setSelectEnabled(false);

        File directory = new File(filePath);
        currentDirectory = directory;
        textDirection.setText(getString(R.string.current_folder, directory.getAbsolutePath()));

        File[] folders = directory.listFiles();
        if (folders == null) {
            adapter.notifyDataSetChanged();
            textEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.folder_unreadable, Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<File> directories = new ArrayList<File>();
        for (File child : folders) {
            if (child.isDirectory() && !child.getName().startsWith(".")) {
                directories.add(child);
            }
        }
        File[] audio = directory.listFiles(AUDIO_FILTER);
        ArrayList<File> tracks = new ArrayList<File>();
        if (audio != null) {
            Collections.addAll(tracks, audio);
        }
        Comparator<File> byName = new Comparator<File>() {
            @Override
            public int compare(File left, File right) {
                return left.getName().compareToIgnoreCase(right.getName());
            }
        };
        Collections.sort(directories, byName);
        Collections.sort(tracks, byName);

        for (File child : directories) {
            items.add(new BgmStorageListItem(child, false));
        }
        for (File child : tracks) {
            items.add(new BgmStorageListItem(child, true));
        }
        adapter.notifyDataSetChanged();
        textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setSelectEnabled(boolean enabled) {
        selectTrack.setEnabled(enabled);
        selectTrack.setAlpha(enabled ? 1f : 0.45f);
    }

    private class BgmListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(MusicDisplayActivity.this)
                        .inflate(R.layout.layout_list_storage_item_bgm, parent, false);
                holder = new ViewHolder();
                holder.title = convertView.findViewById(R.id.bgm_title);
                holder.imageIcon = convertView.findViewById(R.id.icon_image);
                holder.button = convertView.findViewById(R.id.bgm_button);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final BgmStorageListItem item = items.get(position);
            final int itemPosition = position;
            boolean selected = item.isFile() && position == selectedPosition;
            convertView.setBackgroundColor(ContextCompat.getColor(
                    MusicDisplayActivity.this,
                    selected ? R.color.row_selected : R.color.white));
            holder.title.setText(item.getFile().getName());
            if (item.isFile()) {
                holder.imageIcon.setImageResource(R.drawable.ic_mp3);
            } else {
                holder.imageIcon.setImageResource(R.drawable.ic_img_directory);
            }
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isFile()) {
                        selectedPosition = itemPosition;
                        selectedFile = item.getFile();
                        setSelectEnabled(true);
                        notifyDataSetChanged();
                    } else {
                        updateSongList(item.getFile().getAbsolutePath());
                    }
                }
            });
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView title;
        Button button;
        ImageView imageIcon;
    }
}
