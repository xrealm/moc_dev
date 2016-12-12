package com.mao.dev.ui.marquee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 16/10/7.
 */

public class MarqueeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);

        final MarqueeText marqueeText = (MarqueeText) findViewById(R.id.marquee_text);

        findViewById(R.id.btn_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marqueeText.isScroll()) {
                    marqueeText.stopScroll();
                } else {
                    marqueeText.startScroll();
                }
            }
        });
    }
}
