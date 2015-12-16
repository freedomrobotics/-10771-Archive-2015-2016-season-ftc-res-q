package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Driver controlled class
 */
public class Controlled {
    Gamepad gamepad1 = null;
    Gamepad gamepad2 = null;
    public Controlled(Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    //nowhere near fina
    public void loop(){
        Aliases.motorMap.get("drive_left").setPower(gamepad1.left_stick_y);
        Aliases.motorMap.get("drive_right").setPower(-gamepad1.right_stick_y);
    }
}
