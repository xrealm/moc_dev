package com.mao.dev.ui.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mao.dev.R;

/**
 * Created by Mao on 2016/12/20.
 */

public class QRActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mImageView = (ImageView) findViewById(R.id.iv_qr);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = new QRCodeEncoder.Builder().width(800)
                        .height(800)
                        .paddingPx(0)
                        .marginPt(1)
                        .build()
                        .encode("http://www.immomo.com");
                mImageView.setImageBitmap(bitmap);
            }
        });

    }
}
