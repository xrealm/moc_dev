package com.mao.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mao.dev.R;


/**
 * Created by Mao on 2019-05-24.
 */
public class C extends LifeActivity {

    @Override
    protected String getName() {
        return "C";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(C.this, D.class);
                startActivity(intent);
            }
        });
    }
}
