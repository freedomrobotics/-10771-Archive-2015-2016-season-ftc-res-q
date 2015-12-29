package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import android.hardware.Camera;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.HashMap;
import java.util.Map;

/**
 * This file either provides a more descriptive or useful alias, or it
 * creates a Java list or hashmap with names from the configuration files
 * so that it's easier to call upon them and any number of references can
 * be made without loss of too much efficiency
 *
 * todo Implement getters and setter and mutex or mutex-like operations for safe multithreading in the future
 * todo better yet, create an interface or abstract class or like with everything already preset.
 */
public class Aliases {
    /**
     * Motor Map. Values from the Components config file can be used to
     * create an alias reference any motor. Retrieve using ~.motorMap.get("name");
     * Set using ~.motorMap.put("name", motor);
     * Cleared using ~.motorMap.clear("name");
     */
    public static Map<String, DcMotor> motorMap = new HashMap<String, DcMotor>();

    /**
     * Servo Map. Values from the Components config file can be used to
     * create an alias reference any motor. Retrieve using ~.servoMap.get("name");
     * Set using ~.servoMap.put("name", servo);
     * Cleared using ~.servoMap.clear("name");
     */
    public static Map<String, Servo> servoMap = new HashMap<String, Servo>();

    /**
     * Touch Sensor Map. Values from Components config file can be used to
     * create an alias reference any touch sensor.  Retrieve using ~.touchSensorMap.get("name");
     * Set using ~.touchSensorMap.put("name", light sensor);
     * Cleared using ~.touchSensorMap.clear("name");
     */
    public static Map<String, TouchSensor> touchSensorMap = new HashMap<String, TouchSensor>();

    /**
     * Light Sensor Map. Values from Components config file can be used to
     * create an alias reference any light sensor.  Retrieve using ~.lightSensorMap.get("name");
     * Set using ~.lightSensorMap.put("name", light sensor);
     * Cleared using ~.lightSensorMap.clear("name");
     */
    public static Map<String, LightSensor> lightSensorMap = new HashMap<String, LightSensor>();

    /**
     * Color Sensor Map. Values from Components config file can be used to
     * create an alias reference any color sensor.  Retrieve using ~.colorSensorMap.get("name");
     * Set using ~.colorSensorMap.put("name", light sensor);
     * Cleared using ~.colorSensorMap.clear("name");
     */
    public static Map<String, ColorSensor> colorSensorMap = new HashMap<String, ColorSensor>();

    /**
     * IR Seeker Map. Values from Components config file can be used to
     * create an alias reference any ir seeker.  Retrieve using ~.irSeekerMap.get("name");
     * Set using ~.irSeekerMap.put("name", light sensor);
     * Cleared using ~.irSeekerMap.clear("name");
     */
    public static Map<String, IrSeekerSensor> irSeekerMap = new HashMap<String, IrSeekerSensor>();

    /**
     * Gyrometer Map. Values from Components config file can be used to
     * create an alias reference any Gyrometer.  Retrieve using ~.gyrometerMap.get("name");
     * Set using ~.gyrometerMap.put("name", light sensor);
     * Cleared using ~.gyrometerMap.clear("name");
     */
    public static Map<String, GyroSensor> gyrometerMap = new HashMap<String, GyroSensor>();

    /**
     * Accelerometer Map. Values from Components config file can be used to
     * create an alias reference any Accelerometer.  Retrieve using ~.accelerometerMap.get("name");
     * Set using ~.accelerometerMap.put("name", light sensor);
     * Cleared using ~.accelerometerMap.clear("name");
     */
    public static Map<String, AccelerationSensor> accelerometerMap = new HashMap<String, AccelerationSensor>();

    /**
     * Camera Sensor Map. Values from Components config file can be used to
     * create an alias reference any camera sensor.  Retrieve using ~.cameraSensorMap.get("name");
     * Set using ~.cameraMap.put("name", light sensor);
     * Cleared using ~.cameraMap.clear("name");
     */
    public static Map<String, Camera> cameraMap = new HashMap<String, Camera>();
}