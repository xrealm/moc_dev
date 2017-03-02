package com.mao.dev.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.mao.dev.AppKit;
import com.orhanobut.logger.Logger;

/**
 * Created by Mao on 2017/2/26.
 */

public class TextStickerUIHelper {
    private Paint mPaint;

    public TextStickerUIHelper() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(AppKit.dp2px(18));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(0xff000000);
    }

    public Bitmap getTextStickerBitmap(String stickerText, Bitmap bgBitmap) {
        Bitmap bitmap = null;
        try {
            String text = !TextUtils.isEmpty(stickerText) ? stickerText : "";
             NinePatchDrawable ninePatchDrawable = AppKit.getNinePatchDrawableFromCache(bgBitmap);
            if (ninePatchDrawable == null) {
                return null;
            }
            Rect paddingRect = new Rect();
            ninePatchDrawable.getPadding(paddingRect);
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.width() + paddingRect.left + paddingRect.right;
            int height = bounds.height() + paddingRect.top + paddingRect.bottom;
            if (width < bgBitmap.getWidth()) {
                width = bgBitmap.getWidth();
            }
            if (height < bgBitmap.getHeight()) {
                height = bgBitmap.getHeight();
            }
            ninePatchDrawable.setBounds(0, 0, width, height);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            ninePatchDrawable.draw(canvas);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseline = (int) ((height - paddingRect.top - paddingRect.bottom) / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2) + paddingRect.top;
            int x = (width - paddingRect.left - paddingRect.right) / 2 + paddingRect.left;
            Logger.d("x=" + x + ",baseline=" + baseline + ",width=" + width + ",height=" + height);
            canvas.drawText(text, x, baseline, mPaint);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
