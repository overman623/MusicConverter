package com.happyhouse.music.converter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class CustomSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    private Rect rect;
    private Paint paint;
    private int seekbarHeight;
    private int max = 10000;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new Rect();
        paint = new Paint();
        seekbarHeight = 7;
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        this.max = max;
        invalidate();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        rect.set((getThumbOffset() * 3),
                (getHeight() / 2) - (seekbarHeight),
                getWidth() - (getThumbOffset() * 3),
                (getHeight() / 2) + (seekbarHeight));

        //최대값이 getWidth() - (getThumbOffset() * 3)

        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);

        if (this.getProgress() > max / 2) {

            rect.set(getWidth() / 2,
                    (getHeight() / 2) - (seekbarHeight),
                    getWidth() / 2 + ((getWidth() - (getThumbOffset() * 3)) / 100) * (getProgress() / 100 - 50),
                    getHeight() / 2 + (seekbarHeight));
            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);

        }

        if (this.getProgress() < max / 2) {

            rect.set(getWidth() / 2 - (((getWidth() - (getThumbOffset() * 3)) / 100) * (50 - getProgress() / 100)),
                    (getHeight() / 2) - (seekbarHeight),
                    getWidth() / 2,
                    getHeight() / 2 + (seekbarHeight));
            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);

        }

        super.onDraw(canvas);
    }
}
