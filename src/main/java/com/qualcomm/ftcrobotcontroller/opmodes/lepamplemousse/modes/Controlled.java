package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.ControllersInit;

/**
 * Driver controlled class
 */
public class Controlled {
    ControllersInit controls;
    public Controlled(ControllersInit controls){
        this.controls = controls;
    }
    //nowhere near fina
    public void loop(){
        Aliases.motorMap.get("drive_left").setPower(controls.getAnalog("drivetrain_left"));
        Aliases.motorMap.get("drive_right").setPower(controls.getAnalog("drivetrain_right"));
    }
}
