package com.mao.dev.ui.seek;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.ResultReceiver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.dev.R;

import java.lang.reflect.Method;

/**
 * Created by Mao on 2017/7/22.
 */

public class SearchView extends FrameLayout {

    private AppCompatAutoCompleteTextView mSearchSrcTextView;
    private ImageView mSearchIcon;
    private ImageView mCloseButton;
    private TextView mSearchHint;
    private TextView mSearchCancel;

    private CharSequence mUserQuery;
    private CharSequence mOldQueryText;

    private OnQueryTextListener mOnQueryChangeListener;
    private OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private CancelClickListener mCancelClickListener;

    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();

    /*
     * SearchView can be set expanded before the IME is ready to be shown during
     * initial UI setup. The show operation is asynchronous to account for this.
     */
    private Runnable mShowImeRunnable = new Runnable() {
        @Override
        public void run() {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, SearchView.this, 0);
            }
        }
    };

    public SearchView(@NonNull Context context) {
        super(context);
        init();
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.hani_view_search_view, this);
        mSearchSrcTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.et_search);
        mSearchIcon = (ImageView) findViewById(R.id.iv_search_icon);
        mCloseButton = (ImageView) findViewById(R.id.iv_search_clear);
        mSearchHint = (TextView) findViewById(R.id.tv_search_hint);
        mSearchCancel = (TextView) findViewById(R.id.tv_search_cancel);

        mSearchSrcTextView.setOnClickListener(mOnClickListener);
        mCloseButton.setOnClickListener(mOnClickListener);
        mSearchCancel.setOnClickListener(mOnClickListener);

        mSearchSrcTextView.addTextChangedListener(mTextWatcher);
        mSearchSrcTextView.setOnEditorActionListener(mOnEditorActionListener);
        mSearchSrcTextView.setOnKeyListener(mTextKeyListener);

        // Inform any listener of focus changes
        mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnQueryTextFocusChangeListener != null) {
                    mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, hasFocus);
                }
            }
        });
    }

    @Override
    public void clearFocus() {
        setImeVisibility(false);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
    }

    void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    SearchView.this.clearFocus();
                    SearchView.this.setImeVisibility(false);
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mCloseButton) {
                onCloseClicked();
            } else if (v == mSearchSrcTextView) {
                mSearchHint.setVisibility(GONE);
                mSearchIcon.setVisibility(VISIBLE);
                mSearchCancel.setVisibility(VISIBLE);
            } else if (v == mSearchCancel) {
                onCancelClicked();
            }
        }
    };

    private void onCancelClicked() {
        if (mCancelClickListener != null) {
            mCancelClickListener.onCancelClicked();
        }
        onCloseClicked();
        mSearchIcon.setVisibility(GONE);
        mSearchHint.setVisibility(VISIBLE);
        mSearchCancel.setVisibility(GONE);
    }

    private void onCloseClicked() {
        CharSequence text = mSearchSrcTextView.getText();
        if (!TextUtils.isEmpty(text)) {
            mSearchSrcTextView.setText("");
            mSearchSrcTextView.requestFocus();
            setImeVisibility(true);
        }
    }

    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if (!isEmpty() && event.hasNoModifiers()) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();
                        return true;
                    }
                }
            }
            return false;
        }
    };

    private boolean isEmpty() {
        return TextUtils.getTrimmedLength(mSearchSrcTextView.getText()) == 0;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int after) {
            SearchView.this.onTextChanged(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        /**
         * Called when the input method default action key is pressed.
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            onSubmitQuery();
            return true;
        }
    };

    void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = newText;
        boolean hasText = !TextUtils.isEmpty(text);
        updateCloseButton();
        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mSearchSrcTextView.getText());
        mCloseButton.setVisibility(hasText ? VISIBLE : GONE);
    }

    void onSubmitQuery() {
        CharSequence query = mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null
                    || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                setImeVisibility(false);
            }
        }
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener the listener object that receives callbacks when the user performs
     * actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnQueryTextFocusChangeListener(OnFocusChangeListener listener) {
        mOnQueryTextFocusChangeListener = listener;
    }

    public void setCancelClickListener(CancelClickListener listener) {
        mCancelClickListener = listener;
    }

    private static class AutoCompleteTextViewReflector {
        private Method doBeforeTextChanged, doAfterTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                doBeforeTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                doAfterTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                ensureImeVisible = AutoCompleteTextView.class
                        .getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod(
                        "showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (Exception e) {
                }
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception e) {
                }
            }

            // Hidden method failed, call public version instead
            imm.showSoftInput(view, flags);
        }
    }

    /**
     * Callbacks for changes to the query text.
     */
    public interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         *
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         *
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }

    public interface CancelClickListener {
        void onCancelClicked();
    }
}
