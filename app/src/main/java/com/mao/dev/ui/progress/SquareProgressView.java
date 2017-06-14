package com.mao.dev.ui.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.text.DecimalFormat;


public class SquareProgressView extends View {

    private float maxProgress;
    private float progress;
    private Paint progressBarPaint;
    private Paint outlinePaint;
    private Paint textPaint;
    private Paint baseBarPaint;

    private float widthInDp = 10;
    private float strokewidth = 0;
    private Canvas canvas;

    private boolean outline = false;
    private boolean startline = false;
    private boolean showProgress = false;
    private boolean centerline = false;
    private boolean baseBar = false;

    private PercentStyle percentSettings = new PercentStyle(Align.CENTER, 100,
            true);
    private boolean clearOnHundred = false;
    private boolean isIndeterminate = false;
    private int indeterminate_count = 1;

    private float indeterminate_width = 20.0f;
    ///
    private Path basePath;
    private long duration;
    private long startTimestamp;
    private boolean running = false;

    public SquareProgressView(Context context) {
        super(context);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializePaints(context);
    }

    public SquareProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePaints(context);
    }

    private void initializePaints(Context context) {
        progressBarPaint = new Paint();
        progressBarPaint.setColor(context.getResources().getColor(
                android.R.color.holo_green_dark));
        strokewidth = dp2px(widthInDp);
        progressBarPaint.setStrokeWidth(strokewidth);
        progressBarPaint.setAntiAlias(true);
        progressBarPaint.setStyle(Style.STROKE);
        progressBarPaint.setPathEffect(new CornerPathEffect(10));

        baseBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        baseBarPaint.setColor(0x33000000);
        baseBarPaint.setStrokeWidth(strokewidth);
        baseBarPaint.setStyle(Style.STROKE);
        baseBarPaint.setPathEffect(new CornerPathEffect(10));

        outlinePaint = new Paint();
        outlinePaint.setColor(context.getResources().getColor(
                android.R.color.black));
        outlinePaint.setStrokeWidth(1);
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(
                android.R.color.black));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        generateBasePath();
    }

    private void generateBasePath() {
        basePath = new Path();
        float radius = strokewidth / 2;
        basePath.moveTo(radius, radius);
        basePath.lineTo(getWidth() - radius, radius);
        basePath.lineTo(getWidth() - radius, getHeight() - radius);
        basePath.lineTo(radius, getHeight() - radius);
        basePath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        super.onDraw(canvas);
        float scope = canvas.getWidth() + canvas.getHeight()
                + canvas.getHeight() + canvas.getWidth() - strokewidth;

        if (outline) {
            drawOutline();
        }

        if (startline) {
            drawStartline();
        }

        if (showProgress) {
            drawPercent(percentSettings);
        }

        if (centerline) {
            drawCenterline(strokewidth);
        }

        if ((clearOnHundred && progress == 100.0) || (progress <= 0.0)) {
            if (isBaseBar()) {
                canvas.drawPath(basePath, baseBarPaint);
            }
            return;
        }

        if (isIndeterminate) {
            drawIndeterminateLine(canvas, scope);
        } else if (running) {
            drawDurationProgress(canvas, scope);
        } else {
            drawProgress(canvas, scope);
        }
    }

    private void drawDurationProgress(Canvas canvas, float scope) {
        //caculate current progress
        long current = SystemClock.uptimeMillis();
        float fraction = (float) (current - startTimestamp) / (float) duration;
        progress = maxProgress - maxProgress * fraction;
        drawProgress(canvas, scope);
        invalidate();
    }

    private void drawProgress(Canvas canvas, float scope) {
        if (isBaseBar()) {
            canvas.drawPath(basePath, baseBarPaint);
        }
        Path path = new Path();
        DrawStop drawEnd = getDrawEnd((scope / 100) * progress, canvas);
        if (drawEnd.place == Place.TOP) {
            if (drawEnd.location < canvas.getWidth() / 2) {
                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                path.lineTo(drawEnd.location, strokewidth / 2);
            } else {
                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
                path.lineTo(strokewidth / 2, strokewidth / 2);
                path.lineTo(strokewidth / 2, canvas.getHeight() - strokewidth / 2);
                path.lineTo(canvas.getWidth() - strokewidth / 2, canvas.getHeight() - strokewidth / 2);
                path.lineTo(canvas.getWidth() - strokewidth / 2, strokewidth / 2);
                path.lineTo(drawEnd.location, strokewidth / 2);
            }
            canvas.drawPath(path, progressBarPaint);
        }
        if (drawEnd.place == Place.LEFT) {
            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, drawEnd.location);
            canvas.drawPath(path, progressBarPaint);
        }

        if (drawEnd.place == Place.BOTTOM) {
            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, canvas.getHeight() - strokewidth / 2);
            path.lineTo(drawEnd.location, canvas.getHeight() - strokewidth / 2);
            canvas.drawPath(path, progressBarPaint);
        }

        if (drawEnd.place == Place.RIGHT) {
            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, strokewidth / 2);
            path.lineTo(strokewidth / 2, canvas.getHeight() - strokewidth / 2);
            path.lineTo(canvas.getWidth() - strokewidth / 2, canvas.getHeight() - strokewidth / 2);
            path.lineTo(canvas.getWidth() - strokewidth / 2, drawEnd.location);
            canvas.drawPath(path, progressBarPaint);
        }
    }

//    private void drawProgress(Canvas canvas, float scope) {
//        if (isBaseBar()) {
//            canvas.drawPath(basePath, baseBarPaint);
//        }
//        Path path = new Path();
//        DrawStop drawEnd = getDrawEnd((scope / 100) * progress, canvas);
//
//        if (drawEnd.place == Place.TOP) {
//            if (drawEnd.location > (canvas.getWidth() / 2)) {
//                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
//                path.lineTo(drawEnd.location, strokewidth / 2);
//            } else {
//                path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
//                path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
//                path.lineTo(canvas.getWidth() - (strokewidth / 2), canvas.getHeight() - strokewidth / 2);
//                path.lineTo(strokewidth / 2, canvas.getHeight() - strokewidth / 2);
//                path.lineTo(strokewidth / 2, strokewidth / 2);
//                path.lineTo(drawEnd.location, strokewidth / 2);
//            }
//            canvas.drawPath(path, progressBarPaint);
//        }
//
//        if (drawEnd.place == Place.RIGHT) {
//            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2
//                    + drawEnd.location);
//            canvas.drawPath(path, progressBarPaint);
//        }
//
//        if (drawEnd.place == Place.BOTTOM) {
//            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), canvas.getHeight() - strokewidth / 2);
//            path.lineTo(drawEnd.location, canvas.getHeight()
//                    - (strokewidth / 2));
//            canvas.drawPath(path, progressBarPaint);
//        }
//
//        if (drawEnd.place == Place.LEFT) {
//            path.moveTo(canvas.getWidth() / 2, strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth / 2);
//            path.lineTo(canvas.getWidth() - (strokewidth / 2), canvas.getHeight() - strokewidth / 2);
//            path.lineTo(strokewidth / 2, canvas.getHeight() - strokewidth / 2);
//            path.lineTo((strokewidth / 2), drawEnd.location);
//            canvas.drawPath(path, progressBarPaint);
//        }
//    }

    private void drawIndeterminateLine(Canvas canvas, float scope) {
        Path path = new Path();
        DrawStop drawEnd = getDrawEnd((scope / 100) * Float.valueOf(String.valueOf(indeterminate_count)), canvas);

        if (drawEnd.place == Place.TOP) {
            path.moveTo(drawEnd.location - indeterminate_width - strokewidth, strokewidth / 2);
            path.lineTo(drawEnd.location, strokewidth / 2);
            canvas.drawPath(path, progressBarPaint);
        }

        if (drawEnd.place == Place.RIGHT) {
            path.moveTo(canvas.getWidth() - (strokewidth / 2), drawEnd.location - indeterminate_width);
            path.lineTo(canvas.getWidth() - (strokewidth / 2), strokewidth
                    + drawEnd.location);
            canvas.drawPath(path, progressBarPaint);
        }

        if (drawEnd.place == Place.BOTTOM) {
            path.moveTo(drawEnd.location - indeterminate_width - strokewidth,
                    canvas.getHeight() - (strokewidth / 2));
            path.lineTo(drawEnd.location, canvas.getHeight()
                    - (strokewidth / 2));
            canvas.drawPath(path, progressBarPaint);
        }

        if (drawEnd.place == Place.LEFT) {
            path.moveTo((strokewidth / 2), drawEnd.location - indeterminate_width
                    - strokewidth);
            path.lineTo((strokewidth / 2), drawEnd.location);
            canvas.drawPath(path, progressBarPaint);
        }

        indeterminate_count++;
        if (indeterminate_count > 100) {
            indeterminate_count = 0;
        }
        invalidate();
    }

    private void drawStartline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(canvas.getWidth() / 2, 0);
        outlinePath.lineTo(canvas.getWidth() / 2, strokewidth);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    private void drawOutline() {
        Path outlinePath = new Path();
        outlinePath.moveTo(0, 0);
        outlinePath.lineTo(canvas.getWidth(), 0);
        outlinePath.lineTo(canvas.getWidth(), canvas.getHeight());
        outlinePath.lineTo(0, canvas.getHeight());
        outlinePath.lineTo(0, 0);
        canvas.drawPath(outlinePath, outlinePaint);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        running = false;
        this.invalidate();
    }

    public void setTimerProgress(float progress, long duration) {
        if (duration <= 0) {
            Log.e("Progress", "invalided param....duration!!");
            return;
        }
        this.maxProgress = progress;
        this.progress = progress;
        this.duration = duration;
        this.startTimestamp = SystemClock.uptimeMillis();
        running = true;
        this.invalidate();
    }

    public void setColor(int color) {
        progressBarPaint.setColor(color);
        this.invalidate();
    }

    public void setBaseColor(int color) {
        baseBarPaint.setColor(color);
        this.invalidate();
    }

    public void setWidthInDp(int width) {
        this.widthInDp = width;
        strokewidth = dp2px(widthInDp);
        progressBarPaint.setStrokeWidth(strokewidth);
        baseBarPaint.setStrokeWidth(strokewidth);
        generateBasePath();
        this.invalidate();
    }

    public boolean isBaseBar() {
        return baseBar;
    }

    public void setBaseBar(boolean baseBar) {
        this.baseBar = baseBar;
    }

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
        this.invalidate();
    }

    public boolean isStartline() {
        return startline;
    }

    public void setStartline(boolean startline) {
        this.startline = startline;
        this.invalidate();
    }

    private void drawPercent(PercentStyle setting) {
        textPaint.setTextAlign(setting.getAlign());
        if (setting.getTextSize() == 0) {
            textPaint.setTextSize((canvas.getHeight() / 10) * 4);
        } else {
            textPaint.setTextSize(setting.getTextSize());
        }

        String percentString = new DecimalFormat("###").format(getProgress());
        if (setting.isPercentSign()) {
            percentString = percentString + percentSettings.getCustomText();
        }

        textPaint.setColor(percentSettings.getTextColor());

        canvas.drawText(
                percentString,
                canvas.getWidth() / 2,
                (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint
                        .ascent()) / 2)), textPaint);
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        this.invalidate();
    }

    public void setPercentStyle(PercentStyle percentSettings) {
        this.percentSettings = percentSettings;
        this.invalidate();
    }

    public PercentStyle getPercentStyle() {
        return percentSettings;
    }

    public void setClearOnHundred(boolean clearOnHundred) {
        this.clearOnHundred = clearOnHundred;
        this.invalidate();
    }

    public boolean isClearOnHundred() {
        return clearOnHundred;
    }

    private void drawCenterline(float strokewidth) {
        float centerOfStrokeWidth = strokewidth / 2;
        Path centerlinePath = new Path();
        centerlinePath.moveTo(centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(canvas.getWidth() - centerOfStrokeWidth, centerOfStrokeWidth);
        centerlinePath.lineTo(canvas.getWidth() - centerOfStrokeWidth, canvas.getHeight() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, canvas.getHeight() - centerOfStrokeWidth);
        centerlinePath.lineTo(centerOfStrokeWidth, centerOfStrokeWidth);
        canvas.drawPath(centerlinePath, outlinePaint);
    }

    public boolean isCenterline() {
        return centerline;
    }

    public void setCenterline(boolean centerline) {
        this.centerline = centerline;
        this.invalidate();
    }

    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        isIndeterminate = indeterminate;
        this.invalidate();
    }

    public DrawStop getDrawEnd(float percent, Canvas canvas) {
        DrawStop drawStop = new DrawStop();
        strokewidth = dp2px(widthInDp);
        float halfOfTheImage = canvas.getWidth() / 2;
        if (percent > halfOfTheImage) {
            float second = percent - halfOfTheImage;
            if (second > canvas.getHeight()) {
                float third = second - canvas.getHeight();

                if (third > canvas.getWidth()) {
                    float forth = third - canvas.getWidth();

                    if (forth > canvas.getHeight()) {
                        float fifth = forth - canvas.getHeight();

                        drawStop.place = Place.TOP;
                        if (fifth < halfOfTheImage) {
                            drawStop.location = halfOfTheImage + halfOfTheImage - fifth;
                        } else {
                            drawStop.location = halfOfTheImage;
                        }
                    } else {
                        drawStop.place = Place.RIGHT;
                        drawStop.location = canvas.getHeight() - forth;
                    }
                } else  {
                    drawStop.place = Place.BOTTOM;
                    drawStop.location = third;
                }
            } else {
                drawStop.place = Place.LEFT;
                drawStop.location = strokewidth + second;
            }
        } else {
            drawStop.place = Place.TOP;
            drawStop.location = halfOfTheImage - percent;
        }
        return drawStop;
    }

//    public DrawStop getDrawEnd(float percent, Canvas canvas) {
//        DrawStop drawStop = new DrawStop();
//        strokewidth = dp2px(widthInDp);
//        float halfOfTheImage = canvas.getWidth() / 2;
//
//        if (percent > halfOfTheImage) {
//            float second = percent - halfOfTheImage;
//
//            if (second > canvas.getHeight()) {
//                float third = second - canvas.getHeight();
//
//                if (third > canvas.getWidth()) {
//                    float forth = third - canvas.getWidth();
//
//                    if (forth > canvas.getHeight()) {
//                        float fifth = forth - canvas.getHeight();
//
//                        if (fifth == halfOfTheImage) {
//                            drawStop.place = Place.TOP;
//                            drawStop.location = halfOfTheImage;
//                        } else {
//                            drawStop.place = Place.TOP;
//                            drawStop.location = strokewidth + fifth;
//                        }
//                    } else {
//                        drawStop.place = Place.LEFT;
//                        drawStop.location = canvas.getHeight() - forth;
//                    }
//
//                } else {
//                    drawStop.place = Place.BOTTOM;
//                    drawStop.location = canvas.getWidth() - third;
//                }
//            } else {
//                drawStop.place = Place.RIGHT;
//                drawStop.location = strokewidth + second;
//            }
//
//        } else {
//            drawStop.place = Place.TOP;
//            drawStop.location = halfOfTheImage + percent;
//        }
//
//        return drawStop;
//    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private class DrawStop {

        private Place place;
        private float location;

        public DrawStop() {

        }
    }

    public enum Place {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public static class PercentStyle {
        private Align align;
        private float textSize;
        private boolean percentSign;
        private String customText = "%";
        private int textColor = Color.BLACK;

        public PercentStyle() {
            // do nothing
        }

        public PercentStyle(Align align, float textSize, boolean percentSign) {
            super();
            this.align = align;
            this.textSize = textSize;
            this.percentSign = percentSign;
        }

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
        }

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }

        public boolean isPercentSign() {
            return percentSign;
        }

        public void setPercentSign(boolean percentSign) {
            this.percentSign = percentSign;
        }

        public String getCustomText() {
            return customText;
        }

        /**
         * With this you can set a custom text which should get displayed right
         * behind the number of the progress. Per default it displays a <i>%</i>.
         *
         * @param customText
         *            The custom text you want to display.
         * @since 1.4.0
         */
        public void setCustomText(String customText) {
            this.customText = customText;
        }

        public int getTextColor() {
            return textColor;
        }

        /**
         * Set the color of the text that display the current progress. This will
         * also change the color of the text that normally represents a <i>%</i>.
         *
         * @param textColor
         *            the color to set the text to.
         * @since 1.4.0
         */
        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

    }
}
