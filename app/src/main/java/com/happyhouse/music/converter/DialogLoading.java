package com.happyhouse.music.converter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DialogLoading extends Dialog {

    private Context context;
    private String title;

    public DialogLoading(Context context, String title) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        this.context = context;
        this.title = title;
    }

    public DialogLoading(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogLoading(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alert_loading);
        ImageView imageLoading = findViewById(R.id.image_loading);
        Glide.with(context).load(R.drawable.ic_img_loading).into(imageLoading);
        TextView textLoading = findViewById(R.id.text_loading);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        imageLoading.setAnimation(anim);
        textLoading.setText(title);
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }


}
