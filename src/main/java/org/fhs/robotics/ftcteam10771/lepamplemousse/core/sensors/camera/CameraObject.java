package org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.qualcomm.ftcrobotcontroller.R;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Camera Object. Used to create camera and pull a preview and image form it.
 * Created by Adam Li on 12/27/2015.
 *
 *
 * Created with the help of below resources:
 * https://github.com/cheer4ftc/OpModeCamera
 * http://developer.android.com/guide/topics/media/camera.html
 * http://stackoverflow.com/questions/3841122/android-camera-preview-is-sideways
 * http://stackoverflow.com/questions/18149964/best-use-of-handlerthread-over-other-similar-classes/19154438#19154438
 */
// TODO: 12/29/2015 Javadocs 
public class CameraObject {

    private static final String TAG = "CameraDebug";
    private static android.hardware.Camera camera = null;
    private static CameraPreview cameraPreview = null;
    Context context;
    int downSample;
    View rootView;
    Activity activity;
    FrameLayout cameraPreviewLayout;
    public CameraData cameraData = new CameraData();
    //public int debug = 0;
    private android.hardware.Camera.PreviewCallback previewCallback = new android.hardware.Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
            try {
                android.hardware.Camera.Parameters parameters = camera.getParameters();
                cameraData.setWidth(parameters.getPreviewSize().width);
                cameraData.setHeight(parameters.getPreviewSize().height);
                cameraData.setYuvImage(new YuvImage(data, ImageFormat.NV21, cameraData.getWidth(), cameraData.getHeight(), null));
                //debug++;
            } catch (Exception e) {

            }
        }
    };
    private CameraHandlerThread cameraHandlerThread = null;

    //USE FTC>APPCONTEXT
    public CameraObject(Context context, Downsample downSample){
        this.context = context;
        this.downSample = downSample.getValue();
        rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        this.activity = (Activity) context;
    }

    /**
     * From the android tutorial, apparently a safe way to get the camera object
     * @return android.hardware.Camera
     */
    public static android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Method to initialize the class's Camera object (which is static)
     * @return A ReturnValues value
     */
    public ReturnValues initCamera(){
        if (checkCameraHardware()){
            //Camera exists on device and is not defined yet
            if (camera == null){
                cameraHandlerThread = new CameraHandlerThread();

                synchronized (cameraHandlerThread) {
                    cameraHandlerThread.openCamera();
                }
                //Camera exists on device, but for whatever reason, it could not open
                if (camera == null){
                    return ReturnValues.CAMERA_FAILED_TO_OPEN;
                }
                //Camera exists on device and was opened, so create preview
                createPreview();
                return ReturnValues.SUCCESS;
            }
            //Camera exists on device and was defined already, so create preview (maybe)
            createPreview();
            return ReturnValues.CAMERA_ALREADY_OPEN;
        }
        //Camera does not exist
        return ReturnValues.CAMERA_DOESNT_EXIST;
    }

    private void createPreview(){
        if(cameraPreview == null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cameraPreview = new CameraPreview(camera, previewCallback);
                    cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
                    cameraPreviewLayout.addView(cameraPreview);

                }
            });
        }
    }

    /**
     * Method to stop camera. MUST BE CALLED OR ELSE.
     */
    public void stopCamera() {
        if (camera != null) {
            if (cameraPreview != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cameraPreviewLayout.removeAllViews();

                    }
                });
                cameraPreview = null;
            }
            camera.stopPreview();
            camera.setPreviewCallback(null);
            if(camera != null) {
                camera.release();
            }
            camera = null;
        }
    }

    /**
     * Check if this device has a camera, also from the android tutorial
     * @return Camera's existance
     */
    private boolean checkCameraHardware() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Based on the code from the android camera tutorial
     */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
        private android.hardware.Camera camera;
        private android.hardware.Camera.PreviewCallback previewCallback = null;

        public CameraPreview(android.hardware.Camera camera, android.hardware.Camera.PreviewCallback previewCallback) {
            super(context);
            this.camera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            holder = getHolder();
            holder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            this.previewCallback = previewCallback;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.

            //Check because if user spams, the thing crashes since surface might be removed before this is called.
            //Doesn't stop the crash, just makes it less likely.
            if (this.holder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            android.hardware.Camera.Parameters parameters = camera.getParameters();
            Display display = activity.getWindowManager().getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                cameraData.setOrientation(Orientation.PORTRAIT);
            }

            if(display.getRotation() == Surface.ROTATION_90)
            {
                cameraData.setOrientation(Orientation.LANDSCAPE);
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                cameraData.setOrientation(Orientation.PORTRAIT_FLIPPED);
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                cameraData.setOrientation(Orientation.LANDSCAPE_FLIPPED);
            }

            // start preview with new settings
            camera.setParameters(parameters);
            camera.setPreviewCallback(previewCallback);
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        // Adapted from http://stackoverflow.com/questions/3841122/android-camera-preview-is-sideways
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (this.holder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                camera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            android.hardware.Camera.Parameters parameters = camera.getParameters();
            // FIXME: 12/28/2015 Doesn't reorient the camera, but doesn't matter for now
            Display display = activity.getWindowManager().getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                cameraData.setOrientation(Orientation.PORTRAIT);
            }

            if(display.getRotation() == Surface.ROTATION_90)
            {
                cameraData.setOrientation(Orientation.LANDSCAPE);
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                cameraData.setOrientation(Orientation.PORTRAIT_FLIPPED);
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                cameraData.setOrientation(Orientation.LANDSCAPE_FLIPPED);
            }

            // start preview with new settings
            camera.setParameters(parameters);
            camera.setPreviewCallback(previewCallback);
            try {
                camera.setPreviewDisplay(this.holder);
                camera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }

        public android.hardware.Camera.PreviewCallback getPreview(){
            return previewCallback;
        }
    }

    //Adapted from http://stackoverflow.com/questions/18149964/best-use-of-handlerthread-over-other-similar-classes/19154438#19154438
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
                    camera = getCameraInstance();

                    camera.setPreviewCallback(previewCallback);

                    android.hardware.Camera.Parameters parameters = camera.getParameters();

                    cameraData.setWidth(parameters.getPreviewSize().width / downSample);
                    cameraData.setHeight(parameters.getPreviewSize().height / downSample);
                    parameters.setPreviewSize(cameraData.getWidth(), cameraData.getHeight());

                    camera.setParameters(parameters);

                    notifyCameraOpened();
                }
            });
            try {
                wait();
            }
            catch (InterruptedException e) {
                Log.w(TAG, "Camera open wait was interrupted");
            }
        }
    }

    //Something
    public class CameraData{
        private int w, h;
        private YuvImage yuvImage;
        private Orientation o = Orientation.PORTRAIT;

        public int getWidth(){
            return w;
        }

        public void setWidth(int w){
            this.w = w;
        }

        public int getHeight(){
            return h;
        }

        public void setHeight(int h){
            this.h = h;
        }

        public YuvImage getYuvImage(){
            return yuvImage;
        }

        public void setYuvImage(YuvImage data){
            this.yuvImage = data;
        }

        public Bitmap getRgbImage(){
            Bitmap rgbImage;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, w, h), 0, out);
            byte[] imageBytes = out.toByteArray();

            BitmapFactory.Options opt;
            opt = new BitmapFactory.Options();
            opt.inSampleSize = downSample;

            rgbImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);
            return rgbImage;
        }

        public void setOrientation(Orientation o){
            if(o == Orientation.PORTRAIT)
            {
                camera.setDisplayOrientation(90);
            }

            if(o == Orientation.LANDSCAPE)
            {
                camera.setDisplayOrientation(0);
            }

            if(o == Orientation.PORTRAIT_FLIPPED)
            {
                camera.setDisplayOrientation(270);
            }

            if(o == Orientation.LANDSCAPE_FLIPPED)
            {
                camera.setDisplayOrientation(180);
            }
            this.o = o;
        }

        public Orientation getOrientation(){
            return o;
        }
    }

    public enum Orientation{
        PORTRAIT,
        LANDSCAPE,
        LANDSCAPE_FLIPPED,
        PORTRAIT_FLIPPED
    }

    public enum Downsample{
        FULL(1),
        HALF(2),
        FOURTH(4),
        EIGHTH(8),
        SIXTEENTH(16);

        private final int value;

        Downsample(final int value){
            this.value = value;
        }

        public int getValue() { return value; }
    }
}
