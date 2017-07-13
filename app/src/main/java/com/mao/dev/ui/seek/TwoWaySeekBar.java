package com.mao.dev.ui.seek;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.mao.dev.R;

/**
 * 双向Seekbar
 * Created by Mao on 2017/7/12.
 */

public class TwoWaySeekBar extends View {

    private static final int TOTAL_PROGRESS = 200;
    private static final int BASE_PROGRESS = 100;

    public interface OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *                 was set by {@link android.widget.ProgressBar#setMax(int)}. (The default value for max is 200.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        void onProgressChanged(TwoWaySeekBar seekBar, int progress, boolean fromUser);

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStartTrackingTouch(TwoWaySeekBar seekBar);

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        void onStopTrackingTouch(TwoWaySeekBar seekBar);
    }

    OnSeekBarChangeListener mOnSeekBarChangeListener;

    private int mMinWidth;
    private int mMinHeight;
    private int mWidth;
    private int mHeight;
    private Rect mContentRect;

    Drawable mThumb;
    private Paint mBasePaint;
    private Paint mHighlightPaint;
    private int mProgressColor = 0xffff2d55;
    private int mBackgroundProgressColor = 0x20ffffff;
    private int mBasePointColor = 0xffffffff;

    private int mDefaultThumbOffset;//thumb基点
    private float mThumbOffset;
    private int mProgress;
    private float mTouchDownX;
    private boolean mIsDragging;
    private int mScaledTouchSlop;

    public TwoWaySeekBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public TwoWaySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoWaySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TwoWaySeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mThumb = getResources().getDrawable(R.drawable.hani_shape_thumb);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TwoWaySeekBar, defStyleAttr, defStyleRes);
            setProgressColor(array.getColor(R.styleable.TwoWaySeekBar_progress_color, mProgressColor));
            setBackgroundProgressColor(array.getColor(R.styleable.TwoWaySeekBar_background_progress_color, mBackgroundProgressColor));
            setBasePointColor(array.getColor(R.styleable.TwoWaySeekBar_basepoint_color, mBasePointColor));
            Drawable drawable = array.getDrawable(R.styleable.TwoWaySeekBar_thumb);
            if (drawable != null) {
                mThumb = drawable;
            }
            array.recycle();
        }
        mMinHeight = dp2px(3);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setStrokeWidth(mMinHeight);
        mBasePaint.setColor(mBackgroundProgressColor);
        mBasePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStrokeWidth(mMinHeight);
        mHighlightPaint.setColor(mProgressColor);
        mBasePaint.setStyle(Paint.Style.STROKE);

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setProgress(-30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        if (mThumb != null) {
            dw = Math.max(mMinWidth, MeasureSpec.getSize(widthMeasureSpec));
            dh = Math.max(mMinHeight, Math.min(mThumb.getIntrinsicHeight(), MeasureSpec.getSize(heightMeasureSpec)));
        }
        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
                resolveSizeAndState(dh, heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mContentRect = new Rect(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        mDefaultThumbOffset = w / 2;
        initThumb();
    }

    private void initThumb() {
        if (mThumb != null) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            mThumbOffset = normalizeToOffset(mProgress);
            Rect rect = new Rect((int) (mThumbOffset - mThumb.getIntrinsicWidth() / 2), paddingTop,
                    (int) (mThumbOffset + mThumb.getIntrinsicWidth() / 2), mContentRect.bottom - paddingBottom);
            mThumb.setBounds(rect);
        }
    }

    private void setThumbRect(float progress) {
        if (mThumb != null) {
            Rect bounds = mThumb.getBounds();
            int left = (int) (normalizeToOffset(progress)) - bounds.width() / 2;
            Rect drawRect = new Rect(left, bounds.top, left + bounds.width(), bounds.bottom);
            mThumb.setBounds(drawRect);
        }
    }

    public void setProgress(int progress) {
        if (mProgress==progress) {
            return;

        }
        mProgress = progress;
        onProgressChanged(progress, false);
        invalidate();
    }

    public void setThumb(@NonNull Drawable thumb) {
        mThumb = thumb;
        initThumb();
    }

    private void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    private void setBackgroundProgressColor(int color) {
        mBackgroundProgressColor = color;
        invalidate();
    }

    private void setBasePointColor(int color) {
        mBasePointColor = color;
        invalidate();
    }

    private float normalizeToOffset(float progress) {
        return mContentRect.left + (progress + BASE_PROGRESS) / (float) TOTAL_PROGRESS * mContentRect.width();
    }

    private float offsetToNormalize(float thumbOffset) {
        return thumbOffset / (float) mWidth * TOTAL_PROGRESS;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawBaseTrack(canvas);
        drawTrack(canvas);
        drawBasePoint(canvas);
        drawThumb(canvas);
    }

    private void drawThumb(Canvas canvas) {
        if (mThumb == null) {
            return;
        }

        final int saveCount = canvas.save();
        mThumb.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    private void drawBaseTrack(Canvas canvas) {
        mBasePaint.setColor(0x20ffffff);
        canvas.drawLine(getPaddingLeft(), mHeight / 2, mWidth - getPaddingRight(), mHeight / 2, mBasePaint);
    }

    private void drawTrack(Canvas canvas) {
        if (mThumbOffset < mDefaultThumbOffset) {
            canvas.drawLine(mThumbOffset, mHeight / 2, mDefaultThumbOffset, mHeight / 2, mHighlightPaint);
        } else {
            canvas.drawLine(mDefaultThumbOffset, mHeight / 2, mThumbOffset, mHeight / 2, mHighlightPaint);
        }
    }

    private void drawBasePoint(Canvas canvas) {
        mBasePaint.setColor(mBasePointColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, dp2px(0.75f), mBasePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer(event.getX(), event.getY())) {
                    mTouchDownX = event.getX();
                } else {
                    startDrag(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    if (Math.abs(event.getX() - mTouchDownX) > mScaledTouchSlop) {
                        startDrag(event);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
                break;
        }
        return true;
    }

    private boolean isInScrollingContainer(float x, float y) {
        return mThumb != null && mThumb.getBounds().contains((int) x, (int) y);
    }

    private void startDrag(MotionEvent event) {
        setPressed(true);
        if (mThumb != null) {
            invalidate(mThumb.getBounds());
        }
        onStartTrackingTouch();
        trackTouchEvent(event);
        attemptClaimDrag();
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        final int x = Math.round(event.getX());
        final int y = Math.round(event.getY());
        final int availableWidth = mContentRect.left + mContentRect.width();
        if (x <= 0) {
            mThumbOffset = mContentRect.left;
        } else if (x >= availableWidth) {
            mThumbOffset = availableWidth;
        } else {
            mThumbOffset = x;
        }
        int progress = (int) offsetToNormalize(mThumbOffset);
        if (progress == BASE_PROGRESS) {
            progress = 0;
        } else if (progress < BASE_PROGRESS) {
            progress -= BASE_PROGRESS;
        } else {
            progress -= BASE_PROGRESS;
        }
        setThumbRect(progress);
        onProgressChanged(progress, true);
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    void onProgressChanged(int progress, boolean fromUser) {
        mProgress = progress;
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }
    }

    void onStartTrackingTouch() {
        mIsDragging = true;
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        mIsDragging = false;
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener changeListener) {
        mOnSeekBarChangeListener = changeListener;
    }
}
