package com.qualcomm.ftcrobotcontroller.opmodes;

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

    @Override
    public void loop(){
        //core loop
    }

    @Override
    public void stop(){
        //stop function
    }

}
