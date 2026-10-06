package com.happyhouse.music.converter.help;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.happyhouse.music.converter.DialogSetting;
import com.happyhouse.music.converter.R;

import java.util.ArrayList;


public class DialogHelp extends AlertDialog implements View.OnClickListener {
    public static final String TAG = DialogSetting.class.getName();
    private Button buttonLicense;
    private Button buttonClose;
    private RecyclerView listMain;
    private boolean bLicense = false;

    public DialogHelp(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;

        Display dis = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        dis.getMetrics(metrics);
        getWindow().setLayout((int)(metrics.widthPixels * 0.9), (int)(metrics.heightPixels * 0.8));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.MyAnimation_Window);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.layout_alert_help);
        setLayout();
    }

    private void setLayout(){
        buttonLicense = findViewById(R.id.button_license);
        buttonClose = findViewById(R.id.button_close);
        listMain = findViewById(R.id.list_main);
        buttonLicense.setOnClickListener(this);
        buttonClose.setOnClickListener(this);
        writeHowTo();
    }

    private void writeHowTo(){
        ArrayList<HowToItem> howToItems = new ArrayList<>();
        howToItems.add(new HowToItem(R.string.how_to_title1, R.drawable.help_image_section1, R.string.how_to_content1));
        howToItems.add(new HowToItem(R.string.how_to_title2, R.drawable.help_image_section2, R.string.how_to_content2));
        HowToAdapter howToAdapter = new HowToAdapter(howToItems);
        listMain.setAdapter(howToAdapter);
        listMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void writeLicense(){
        ArrayList<LicenseItem> licenseItems = new ArrayList<>();
        licenseItems.add(new LicenseItem(
                R.string.license1_title,
                R.string.license1_copyright,
                R.string.license1_link,
                R.string.license1_content
                ));
        licenseItems.add(new LicenseItem(
                R.string.license2_title,
                R.string.license2_copyright,
                R.string.license2_link,
                R.string.license2_content
        ));
        LicenseListAdapter licenseAdapter = new LicenseListAdapter(licenseItems);
        listMain.setAdapter(licenseAdapter);
        listMain.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_license:
                if(bLicense){
                    writeHowTo();
                    bLicense = false;
                    buttonLicense.setBackgroundResource(R.drawable.ic_img_license);
                }else{
                    writeLicense();
                    bLicense = true;
                    buttonLicense.setBackgroundResource(R.drawable.ic_img_information);
                }
                break;
            case R.id.button_close:
                cancel();
                break;
        }
    }
}
