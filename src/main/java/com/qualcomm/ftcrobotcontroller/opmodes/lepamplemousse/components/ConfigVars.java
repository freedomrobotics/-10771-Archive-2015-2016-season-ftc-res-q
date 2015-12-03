package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.SerialNumber;

/**
 * Contains all the variables used to input data from configuration files
 * and to initialize all class objects of all robot hardware parts
 * Created by joelv on 12/2/2015.
 */
public class ConfigVars {

    ConfigVars(){
        //Default Constructor
    }

    //Tells how many components
    private static final int moduleNumber = 4;
    private static final int sensorNumber = 6;
    private static final int motorNumber = 2;

    //Module serial numbers
    public static SerialNumber modSerials[] = new SerialNumber[moduleNumber];
    public static SerialNumber interfaceSerial = modSerials[0];
    public static SerialNumber legacySerial = modSerials[1];
    public static SerialNumber motorConSerial = modSerials[2];
    public static SerialNumber servoConSerial = modSerials[3];

    //Hardware model enumerations
    public enum MODEL{
        MODERN_ROBOTICS, NXT, ADAFRUIT
    }
    MODEL moduleModels[] = new MODEL[moduleNumber];
    MODEL sensorModles[]  = new MODEL[sensorNumber];
    MODEL motorModels[] = new MODEL[motorNumber];

    //Port numbers of devices
    static int portNumbers[] = new int[motorNumber+sensorNumber];
    static int touchPort = portNumbers[0];
    static int lightPort = portNumbers[1];
    static int irPort = portNumbers[2];
    static int gryoPort = portNumbers[3];
    static int acceleroPort = portNumbers[4];
    static int colorPort = portNumbers[5];
}
