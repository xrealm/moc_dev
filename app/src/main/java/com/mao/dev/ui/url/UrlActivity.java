package com.mao.dev.ui.url;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mao.dev.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mao on 16/10/8.
 */

public class UrlActivity extends AppCompatActivity {

    TextView mTextView;
    PopupWindow mPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        mTextView = (TextView) findViewById(R.id.tv_text);

//        test1();

        test2();
    }

    private void test2() {
        SpannableStringBuilder ssb = new SpannableStringBuilder("使用手机直播，和身边另一台手机、电脑、无人机、手持摄像机连接，进行双镜头直播。");
        String highLightText = "了解更多";
        int start = ssb.length();
        ssb.append(highLightText);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                start, start + highLightText.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d("mao", "clickclick");
            }
        }, start, start + highLightText.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.setText(ssb);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void test1() {
        String content = "测试测试https://www.google.com测试测试";
        SpannableString ss = new SpannableString(content);
        String url = getUrl(content);
        if (!TextUtils.isEmpty(url)) {
            int index = content.indexOf(url);
            ss.setSpan(new BiliURLSpan(url), index, index + url.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        mTextView.setText(ss);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    TextView textView = new TextView(UrlActivity.this);
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setText("托尔斯泰");
                    mPopupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
                    mPopupWindow.setTouchable(true);
                    mPopupWindow.setOutsideTouchable(true);
                }
                if (mPopupWindow.getContentView().getMeasuredWidth() == 0) {
                    mPopupWindow.getContentView().measure(0, 0);
                }
                mPopupWindow.showAsDropDown(mTextView, mTextView.getWidth() / 2 - mPopupWindow.getContentView().getMeasuredWidth() / 2, -mTextView.getHeight() - mPopupWindow.getContentView().getMeasuredHeight());
            }
        });
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
