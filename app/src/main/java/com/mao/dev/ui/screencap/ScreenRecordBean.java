package com.mao.dev.ui.screencap;

/**
 * Created by Mao on 2016/12/6.
 */

public class ScreenRecordBean implements Comparable<ScreenRecordBean> {

    public String path;
    public String thumbPath;
    public long timestamp;

    public ScreenRecordBean() {
    }

    public ScreenRecordBean(String path, String thumbPath, long timestamp) {
        this.path = path;
        this.thumbPath = thumbPath;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(ScreenRecordBean o) {
        return (int) (o.timestamp - this.timestamp);
    }
}
