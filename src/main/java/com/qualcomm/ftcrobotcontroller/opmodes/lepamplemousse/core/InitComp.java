package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Map;

/**
 * Create the actual references to the components based on the configuration
 */
public class InitComp {

    HardwareMap hardwareMap = null;

    Components components = null;

    public InitComp (HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        components = new Components(telemetry);
        components.create();
        components.load();
        telemetry.addData("1", components.retrieve("dc_motors"));
        telemetry.addData("2", ((Map)((Map) components.retrieve("dc_motors")).get("motor1")).get("map_name").toString());
        //Core.motor[0] = hardwareMap.dcMotor.get(((Map)((Map) components.retrieve("dc_motors")).get("motor1")).get("map_name").toString());
    }

    /*
    Not tested, yet
        Core.motor[1] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("hardware_map_name").toString());
     */
}
