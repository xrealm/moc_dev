package com.mao.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.dev.R;
import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mao on 2016/12/31.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test);

        test();
    }

    private void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Logger.d("emit 1");
                e.onNext("11");
                Logger.d("emit 2");
                e.onNext("22");
                e.onComplete();
                Logger.d("emit 3");
                e.onNext("33");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                Logger.d("onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(String value) {
                Logger.d("onNext " + value);
                if ("11".equals(value)) {
                    Logger.d("dispose");
                    mDisposable.dispose();
                    Logger.d("isDisposed=" + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Logger.d("onComplete");
            }
        });
    }

    private Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> e) throws Exception {
            e.onNext("11");
            e.onNext("22");
            e.onComplete();
        }
    });

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            Logger.d("onSubscribe");
        }

        @Override
        public void onNext(String value) {
            Logger.d("onNext " + value);
        }

        @Override
        public void onError(Throwable e) {
            Logger.d("onError ", e);
        }

        @Override
        public void onComplete() {
            Logger.d("onComplete");
        }
    };
}
