package com.mao.dev.ui.screencap;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mao.dev.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 2016/12/6.
 */

public class ScreenRecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ScreenRecordBean>> {

    private RecyclerView mRecyclerView;

    private String mRecordPath = Environment.getExternalStorageDirectory() + "/immomo/screenrecoder";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screencap);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_screen_cap);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
//        new ScreenCapTask().execute();

        getLoaderManager().initLoader(0, null, this);
    }

    private List<ScreenRecordBean> getVideo(String bucketName) {
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME +  " = \"" + bucketName + "\"";
        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media._ID};
        Cursor cursor = null;
        List<ScreenRecordBean> list = null;
        try {
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, null, orderBy + " DESC");
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    ScreenRecordBean bean = new ScreenRecordBean();
                    bean.path = cursor.getString(0);
                    bean.timestamp = cursor.getLong(1);
                    bean.thumbPath = getVideoThumb(cursor.getString(2));
                    Log.d("mpg", "path=" + bean.path + "\nthumbPath=" + bean.thumbPath);
                    list.add(bean);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private String getVideoThumb(String id) {
        Cursor cursor = null;

        try {
            String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id;
            cursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID},
                    selection, null, null);
            if (cursor != null && cursor.moveToNext()) {
                String thumb = cursor.getString(0);
                return thumb;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public Loader<List<ScreenRecordBean>> onCreateLoader(int id, Bundle args) {
        Log.d("mpg", "onCreateLoader");
        return new ScreenRecordListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<ScreenRecordBean>> loader, List<ScreenRecordBean> data) {
        Log.d("mpg", "onLoadFinished");
        ScreenVideoAdapter adapter = new ScreenVideoAdapter(data);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<ScreenRecordBean>> loader) {
        Log.d("mpg", "onLoaderReset");
    }

    class ScreenCapTask extends AsyncTask<Void, Void, List<ScreenRecordBean>> {

        @Override
        protected List<ScreenRecordBean> doInBackground(Void... params) {
            return getVideo("369685912");
//            return getVideoFromPath("369685912");
        }

        @Override
        protected void onPostExecute(List<ScreenRecordBean> screenCapBeen) {
            ScreenVideoAdapter adapter = new ScreenVideoAdapter(screenCapBeen);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private List<ScreenRecordBean> getVideoFromPath(String userId) {
        File folder = new File(mRecordPath, userId);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        List<ScreenRecordBean> list = new ArrayList<>();
        for (File file : files) {
            if (file.getName().endsWith(".mp4")) {
                ScreenRecordBean bean = new ScreenRecordBean();
                bean.path = file.getAbsolutePath();
                bean.timestamp = file.lastModified();
                list.add(bean);
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight());
            }
        }
        return list;
    }
}
