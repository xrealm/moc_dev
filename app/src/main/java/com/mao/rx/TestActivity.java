package com.mao.rx;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.dev.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mao on 2016/12/31.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test);

//        test();
        testZip();
    }

    private void testZip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                SystemClock.sleep(1000);
                Logger.d("observable1 subscribe");
                e.onNext(111);
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                SystemClock.sleep(1000);
                Logger.d("observable2 subscribe");
                e.onNext(222);
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable3 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                SystemClock.sleep(1000);
                Logger.d("observable3 subscribe");
                e.onNext(333);
            }
        }).subscribeOn(Schedulers.io());

        ArrayList<Observable<Integer>> observables = new ArrayList<>();
        observables.add(observable1);
        observables.add(observable2);
        observables.add(observable3);

        Observable.zip(observables, new Function<Object[], String>() {
            @Override
            public String apply(Object[] integers) throws Exception {
                StringBuilder stringBuilder = new StringBuilder();
                for (Object integer : integers) {
                    stringBuilder.append(integer).append(":");
                }
                return stringBuilder.toString();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Logger.d("accept " + s);
                    }
                });
//        Observable.zip(observable1, observable2, observable3, new Function3<Integer, Integer, Integer, String>() {
//            @Override
//            public String apply(Integer integer, Integer integer2, Integer integer3) throws Exception {
//                Logger.d("apply");
//                return integer + ":" + integer2 + ":" + integer3;
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Logger.d("accept " + s);
//                    }
//                });
//        Observable.zip(observable1, observable2, new BiFunction<Integer, Integer, String>() {
//            @Override
//            public String apply(Integer integer, Integer integer2) throws Exception {
//                SystemClock.sleep(1000);
//                Logger.d("apply");
//                return integer + ":" + integer2;
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Logger.d("accept " + s);
//                    }
//                });

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
