package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.Camera;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.CameraObject;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.ColorGrid;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Static;

/**
 * Test OpMode
 */
public class TestOpmode extends OpMode {

    //CameraObject cameraObject;
    //CameraObject cameraObject2;
    //int counter = 0;
    Camera colorGrid = null;
    long lastTime;

    public TestOpmode() {
        //Constructor
    }

    @Override
    public void init() {
        //initializer
        //cameraObject = new CameraObject(hardwareMap.appContext, CameraObject.Downsample.FULL);
        //cameraObject2 = new CameraObject(hardwareMap.appContext, CameraObject.Downsample.HALF);

        colorGrid = new ColorGrid(24, 60, hardwareMap.appContext);
        /*
         That's 576 cells to get raw values for! And it add only a millisecond or two extra
         delay in each loop from when I tested with 9 cells (3x3). It's calling values 60 times
         every second; and by putting it on a separate thread, it's not adding much lag at all.
         (when I tested the update on the same thread and under loop, it added ~35 milliseconds
         of delay)
         */
    }

    @Override
    public void start() {
        //set default values
        /*ReturnValues returned = cameraObject.initCamera();
        if (returned != ReturnValues.SUCCESS){
            telemetry.addData("Error", returned);
        }*/

        //returned = cameraObject2.initCamera();
        //if (returned != ReturnValues.SUCCESS){
        //    telemetry.addData("Error2", returned);
        //}
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        int changeTime = (int) (System.currentTimeMillis() - lastTime);
        lastTime = lastTime + changeTime;
        //core loop
        /*telemetry.addData("CameraX", cameraObject.cameraData.getWidth());
        telemetry.addData("CameraY", cameraObject.cameraData.getHeight());
        telemetry.addData("Orientation", cameraObject.cameraData.getOrientation());
        if (counter == 120){
            cameraObject.cameraData.setOrientation(CameraObject.Orientation.PORTRAIT);
        }
        if (counter == 240){
            cameraObject.cameraData.setOrientation(CameraObject.Orientation.LANDSCAPE);
        }
        if (counter == 360){
            cameraObject.cameraData.setOrientation(CameraObject.Orientation.PORTRAIT_FLIPPED);
        }
        if (counter == 480){
            cameraObject.cameraData.setOrientation(CameraObject.Orientation.LANDSCAPE_FLIPPED);
            counter = 0;
        }
        //telemetry.addData("Camera2X", cameraObject2.cameraData.getWidth());
        //telemetry.addData("Camera2Y", cameraObject2.cameraData.getHeight());
        //telemetry.addData("Orientation2", cameraObject2.cameraData.getOrientation());
        counter++;*/
        //((ColorGrid)colorGrid).updateGrid();
        for (int w = 0; w < 3; w++){
            for (int h = 0; h < 3; h++){
                telemetry.addData("grid" + w + "," + h + "red", ((ColorGrid)colorGrid).getCell(w, h).red());
                telemetry.addData("grid" + w + "," + h + "green", ((ColorGrid)colorGrid).getCell(w, h).green());
                telemetry.addData("grid" + w + "," + h + "blue", ((ColorGrid)colorGrid).getCell(w, h).blue());
                telemetry.addData("grid" + w + "," + h + "color", ((ColorGrid) colorGrid).getCell(w, h).colorInt());
            }
        }
        telemetry.addData("debug", changeTime);
    }

    @Override
    public void stop() {
        //stop function
        //cameraObject.stopCamera();
        //cameraObject2.stopCamera();

        ((ColorGrid)colorGrid).closeGrid();
    }

}
