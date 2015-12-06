package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.InitComp;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.StartValues;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes.Controlled;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Le Pamplemousse DRIVE!
 *
 * The core robot framework. This file should rarely be edited.
 * This is the regular drive
 */
public class RobotDrive extends OpMode{

    Components components = null;
    Controlled controlled = null;

    public RobotDrive(){
        //Constructor
    }

    @Override
    public void init(){
        //initializer
        InitComp initComp = new InitComp(hardwareMap, telemetry);
        components = initComp.getComponents();
        if (initComp.initialize().equals(ReturnValues.SUCCESS)){
            //idk
        }
    }

    @Override
    public void start(){
        //set default values
        StartValues startValues = new StartValues(telemetry);
        controlled = new Controlled(gamepad1, gamepad2);
    }

    @Override
    public void loop(){
        //core loop
        controlled.loop();
    }

    @Override
    public void stop(){
        //stop function
    }

}
