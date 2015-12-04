package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;


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
    static final int moduleNumber = 4;
    static final int motorNumber = 2;
    static final int servoNumber = 2;
    public final int touchSensorNum = 1;
    static final int lightSensorNum = 1;
    static final int irSensorNum = 1;
    static final int gyroNum = 1;
    static final int accelerometerNum = 1;
    static final int colorSenseNum = 1;

    //TODO: Make a new file for insatiating HardwareInfo Objects
    //TODO: Figure out a way to obtain serial numbers of modules
}
