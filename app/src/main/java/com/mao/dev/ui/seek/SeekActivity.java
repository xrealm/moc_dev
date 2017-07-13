package com.mao.dev.ui.seek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/7/12.
 */

public class SeekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);

        TwoWaySeekBar seekBar = (TwoWaySeekBar) findViewById(R.id.seekbar);

    }
}
