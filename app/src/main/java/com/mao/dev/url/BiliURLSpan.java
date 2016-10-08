package com.mao.dev.url;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Mao on 16/10/8.
 */

public class BiliURLSpan extends URLSpan {


    public BiliURLSpan(String url) {
        super(url);
    }

    public BiliURLSpan(Parcel src) {
        super(src);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(widget.getContext(), getURL(), Toast.LENGTH_SHORT).show();
    }
}
