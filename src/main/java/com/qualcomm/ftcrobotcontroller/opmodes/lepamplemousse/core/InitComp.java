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
        Integer count = 0;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("enabled").equals(true)) count++;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("enabled").equals(true)) count++;
        Core.motor = new DcMotor[count]; // Obviously pull from config.
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
        return ReturnValues.SUCCESS;
    }

    public Components getComponents(){
        return components;
    }

    /*
    Not tested, yet
        Core.motor[1] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("hardware_map_name").toString());
     */

    public void Run(){

    }
}
