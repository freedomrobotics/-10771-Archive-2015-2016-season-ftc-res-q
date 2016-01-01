package org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Static;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Accepts one or two arguments to determine a grid the camera should average the
 * colors for and return.
 * <p/>
 * Construct with a size of 1 to somewhat mimic a color sensor. (not recommended;
 * recommended instead is to construct with some grid and take the center cell as
 * the color sensor)
 */
// TODO: 1/1/2016 Organization, style, and javadocs. I also understand some of my numbers might
// TODO:          be backwards and that needs to be accounted for as well.
public class ColorGrid extends Camera{

    private GridValue[][] grid = null;
    private int width, height;

    public int counter = 0;

    ScheduledExecutorService executorService;
    Runnable gridUpdate = new Runnable() {
        public void run() {
            updateGrid();
        }
    };

    /*Timer timer = new Timer();
    TimerTask gridUpdate = new TimerTask() {
        @Override
        public void run() {
            updateGrid();
        }
    };*/

    public ColorGrid(int side, float refreshRate, Context context){
        this(side, side, refreshRate, context);
    }

    public ColorGrid(int width, int height, float refreshRate, Context context){
        grid = new GridValue[width][height];
        for (int w = 0; w < width; w++){
            for (int h = 0; h < height; h++){
                grid[w][h] = new GridValue();
            }
        }
        this.width = width;
        this.height = height;
        //calculate the nearest downsampling ratio for the camera and set it to the second nearest
        // (unless it's a really large ratio)
        CameraObject.Downsample ds;
        int GLS = (width > height) ? width : height;
        int CLS = (cameraFullX < cameraFullY) ? cameraFullX : cameraFullY;
        int w = CLS/GLS;
        int c = (int) Math.floor(Math.log(w)/Math.log(2)) - 1;
        switch (c){
            case 0:
                ds = CameraObject.Downsample.FULL;
                break;
            case 1:
                ds = CameraObject.Downsample.HALF;
                break;
            case 2:
                ds = CameraObject.Downsample.FOURTH;
                break;
            case 3:
                ds = CameraObject.Downsample.EIGHTH;
                break;
            case 4:
                ds = CameraObject.Downsample.SIXTEENTH;
                break;
            case 5:
                ds = CameraObject.Downsample.BY32;
                break;
            case 6:
                ds = CameraObject.Downsample.BY64;
                break;
            default:
                if (c < 0) {
                    ds = CameraObject.Downsample.FULL;
                    break;
                }
                ds = CameraObject.Downsample.BY128;
                break;
        }
        //initialize the camera with the downsample.
        //Bad style ignoring the return.
        createCameraObject(context, ds);
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(gridUpdate, (long)((1.0f / refreshRate) / Static.nanoSecondsToSeconds), (long)((1.0f / refreshRate) / Static.nanoSecondsToSeconds), TimeUnit.NANOSECONDS);
        //timer.scheduleAtFixedRate(gridUpdate, 2000, (long)((1.0f / refreshRate) * 1000.0f));

    }

    private void updateGrid(){
        Bitmap preview = cameraObject.cameraData.getRgbImage();
        if (preview == null) return;
        int cellX = (preview.getWidth() / width) - 1;
        int cellY = (preview.getHeight() / width) - 1;
        for (int w = 0; w < width; w++){
            for (int h = 0; h < height; h++){
                grid[w][h].setColor(getAverageColor(Bitmap.createBitmap(preview, w*cellX, h*cellY, cellX, cellY)));
            }
        }
        //counter++;
    }

    //http://stackoverflow.com/a/25071613
    private int getAverageColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++)
            {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public GridValue getCell(int x, int y){
        return grid[x][y];
    }

    public void closeGrid(){
        executorService.shutdown();
        //timer.cancel();
        //timer.purge();
        stopCamera();
    }

    public class GridValue{
        private int r, g, b;

        GridValue(){
            r = 0;
            g = 0;
            b = 0;
        }

        protected void setColor(int c){
            r = Color.red(c);
            g = Color.green(c);
            b = Color.blue(c);
        }
        public int red(){
            return r;
        }
        public int green() {
            return g;
        }
        public int blue() {
            return b;
        }
        public int colorInt(){
            return Color.rgb(r, g, b);
        }
    }
}
