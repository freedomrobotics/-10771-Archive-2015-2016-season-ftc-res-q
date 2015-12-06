package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Create the actual references to the components based on the configuration
 */
public class InitComp {

    HardwareMap hardwareMap = null;

    Components components = null;

    public InitComp (HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        components = new Components(telemetry);
        components.load();
    }

    /*
    Not tested, yet
        Core.motor[1] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("hardware_map_name").toString());
     */

    public void Run(){

    }
}
