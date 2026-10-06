package com.happyhouse.music.converter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.happyhouse.music.converter.help.DialogHelp;
import com.happyhouse.music.converter.soundtouch.MusicDisplayActivity;
import com.happyhouse.music.converter.soundtouch.SoundTouch;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_FOLDER = 1;
    private static final int REQUEST_MUSIC = 2;
    private static final int REQUEST_STORAGE = 10;
    private static final String[] PERMISSIONS = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String PACKAGE_NAME_PATH;

    private ImageView imageMusicSelect = null;
    private TextView textMusicInfo = null;
    private TextView textInfoTitle = null;
    private TextView textTempo = null;
    private TextView textPitch = null;
    private Button buttonMusicInit = null;
    private Button buttonConvert = null;
    private Button buttonControlInit = null;
    private Button buttonSetting = null;
    private Button buttonHelp;
    private ConstraintLayout layoutBottom = null;
    private View layoutLoading = null;
    private SeekBar seekBarTempo = null;
    private SeekBar seekBarPitch = null;
    private DialogSetting dialogSetting;

    public SharedPreferences setting = null;
    public SharedPreferences.Editor setter = null;

    private String outPath;
    private String convertPath;
    private String fileName;
    private String lastOutputPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    /// Called when the activity is created
        Log.d(TAG, "======================= onCreate =======================");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //가로화면 고정
        setContentView(R.layout.activity_main);
        PACKAGE_NAME_PATH = getPackageName();

        imageMusicSelect = findViewById(R.id.image_music_select);
        layoutBottom = findViewById(R.id.layout_bottom);
        layoutLoading = findViewById(R.id.layout_loading);
        textMusicInfo = findViewById(R.id.text_music_info);
        textInfoTitle = findViewById(R.id.text_info_title);
        textTempo = findViewById(R.id.text_tempo);
        textPitch = findViewById(R.id.text_pitch);
        buttonMusicInit = findViewById(R.id.button_music_init);
        buttonConvert = findViewById(R.id.button_convert);
        buttonControlInit = findViewById(R.id.button_control_init);
        buttonSetting = findViewById(R.id.button_setting);
        buttonHelp = findViewById(R.id.button_help);
        imageMusicSelect.setBackgroundResource(R.drawable.ic_img_add_music);
        seekBarTempo = findViewById(R.id.seek_bar_tempo);
        seekBarTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textTempo.setText("Tempo : ".concat(String.valueOf(progress)).concat("%"));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarTempo.setProgress(100);
        seekBarPitch = findViewById(R.id.seek_bar_pitch);
        seekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String num = String.format("%.3f", 0.001 * (progress - 5000));
                textPitch.setText("Pitch : ".concat(num));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekBarPitch.setProgress(5000 - 318);
        imageMusicSelect.setOnClickListener(this);
        buttonConvert.setOnClickListener(this);
        buttonControlInit.setOnClickListener(this);
        buttonMusicInit.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
        buttonHelp.setOnClickListener(this);
        textInfoTitle.setText(R.string.sub_title);
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        permissionsChecker.lacksPermissions(PERMISSIONS);
        setting = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        outPath = setting.getString("FILE_PATH", Environment.getExternalStorageDirectory() + File.separator + "Converter" + File.separator);
        setter = setting.edit();
        new MusicFileManager(getFilesDir().getParent() + File.separator + "cache_file");
        dialogSetting = new DialogSetting(this);
        //처음 실행했을때만 ffmpeg 를 카피해야함.
    }

    /// Button click handler
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.button_convert:
                process();
                break;

            case R.id.button_control_init:
                seekBarTempo.setProgress(100);
                seekBarPitch.setProgress(5000 - 318);
                break;
            case R.id.button_help:
                DialogHelp dialogHelp = new DialogHelp(this);
                dialogHelp.show();
                break;
            case R.id.button_setting:
                Log.d(TAG, "PATH : " + outPath);
                dialogSetting.setAlertDialogCallback(new DialogSetting.AlertDialogCallback() {
                    @Override
                    public void editPath() {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(intent, REQUEST_FOLDER);
                    }

                    @Override
                    public void savePath(String path) {
                        outPath = path;
                        setter.putString("FILE_PATH", path).apply(); //파일 네이밍 초기화
                    }
                });
                dialogSetting.show();
                dialogSetting.setText(outPath);
                break;
            case R.id.button_music_init:
                layoutBottom.setVisibility(View.GONE);
                imageMusicSelect.setBackgroundResource(R.drawable.ic_img_add_music);
                buttonMusicInit.setVisibility(View.GONE);
                Glide.with(this).clear(imageMusicSelect);
                textMusicInfo.setText("");
                textInfoTitle.setText(R.string.sub_title);
                imageMusicSelect.setClickable(true);
                break;
            case R.id.image_music_select:
                openMusicBrowser();
                break;
        }
    }

    /// Play audio file //컨버팅이 끝나면 플레이 할건지 물어보는 메소드.
    //output경로를 지정해야함.
    protected void playWavFile(String fileName) {
        File file2play = new File(fileName);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        Uri musicURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file2play);
        i.setDataAndType(musicURI, "audio/wav");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(i);
    }

    class ProcessTask extends AsyncTask<ProcessTask.Parameters, Integer, Long> {
        /// Helper class to store the SoundTouch file processing parameters

        public final class Parameters {
            String inFileName;
            String outFileName;
            float tempo;
            float pitch;
        }

        @Override
        protected void onPreExecute() {
            layoutLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Long result) {
            layoutLoading.setVisibility(View.GONE);
            if (result != null && result == 0L) {
                Toast.makeText(MainActivity.this, getString(R.string.convert_saved, lastOutputPath), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.convert_failed, Toast.LENGTH_SHORT).show();
            }
        }

        /// Function that does the SoundTouch processing
        public final long doSoundTouchProcessing(Parameters params) {
            SoundTouch st = new SoundTouch();
            st.setTempo(params.tempo);
            Log.i("SoundTouch", "process file tempo = " + params.tempo);
            st.setPitchSemiTones(params.pitch);
            Log.i("SoundTouch", "process input file " + params.inFileName);
            Log.i("SoundTouch", "process output file " + params.outFileName);
            String input = params.inFileName;
            if (input == null) {
                return -1L;
            }
            if (input.toLowerCase(java.util.Locale.US).endsWith(".mp3")) {
                String wav = MusicFileManager.convertMP3toWAV(input);
                if (wav == null || "ERROR".equals(wav)) {
                    return -1L;
                }
                input = wav;
            }
            long startTime = System.currentTimeMillis();
            int res = st.processFile(input, params.outFileName);
            long endTime = System.currentTimeMillis();
            float duration = (endTime - startTime) * 0.001f;

            Log.i("SoundTouch", "process file done, duration = " + duration);
//        appendToConsole("Processing done, duration " + duration + " sec.");
            if (res != 0) {
                String err = SoundTouch.getErrorString();
//            appendToConsole("Failure: " + err);
                return -1L;
            }

//        Log.d(TAG, "outFileName : " + params.outFileName);
            // Play file if so is desirable
//                playWavFile(params.outFileName); //이곳을 autiotrack로 수정해서 재생함.
            return 0L;
        }

        /// Overloaded function that get called by the system to perform the background processing
        @Override
        protected Long doInBackground(Parameters... aparams) {
            try {
                return doSoundTouchProcessing(aparams[0]);
            } catch (Exception exception) {
                Log.e(TAG, "convert failed", exception);
                return -1L;
            }
        }

    }
//    Tempo & Playback Rate adjustable in range -95% .. +5000%. 100이 표준값
//    0의 의미는 그대로 라는뜻이다.
//      73.9                      -> BPM
//  -95%   0% 100% 200% 300% 400%
//    5% 100% 200% 300% 400% 500% -> 실제 표현해야할 Progress..
//
//    73.9 -> 0%
//    The sound Pitch (key) adjustable in range -60 .. +60 semitones (+- 5 octaves). -0.318이 정상수치
//  설정파일 불러오기.
//  원래 경로는 없으면 만들기라도 해야함. Converter

    protected void process() {
        if (convertPath == null || fileName == null) {
            Toast.makeText(this, R.string.select_file_first, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (!outPath.endsWith(File.separator)) {
                outPath = outPath + File.separator;
            }
            File file = new File(outPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            ProcessTask task = new ProcessTask();
            ProcessTask.Parameters params = task.new Parameters();
            params.inFileName = convertPath;
            String baseName = fileName;
            int dot = baseName.lastIndexOf('.');
            if (dot > 0) {
                baseName = baseName.substring(0, dot);
            }
            params.outFileName = outPath + baseName + "_converted.wav";
            lastOutputPath = params.outFileName;
            float seekTempoProgress = seekBarTempo.getProgress();
            params.tempo = seekTempoProgress * 0.01f;
            float seekPitchProgress = seekBarPitch.getProgress();
            params.pitch = (float) (0.001 * (seekPitchProgress - 5000));
            task.execute(params);
        } catch (Exception exp) {
            layoutLoading.setVisibility(View.GONE);
            exp.printStackTrace();
            Toast.makeText(this, R.string.convert_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void openMusicBrowser() {
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_STORAGE);
            return;
        }
        startActivityForResult(new Intent(this, MusicDisplayActivity.class), REQUEST_MUSIC);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_STORAGE) {
            return;
        }
        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show();
            return;
        }
        openMusicBrowser();
    }

    private void applySelectedTrack(String loadPath) {
        if (loadPath == null) {
            return;
        }
        String lower = loadPath.toLowerCase(java.util.Locale.US);
        if (!lower.endsWith(".wav") && !lower.endsWith(".mp3")) {
            Toast.makeText(this, R.string.unsupported_file, Toast.LENGTH_SHORT).show();
            return;
        }
        fileName = new File(loadPath).getName();
        convertPath = loadPath;
        textMusicInfo.setText(fileName);
        buttonMusicInit.setVisibility(View.VISIBLE);
        layoutBottom.setVisibility(View.VISIBLE);
        textInfoTitle.setText(R.string.sub_title_change);
        seekBarTempo.setProgress(100);
        seekBarPitch.setProgress(5000 - 318);
        imageMusicSelect.setClickable(false);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(loadPath);
            byte[] albumImage = mmr.getEmbeddedPicture();
            if (albumImage == null) {
                Glide.with(this).clear(imageMusicSelect);
                imageMusicSelect.setBackgroundResource(R.drawable.ic_img_not_found);
            } else {
                imageMusicSelect.setBackgroundResource(android.R.color.transparent);
                Glide.with(this).load(albumImage)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                        .into(imageMusicSelect);
            }
        } catch (Exception exception) {
            Glide.with(this).clear(imageMusicSelect);
            imageMusicSelect.setBackgroundResource(R.drawable.ic_img_not_found);
        } finally {
            mmr.release();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_MUSIC) {
                applySelectedTrack(data.getStringExtra(MusicDisplayActivity.EXTRA_SOURCE_PATH));
            } else if (requestCode == REQUEST_FOLDER) {
                String path = MusicPathChanger.getSDfolderPath(this, data.getData());
                if (path.equals("")) {
                    Toast.makeText(this, R.string.directory_missing, Toast.LENGTH_SHORT).show();
                } else {
                    dialogSetting.setText(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
