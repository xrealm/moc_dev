package com.mao.dev.screencap;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 2016/12/7.
 */

public class ScreenRecordListLoader extends AsyncTaskLoader<List<ScreenRecordBean>> {

    public ScreenRecordListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("mpg", "onStartLoading");
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d("mpg", "onStopLoading");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d("mpg", "onReset");
        stopLoading();
    }

    @Override
    public List<ScreenRecordBean> loadInBackground() {
        Log.d("mpg", "loadInBackground");
        return getVideo("369685912");
    }

    private List<ScreenRecordBean> getVideo(String bucketName) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/immomo/screenrecoder", bucketName);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        List<ScreenRecordBean> list = new ArrayList<>();
        for (File file : files) {
            if (file.getName().endsWith(".mp4")) {
                AsyncThumbLoader.getInstance().generateVideoThumb(file.getAbsolutePath(), null);
                ScreenRecordBean bean = new ScreenRecordBean(file.getAbsolutePath(),
                        AsyncThumbLoader.getInstance().mappingThumbPath(file), file.lastModified());
                list.add(bean);
            }
        }
        return list;
    }
}
