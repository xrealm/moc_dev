package com.master.aptdmeo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.mao.apt_lib.InjectHelper;
import com.mao.dev_annotation.BindView;
import com.mao.dev_annotation.Test;

@Test
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.testhello)
    TextView testHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectHelper.bind(this);
        System.out.println("mao  --> " + testHello);
    }
}
