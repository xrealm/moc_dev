package com.mao.dev.ui.vb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.dev.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mao on 2016/11/12.
 */

public class VBActivity extends AppCompatActivity {

    private ImageView mIvTest;
    private TextView mTvTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vb);
        mIvTest = (ImageView) findViewById(R.id.iv_test);
        mTvTest = (TextView) findViewById(R.id.tv_test);

//        getWindow().getDecorView().post(new Runnable() {
//            @Override
//            public void run() {
//                createBitmap();
//            }
//        });

        mTvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvTest.setImageBitmap(createBitmap());
            }
        });

    }

    private Bitmap createBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mTvTest.getWidth(), mTvTest.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);
//        mTvTest.draw(canvas);
        canvas.drawText(mTvTest.getText().toString(), 0, mTvTest.getHeight() / 2, mTvTest.getPaint());

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/watermark";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            File output = new File(file.getAbsolutePath() + "/watermark_000.png");
            if (!output.exists()) {
                output.createNewFile();
            }
            fos = new FileOutputStream(output);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (compress) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
