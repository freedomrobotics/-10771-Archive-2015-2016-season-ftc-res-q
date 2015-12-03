package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import android.hardware.Camera;
import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Core variables with generic names to be aliased later. Since all of the
 * variables/objects/references will be initialized as null, they should only
 * take a little or no memory space. All possible components should be defined
 * here generically.
 */
public class Core {


    Core(){
        //Default constructor
    }

    public static DcMotor motor[];
    public static Servo servo[];
    public static Telemetry telemetry;

    /*
        Sensors of all models are listed below
    */

    //Touch Sensors
    public static ModernRoboticsDigitalTouchSensor ModRobTouchSensor;
    public static HiTechnicNxtTouchSensor nxtTouchSensor;

    //Light Sensors
    public static ModernRoboticsAnalogOpticalDistanceSensor ModRobODS;
    public static HiTechnicNxtLightSensor nxtLightSensor;

    //Color Sensors
    public static AdafruitI2cColorSensor AdaFruitColorSensor;
    public static ModernRoboticsI2cColorSensor ModRobColorSensor;
    public static HiTechnicNxtColorSensor nxtColorSensor;

    //IR Seeker Sensors
    public static ModernRoboticsI2cIrSeekerSensorV3 ModRobirSeeker;
    public static HiTechnicNxtIrSeekerSensor nxtIRSeeker;

    //Gyrometers
    public static ModernRoboticsI2cGyro ModRobGyro;
    public static HiTechnicNxtGyroSensor NXTgyro;

    //Accelerometers todo 12/1/2015 Determine future use of accelerometer
    public static HiTechnicNxtAccelerationSensor nxtAccelerometer;

    //Camera(possibly)
    public static Camera camera;

    //

    /*TODO: Determine whether and which controller classes to include
      TODO: Put more comments and documentations
      TODO: Make variables to use for robot configuration inputs(serial numbers, port numbers, etc.)
      TODO: Make class file reusable for any robot of any model
     */

}


