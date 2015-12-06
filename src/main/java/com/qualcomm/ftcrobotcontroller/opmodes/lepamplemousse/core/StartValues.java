package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

/**
 * Initial reset values. Should include the default/starting values loaded
 * from the config or other places and set all variables to such.
 */
public class StartValues {
    public StartValues(HardwareMap hardwareMap){
        //Core.motor[1] = hardwareMap.dcMotor.get(((Map<String, Object>)((Map<String, Object>)components.retrieve("dc_motors")).get("motor1")).get("hardware_map_name").toString());
    }
}
