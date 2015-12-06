package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;

/**
 * Driver controlled class
 */
public class Controlled {
    public Controlled(){
        Core.motor[0].setPower(1.0);
    }
}
