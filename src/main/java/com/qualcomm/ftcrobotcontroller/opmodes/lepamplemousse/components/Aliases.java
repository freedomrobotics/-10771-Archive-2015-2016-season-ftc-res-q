package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

/**
 * This file either provides a more descriptive or useful alias, or it
 * creates a Java list or hashmap with names from the configuration files
 * so that it's easier to call upon them and any number of references can
 * be made without loss of too much efficiency
 */
public class Aliases {
    /**
     * Motor Map. Values from the Components config file can be used to
     * create an alias reference any motor. Retrieve using ~.motorMap.get("name");
     * Set using ~.motorMap.put("name", motor);
     */
    public static Map<String, DcMotor> motorMap = new HashMap<String, DcMotor>();

    /**
     * Servo Map. Values from the Components config file can be used to
     * create an alias reference any motor. Retrieve using ~.servoMap.get("name");
     * Set using ~.servoMap.put("name", servo);
     */
    public static Map<String, Servo> servoMap = new HashMap<String, Servo>();

    /**
     * Light Sensor Map. Values from Components config file can be used to
     * create an alias reference any light sensor.  Retrieve using ~.lightSensor.get("name");
     * Set using ~.motorMap.put("name", light sensor);
     */
    public static Map<String, LightSensor> lightSensorMap = new HashMap<String, LightSensor>();

}
