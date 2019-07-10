package com.mao.dev.ui.screencap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.SystemClock;
import android.provider.MediaStore;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.dev.R;

import java.io.File;
import java.util.List;

/**
 * Created by Mao on 2016/12/6.
 */

public class ScreenVideoAdapter extends RecyclerView.Adapter<ScreenVideoAdapter.ViewHolder> {

    private List<ScreenRecordBean> mList;

    public ScreenVideoAdapter(List<ScreenRecordBean> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_cap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ScreenRecordBean item = getItem(position);
        holder.tvVideoTime.setText(item.timestamp + "");
//        holder.ivThumb.setImageBitmap(getThumb(item.thumbPath, holder.ivThumb.getWidth(), holder.ivThumb.getHeight()));
        long start = SystemClock.uptimeMillis();
//        holder.ivThumb.setImageBitmap(MediaStore.Video.Thumbnails.getThumbnail(AppKit.getContext().getContentResolver(), item.id, MediaStore.Video.Thumbnails.MICRO_KIND, null));
        AsyncThumbLoader.getInstance().generateVideoThumb(item.path, new AsyncThumbLoader.ThumbLoaderListener() {
            @Override
            public void onLoadSuccess(File file) {
                holder.ivThumb.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }

            @Override
            public void onLoadFailed() {

            }
        });
        Log.d("mpg", "time=" + (SystemClock.uptimeMillis() - start));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayScreenRecordActivity.launch(holder.itemView.getContext(), item.path, item.thumbPath);
            }
        });
    }

    private Bitmap getThumb(String thumbPath,int width, int height) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inDither = false
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bitmap = MediaStore.Video.

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(thumbPath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public ScreenRecordBean getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        ImageView ivMore;
        TextView tvVideoTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_screen_record);
            ivMore = (ImageView) itemView.findViewById(R.id.iv_more);
            tvVideoTime = (TextView) itemView.findViewById(R.id.tv_video_timestamp);
        }
    }


}
