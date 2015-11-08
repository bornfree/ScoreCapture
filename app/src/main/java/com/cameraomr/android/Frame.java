package com.cameraomr.android;

/**
 * Created by harsha on 7/11/15.
 */

import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import android.hardware.Camera.Size;
import android.util.Log;

public class Frame {

    private final String TAG = "Frame Class";
    private Mat mFrame;
    private long mStartTime = 0 , mEndTime = 0;

    public Frame(byte[] data, Size size) {
        mStartTime= System.nanoTime();
        Mat yuv = new Mat(size.height + size.height / 2, size.width, CvType.CV_8UC1);
        mFrame = new Mat();
        yuv.put(0, 0, data);

        // Convert Mat to gray and make a copy of template too
        Imgproc.cvtColor( yuv, mFrame, Imgproc.COLOR_YUV2GRAY_NV21, 1 );
    }

    public void process()
    {
        processFrame(mFrame.getNativeObjAddr());
        mEndTime = System.nanoTime();
    }

    public void printProcessingTime()
    {
        long duration = (mEndTime - mStartTime)/1000000;
        Log.d(TAG,"Processing time: " + duration + "ms");
    }

    public Mat getMat()
    {
        return mFrame;
    }

    public native void processFrame(long nativeObjAddr);
}
