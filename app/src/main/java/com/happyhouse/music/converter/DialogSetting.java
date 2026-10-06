package com.happyhouse.music.converter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class DialogSetting extends AlertDialog{
    public static final String TAG = DialogSetting.class.getName();
    private Button buttonPathSelect;
    private Button buttonPathSave;
    private AlertDialogCallback alertDialogCallback;
    private String path;
    private TextView textOutFolderPath;

    public DialogSetting(Context context) {
        super(context);
    }

    public void setAlertDialogCallback(AlertDialogCallback alertDialogCallback){
        this.alertDialogCallback = alertDialogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 1.0f;
        lpWindow.dimAmount = 0.8f;

//        getWindow().setAttributes(lpWindow);
//        getWindow().setLayout((int)(displayX * 0.9), (int)(displayY * 0.5));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.MyAnimation_Window);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.layout_alert_setting);
        setLayout();
    }

    private void setLayout(){
        buttonPathSelect = findViewById(R.id.button_path_select);
        buttonPathSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCallback.editPath();
            }
        });
        buttonPathSave = findViewById(R.id.button_path_save);
        buttonPathSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCallback.savePath(textOutFolderPath.getText().toString());
                cancel();
            }
        });
        textOutFolderPath = findViewById(R.id.text_out_folder_path);
        textOutFolderPath.setMovementMethod(new ScrollingMovementMethod());
        textOutFolderPath.setText(path);
    }

    public void setText(String path) {
        this.path = path;
        if (textOutFolderPath != null) {
            textOutFolderPath.setText(path);
        }
    }

    public String getText() {
        return this.path;
    }

    public interface AlertDialogCallback{
        void editPath();
        void savePath(String path);
    }

}
