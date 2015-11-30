package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import com.qualcomm.hardware.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Core variables with generic names to be aliased later. Since all of the
 * variables/objects/references will be initialized as null, they should only
 * take a little or no memory space. All possible components should be defined
 * here generically.
 */
public class Core {
    public static DcMotor motor[];
    public static Servo servo[];
    public static Telemetry telemetry;
    public static LightSensor lightSensor;
    public static TouchSensor touchSensor;
    public static ColorSensor colorSensor;
    public static AdafruitI2cColorSensor Example1; //I don't know, felt like adding AdaFruit
    public static GyroSensor gyroSensor;
    public static AccelerationSensor accelerationSensor;
    // TODO: 11/26/2015 Add All the components
    /* All declared objects are abstract
       There are two implementations:
            ModernRoboticsI2c
            HiTechnicNxt
     */
    // TODO: 11/30/2015 Determine which is included in our kit

}
