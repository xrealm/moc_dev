package com.mao.dev.url;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.mao.dev.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mao on 16/10/8.
 */

public class UrlActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        mTextView = (TextView) findViewById(R.id.tv_text);

        String content = "测试测试https://www.google.com测试测试";
        SpannableString ss = new SpannableString(content);
        String url = getUrl(content);
        if (!TextUtils.isEmpty(url)) {
            int index = content.indexOf(url);
            ss.setSpan(new BiliURLSpan(url), index, index + url.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        mTextView.setText(ss);
    }

    private String getUrl(String content) {
        String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String group = matcher.group();
            Log.d("mpg", group);
            return group;
        }
        return "";
    }
}
