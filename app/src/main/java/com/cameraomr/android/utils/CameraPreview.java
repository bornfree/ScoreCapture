package com.cameraomr.android.utils;



import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CameraPreview extends SurfaceView
        implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private static final int FOCUS_AREA_SIZE= 200;

    private SurfaceHolder sh;
    private Camera camera;
    private Camera.PreviewCallback previewCallback;
    private Camera.Size mFrameSize;
    private Camera.Parameters params;

    private OnTouchListener mCameraPreviewTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                focusOnTouch(event);
            }
            return true;
        }
    };

    private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0){
                camera.cancelAutoFocus();
            }
        }
    };

    public CameraPreview(Context context, Camera cm, Camera.PreviewCallback pCallback) {
        super(context);
        camera = cm;
        previewCallback = pCallback;
        int totalFocusingAreas = camera.getParameters().getMaxNumFocusAreas();
        if(totalFocusingAreas > 0)
            setOnTouchListener(mCameraPreviewTouchListener);

        params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        mFrameSize  = sizes.get(closest(sizes, 640, 480));

        sh = getHolder();
        sh.addCallback(this);
        // deprecated but required pre-3.0
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (sh.getSurface() == null) {
            // no preview surface!
            return;
        }

        // Stop preview before changing.
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // Tried to stop non-existent preview
        }

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }

        try {

            // Width is longer
            params.setPreviewSize(mFrameSize.width, mFrameSize.height);
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.setPreviewCallback(previewCallback);
            camera.setPreviewDisplay(sh);
            camera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error restarting preview: " + e.getMessage());
            e.getStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Activity looks after releasing camera preview
    }

    public Camera.Size getFrameSize()
    {
        return mFrameSize;
    }

    public static int closest( List<Camera.Size> sizes , int width , int height ) {
        int best = -1;
        int bestScore = Integer.MAX_VALUE;

        for( int i = 0; i < sizes.size(); i++ ) {
            Camera.Size s = sizes.get(i);

            int dx = s.width-width;
            int dy = s.height-height;

            int score = dx*dx + dy*dy;
            if( score < bestScore ) {
                best = i;
                bestScore = score;
            }
        }

        return best;
    }

    private void focusOnTouch(MotionEvent event) {
        if (camera != null ) {

            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0){
                Rect rect = calculateFocusArea(event.getX(), event.getY());

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                camera.setParameters(parameters);
                camera.autoFocus(myAutoFocusCallback);
            }else {
                camera.autoFocus(myAutoFocusCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / this.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / this.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }

}