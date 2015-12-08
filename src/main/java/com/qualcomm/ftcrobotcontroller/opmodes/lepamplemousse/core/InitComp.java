package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Map;

/**
 * Create the actual references to the components based on the configuration
 */
public class InitComp {

    HardwareMap hardwareMap = null;

    Components components = null;

    Telemetry telemetry = null;

    public InitComp (HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        components = new Components(telemetry);
        if (!components.load()){
            components.create();
        }
    }

    public ReturnValues initialize(){
        /*
        Integer count = 0;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("enabled").equals(true)) count++;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("enabled").equals(true)) count++;

        //Sorry Joel! You were right to create the references to the array.
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("enabled").equals(true)){
            Core.motor[0] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("enabled").equals(true)){
            Core.motor[1] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor3")).get("enabled").equals(true)){
            Core.motor[2] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor3")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor4")).get("enabled").equals(true)){
            Core.motor[3] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor4")).get("map_name").toString());
        }
        */
        Core.motor = new DcMotor[components.count("dc_motors")]; // Obviously pull from config.
        objectInit("dc_motors", Core.motor);
        return ReturnValues.SUCCESS;
    }

    //TODO: 12/7/2015 make each private variable in Components.java work
    //todo            in a way that makes a unique value for each device type
    //TODO: FIX THE REALLY BAD PARAMETER NAMES JOEL!!!!!
    /**
     * Initializes each individual object type
     * @param deviceType The group of devices to assign
     * @param devices   The array to assign to
     * @return ReturnValues whether or not the method succeeded
     */
    public ReturnValues objectInit(String deviceType, Object devices[]){
        for (int i=0; i<components.count(deviceType); i++){
            int reference = i+1;
            int max = components.determineMax(deviceType);
            while ((!(components.valid(deviceType, reference)) && (reference<max))){
                reference++;
            }
            if (components.valid(deviceType, reference)){
                devices[i] = components.assignObject(deviceType, reference);
            }
            else{
                return ReturnValues.FAIL;
            }
        }
        return ReturnValues.SUCCESS;
    }

    public Components getComponents(){return components;}

    public void Run(){

    }
}
