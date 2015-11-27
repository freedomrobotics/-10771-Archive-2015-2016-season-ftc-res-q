package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.InitComp;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.StartValues;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Autonomous OpMode created separately for two reasons, easier to setup and faster to run
 */
public class RobotAuto extends OpMode {

    public RobotAuto(){
        //Constructor
    }

    @Override
    public void init(){
        //initializer
        InitComp.Run();

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