package com.cameraomr.android;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cameraomr.android.classes.Frame;
import com.cameraomr.android.classes.Key;
import com.cameraomr.android.classes.Section;
import com.cameraomr.android.classes.SectionNDK;
import com.cameraomr.android.classes.Template;
import com.cameraomr.android.db.KeysDataSource;
import com.cameraomr.android.db.SectionsDataSource;
import com.cameraomr.android.db.TemplatesDataSource;
import com.cameraomr.android.utils.CameraPreview;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private final static String TAG = "Camera Activity";
    private CameraHandlerThread mThread = null;
    private Camera.Size mFrameSize;
    private FrameLayout mContainer;
    private int mContainerWidth = 0;
    private int mContainerHeight = 0;
    private ImageView mMatDebug;
    private TextView mScore;
    private CheckBox mDebugPerspective, mDebugSection1, mDebugSection2;
    private KeysDataSource keydatasource;
    private TemplatesDataSource templatesdatasource;
    private SectionsDataSource sectionsdatasource;
    private String mKeyId;
    private int previousScore = 0;
    private ToneGenerator toneG;

    static {
        if (!OpenCVLoader.initDebug()) {
            //Handle error
        }
        else {
            Log.d(TAG, "Here");
            System.loadLibrary("CameraOMRNative");
        }
    }

    public native void setTemplateProperties(int width, int height);
    public native void setSections(SectionNDK[] sections);

    private ViewTreeObserver.OnGlobalLayoutListener previewSizeAdjuster = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < 16)
                mContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            else
                mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            mContainerWidth  = mContainer.getMeasuredWidth();
            mContainerHeight = mContainer.getMeasuredHeight();
            FrameLayout frame = (FrameLayout) findViewById(R.id.fullscreen_content);
            FrameLayout markersFrame = (FrameLayout) findViewById(R.id.markers_frame);
            FrameLayout remainingFrame = (FrameLayout) findViewById(R.id.remainingPortion);
            if(mFrameSize.width > 0 && mFrameSize.height > 0)
            {
                int newHeight = (int) (mContainerHeight*1.0*mFrameSize.height)/mFrameSize.width;
                FrameLayout.LayoutParams fparams = new FrameLayout.LayoutParams(mContainerWidth, newHeight);
                frame.setLayoutParams(fparams);
                markersFrame.setLayoutParams(fparams);
                fparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mContainerHeight - newHeight);
                fparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                remainingFrame.setLayoutParams(fparams);
            }
        }
    };

    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Frame frame = new Frame(data, mFrameSize);
            int debugIndex = getDebugIndex();
            int score = frame.process(debugIndex);
            if(score != previousScore)
            {
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100);
                previousScore = score;
            }
            frame.printProcessingTime();
            useResults(frame, debugIndex, score);
        }
    };

    public int getDebugIndex()
    {
        int db = 0;
        if(mDebugPerspective.isChecked())
            db = 1;
        if(mDebugSection1.isChecked())
            db = 2;
        if(mDebugSection2.isChecked())
            db = 3;
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mMatDebug = (ImageView) findViewById(R.id.matDebug);
        mScore = (TextView) findViewById(R.id.score);
        mDebugPerspective = (CheckBox) findViewById(R.id.debugPerspective);
        mDebugSection1 = (CheckBox) findViewById(R.id.debugSection1);
        mDebugSection2 = (CheckBox) findViewById(R.id.debugSection2);
        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 60);

        keydatasource = new KeysDataSource(this);
        templatesdatasource = new TemplatesDataSource(this);
        sectionsdatasource = new SectionsDataSource(this);

        mKeyId = getIntent().getStringExtra("key_id");

        mContainer = (FrameLayout) findViewById(R.id.container);
        ViewTreeObserver vto = mContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(previewSizeAdjuster);


    }

    @Override
    protected void onResume() {
        super.onResume();
        keydatasource.open();
        templatesdatasource.open();
        sectionsdatasource.open();
        setKeyTemplateSections(mKeyId);
        if(mCamera == null)
        {
            newOpenCamera();
            if(mCamera != null)
            {
                mPreview = new CameraPreview(this, mCamera, mPreviewCallback);
                mFrameSize = mPreview.getFrameSize();
                FrameLayout frame = (FrameLayout) findViewById(R.id.fullscreen_content);

//                FrameLayout.LayoutParams fparams = new FrameLayout.LayoutParams()
//                frame.setLayoutParams();
                frame.addView(mPreview);

            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FrameLayout frame = (FrameLayout) findViewById(R.id.fullscreen_content);
        frame.removeView(mPreview);
        releaseCamera();
        keydatasource.close();
        templatesdatasource.close();
        sectionsdatasource.close();

    }

    public void setKeyTemplateSections(String keyId)
    {


        Key k = keydatasource.getKey(keyId);
        if(k != null){

            Template template = templatesdatasource.getTemplate(Long.toString(k.getTemplate_id()));
            List<Section> sections = template.getSections();
            SectionNDK[] sectionNDKs = new SectionNDK[sections.size()];

            for(int i=0; i< sections.size(); i++)
            {

                Section section = sections.get(i);
                String answerString = k.getAnswers().substring(i * 2 * section.getNum_answers(), (i+1)* 2 * section.getNum_answers() );
                answerString = answerString.replace(";","");

                SectionNDK sectionNDK = new SectionNDK(section, answerString);
                sectionNDKs[i] = sectionNDK;
            }
            setTemplateProperties(template.getWidth(), template.getHeight());
            setSections(sectionNDKs);
        }

    }

    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }

    private class CameraHandlerThread extends HandlerThread {
        Handler mHandler = null;

        CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(safeCameraOpen() == true)
                        notifyCameraOpened();
                }
            });
            try {
                wait();
            }
            catch (InterruptedException e) {
                Log.w(TAG, "wait was interrupted");
            }
        }
    }

    private boolean safeCameraOpen()
    {
        boolean qOpened = false;
        releaseCamera();
        mCamera = getCameraInstance();

        qOpened = (mCamera != null);
        return qOpened;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.setOnTouchListener(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mPreview = null;
        }
    }

    public void useResults(final Frame frame, final int debugMode, final int score) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mScore.setText("Score: "+ score);
                if(debugMode == 0)
                {
                    mMatDebug.setImageBitmap(null);
                    return;
                }

                Bitmap resultBitmap = Bitmap.createBitmap(frame.getMat().cols(), frame.getMat().rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(frame.getMat(), resultBitmap);
                mMatDebug.setImageBitmap(resultBitmap);
            }

        });
    }
}
