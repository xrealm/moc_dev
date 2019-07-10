package com.mao.dev.ui.custom;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mao.dev.R;

/**
 * Created by Mao on 2016/11/29.
 */

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        findViewById(R.id.fl_circle).getBackground().setLevel(5000);
    }
}
