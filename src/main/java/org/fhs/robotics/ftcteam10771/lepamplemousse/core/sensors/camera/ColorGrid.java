package org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera;

/**
 * Accepts one or two arguments to determine a grid the camera should average the
 * colors for and return.
 */
public class ColorGrid extends Camera{

    private GridValue[][] grid = null;

    public ColorGrid(int side){
        this(side, side);
    }

    public ColorGrid(int width, int height){
        grid = new GridValue[width][height];
        //calculate the nearest downsampling ratio for the camera
        int GLS = (width > height) ? width : height;
        int CLS = (cameraFullX < cameraFullY) ? cameraFullX : cameraFullY;
    }


    public class GridValue{

    }
}
