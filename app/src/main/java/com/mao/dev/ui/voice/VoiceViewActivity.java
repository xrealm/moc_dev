package com.mao.dev.ui.voice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/2/20.
 */

public class VoiceViewActivity extends AppCompatActivity{

    private VoiceView mVoiceView;
    boolean loop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_line);
        mVoiceView = (VoiceView) findViewById(R.id.voice_view);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loop = false;
            }
        });
    }

    private void start() {
        loop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}