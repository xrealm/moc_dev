package com.mao.dev.autosize;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Mao on 2016/10/18.
 */

public class AutoEditText extends EditText{
    public AutoEditText(Context context) {
        super(context);
    }

    public AutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
