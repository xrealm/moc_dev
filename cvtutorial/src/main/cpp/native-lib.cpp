//
// Created by Mao on 25/9/18.
//

#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include "opencv/cv.h"
#include "opencv2/opencv.hpp"

using namespace cv;

extern "C" JNIEXPORT jstring JNICALL
Java_com_master_cvtutorial_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    std::string v = cv::getVersionString();

    return env->NewStringUTF(v.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_master_cvtutorial_MainActivity_edgeExtraction(JNIEnv *env, jobject instance,
                                                       jobject bitmap) {
    AndroidBitmapInfo info;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
              info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    void *pixels;
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        cv::Mat temp(info.height, info.width, CV_8UC4, pixels);
        cv::Mat gray;
        cv::cvtColor(temp, gray, cv::COLOR_RGBA2GRAY);
//        cv::Canny(gray, gray, 50, 10);
        cv::cvtColor(gray, temp, cv::COLOR_GRAY2BGRA);
    } else {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGB2GRAY);
        Canny(gray, gray, 125, 225);
        cvtColor(gray, temp, COLOR_GRAY2RGB);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}