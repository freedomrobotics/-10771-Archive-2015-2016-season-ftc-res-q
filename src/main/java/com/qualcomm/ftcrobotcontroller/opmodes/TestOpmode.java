package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.CameraObject;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;

/**
 * Test OpMode
 */
public class TestOpmode extends OpMode {

    CameraObject cameraObject;
    //CameraObject cameraObject2;
    int counter = 0;

    public TestOpmode() {
        //Constructor
    }

    @Override
    public void init() {
        //initializer
        cameraObject = new CameraObject(hardwareMap.appContext, CameraObject.Downsample.FOURTH);
        //cameraObject2 = new CameraObject(hardwareMap.appContext, CameraObject.Downsample.HALF);

    }

    @Override
    public void start() {
        //set default values
        ReturnValues returned = cameraObject.initCamera();
        if (returned != ReturnValues.SUCCESS){
            telemetry.addData("Error", returned);
        }

        //returned = cameraObject2.initCamera();
        //if (returned != ReturnValues.SUCCESS){
        //    telemetry.addData("Error2", returned);
        //}
    }

    @Override
    public void loop() {
        //core loop
        telemetry.addData("CameraX", cameraObject.cameraData.getWidth());
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
        counter++;
    }

    @Override
    public void stop() {
        //stop function
        cameraObject.stopCamera();
        //cameraObject2.stopCamera();
    }

}
