package com.mao.dev.screencap;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Mao on 2016/12/6.
 */

public class AsyncThumbLoader {

    private HashSet<String> mLoadSet = new HashSet<>();
    private static AsyncThumbLoader mThumbLoader;

    public static AsyncThumbLoader getInstance() {
        //不加synchronized了
        if (mThumbLoader == null) {
            mThumbLoader = new AsyncThumbLoader();
        }
        return mThumbLoader;
    }

    public void generateVideoThumb(String videoPath, ThumbLoaderListener listener) {
        if (TextUtils.isEmpty(videoPath) || !videoPath.endsWith(".mp4")) {
            if (listener != null) {
                listener.onLoadFailed();
            }
            return;
        }

        File video = new File(videoPath);
        if (!video.exists()) {
            if (listener != null) {
                listener.onLoadFailed();
            }
            return;
        }
        File file = mappingThumbFile(video);
        if (file.exists()) {
            if (listener != null) {
                listener.onLoadSuccess(file);
            }
            return;
        }
        if (mLoadSet.contains(videoPath)) {
            // 已经在下载
            return;
        }
        new GenetateThumbTask(video, listener).execute();
    }

    private File mappingThumbFile(File video) {
        return new File(mappingThumbPath(video));
    }

    public String mappingThumbName(File video) {
        return video.getName().substring(0, video.getName().lastIndexOf(".")) + ".jpg";
    }

    public String mappingThumbPath(File video) {
        return video.getParent() + "/" + mappingThumbName(video);

    }

    private class GenetateThumbTask extends AsyncTask<Void, Void, File> {

        File mVideoFile;
        ThumbLoaderListener mListener;

        public GenetateThumbTask(File videoFile, ThumbLoaderListener listener) {
            this.mVideoFile = videoFile;
            mListener = listener;
        }

        @Override
        protected File doInBackground(Void... params) {

            if (!mVideoFile.exists()) {
                if (mListener != null) {
                    mListener.onLoadFailed();
                }
                return null;
            }
            //这个生成的缩略图太小了
//            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 720, 1280);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mVideoFile.getAbsolutePath());
            Bitmap bitmap = retriever.getFrameAtTime();
            if (bitmap != null) {
                File file = mappingThumbFile(mVideoFile);
                saveImageFile(bitmap, file);
                bitmap.recycle();
                return file;
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (mListener != null && file != null && file.exists()) {
                mLoadSet.remove(mVideoFile);
                mListener.onLoadSuccess(file);
            }
        }
    }

    public static void saveImageFile(Bitmap bitmap, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface ThumbLoaderListener {
        void onLoadSuccess(File file);

        void onLoadFailed();
    }
}
