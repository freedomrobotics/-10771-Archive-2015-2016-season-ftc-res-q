package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

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

    public static Telemetry telemetry;
    public static DeviceInterfaceModule mainInterface;
    public static LegacyModule legacyModule;
    public static DcMotorController motorControl;
    public static ServoController servoControl;
    public static TouchSensor touchSensor[] = new TouchSensor[ConfigVars.touchSensorNum];
    public static LightSensor lightSensor[] = new LightSensor[ConfigVars.lightSensorNum];
    public static ColorSensor colorSensor[] = new ColorSensor[ConfigVars.colorSenseNum];
    public static IrSeekerSensor irSeeker[] = new IrSeekerSensor[ConfigVars.irSensorNum];
    public static GyroSensor gyro[] = new GyroSensor[ConfigVars.gyroNum];
    public static AccelerationSensor accelerometer[] = new AccelerationSensor[ConfigVars.accelerometerNum];
    public static DcMotor motor[] = new DcMotor[ConfigVars.motorNumber];
    public static Servo servo[] = new Servo[ConfigVars.servoNumber];

    /*
      TODO: Put more comments and documentations
      TODO: Make class file reusable for any robot of any model
     */
}


