package com.mao.dev.ui.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mao.dev.AppKit;
import com.mao.dev.R;

/**
 * Created by Mao on 2016/12/20.
 */

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        ImageView imageView = (ImageView) findViewById(R.id.iv_qr);

        Bitmap bitmap = new QRCodeEncoder.Builder().width(800)
                .height(800)
                .paddingPx(0)
                .marginPt(3)
                .build()
                .encode("http://www.immomo.com");
        imageView.setImageBitmap(bitmap);

        AppKit.getHostIp();
    }
}
