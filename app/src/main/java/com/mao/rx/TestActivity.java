package com.mao.rx;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mao.dev.R;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
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

        initView();
//        test();
//        testZip();
//        flowabledemo1();
//        flowabledemo2();
    }

    private void initView() {
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flowabledemo4();
                flowabledemo5();
            }
        });

        findViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(128);
            }
        });
    }

    private void flowabledemo5() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Logger.d("emit 1");
                e.onNext(1);
                Logger.d("emit 2");
                e.onNext(2);
                Logger.d("emit 3");
                e.onNext(3);
                Logger.d("emit complete");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Logger.d("onSubscribe");
                        mSubscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.d("onNext " + integer);
                        mSubscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.d("onError");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("onComplete");
                    }
                });
    }

    private void testGson() {
        String extension = "{\\\"filterid\\\":\\\"自然清新\\\",\\\"filtertype\\\":\\\"自然清新\\\",\\\"filtertest\\\":\\\"自然清新\\\"}";
        Map<String, String> map = new HashMap<>();
        map.put("filter_id", "ziran");
        map.put("filtertype", "xiaoziran");
        map.put("filtertest", "daziran");

        Gson gson = new Gson();
        String json = gson.toJson(map);
        Logger.d(json);

        Object o = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        Logger.d(o);
    }

    private void request(int amount) {
        mSubscription.request(amount);
    }

    private void flowabledemo4() {
        Flowable.create(createFlowableOnSubscribe3(), BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSubscriber());
    }

    private void flowabledemo3() {
        Flowable.create(createFlowableOnSubscribe3(), BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSubscriber());
    }

    private FlowableOnSubscribe<Integer> createFlowableOnSubscribe3() {
        return new FlowableOnSubscribe<Integer>() {

            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    e.onNext(i);
                }
                Logger.d("emit complete");
                e.onComplete();
            }
        };
    }


    private void flowabledemo2() {
        Flowable.create(createFlowableOnSubscribe(), BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSubscriber());
    }

    private FlowableOnSubscribe<Integer> createFlowableOnSubscribe() {
        return new FlowableOnSubscribe<Integer>() {

            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                Logger.d("emit 1");
                e.onNext(1);
                Logger.d("emit 2");
                e.onNext(2);
                Logger.d("emit 3");
                e.onNext(3);
                Logger.d("emit complete");
                e.onComplete();
            }
        };
    }

    private Subscription mSubscription;

    private Subscriber<Integer> createSubscriber() {
        return new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Logger.d("onSubscribe");
                mSubscription = s;
                s.request(128);
            }

            @Override
            public void onNext(Integer integer) {
                Logger.d("onNext" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Logger.d("onError", t);
            }

            @Override
            public void onComplete() {
                Logger.d("onComplete");
            }
        };
    }

    private void flowabledemo1() {
        Flowable<Integer> upStream = Flowable.create(createFlowableOnSubscribe(), BackpressureStrategy.ERROR);

        Subscriber<Integer> downStream = createSubscriber();
        upStream.subscribe(downStream);
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
