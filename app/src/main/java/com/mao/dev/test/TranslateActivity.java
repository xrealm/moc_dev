package com.mao.dev.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2016/11/19.
 */

public class TranslateActivity extends AppCompatActivity {

    View mView;
    int translateX;
    int translateY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        mView = findViewById(R.id.view_test);
        findViewById(R.id.btn_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawBitmap();
                    }
                }).start();

                if (translateX <= 200) {
                    translateX += 20;
                    translateY += 20;
                } else {
                    translateX -= 20;
                    translateY -= 20;
                }
                mView.setTranslationX(translateX);
                mView.setTranslationY(translateY);
                Log.d("mpg", "x=" + translateX + ",y=" + translateY);
            }
        });
    }

    private void drawBitmap() {
        NinePatchDrawable drawable = (NinePatchDrawable) getResources().getDrawable(R.drawable.live_textstickers_chicken);
        drawable.setBounds(new Rect(0, 0, 800, 300));
        Bitmap bitmap = Bitmap.createBitmap(800, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);
        paint.setColor(0xffff0000);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        canvas.drawText("哈你直播", 400, 150 - fontMetrics.top / 2 - fontMetrics.bottom / 2, paint);

        bitmap.getWidth();
        bitmap.getHeight();
    }
}
