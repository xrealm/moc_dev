package com.mao.dev.ui.qr;

import android.graphics.Bitmap;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 2.0
 */
public interface PixelsProcessor {

    Bitmap create(int[] pixels, int width, int height);
}
