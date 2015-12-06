package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.InitComp;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.StartValues;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Le Pamplemousse DRIVE!
 *
 * The core robot framework. This file should rarely be edited.
 * This is the regular drive
 */
public class RobotDrive extends OpMode{

    public RobotDrive(){
        //Constructor
    }

    @Override
    public void init(){
        //initializer
        InitComp initComp = new InitComp(hardwareMap, telemetry);

    }

    @Override
    public void start(){
        //set default values
        StartValues.Run();

    }

    float motorPower;

    @Override
    public void loop(){
        //Example of assigning motor power
        Core.motor[0].setPower(motorPower);
        //core loop
    }

    @Override
    public void stop(){
        //stop function
    }

}
