package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.ControllersInit;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.robotcore.hardware.Gamepad;

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
        Aliases.motorMap.get("drive_left").setPower(controls.drivetrain_left);
        Aliases.motorMap.get("drive_right").setPower(controls.drivetrain_right);
    }
}
