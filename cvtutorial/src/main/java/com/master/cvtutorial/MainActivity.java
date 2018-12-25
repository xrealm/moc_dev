package com.master.cvtutorial;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.master.cvtutorial.R;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    ImageView mSourceIv;
    ImageView mDestIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSourceIv = (ImageView) findViewById(R.id.iv_source);
        mDestIv = (ImageView) findViewById(R.id.iv_dest);

        TextView textView = findViewById(R.id.test);
        textView.setText(stringFromJNI());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        mSourceIv.setImageBitmap(bitmap);

    }

    private void process() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        edgeExtraction(bitmap);
        mDestIv.setImageBitmap(bitmap);
    }

    public native void edgeExtraction(Bitmap bitmap);

    public native String stringFromJNI();
}
