package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.InitComp;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.StartValues;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes.Controlled;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Le Pamplemousse DRIVE!
 *
 * The core robot framework. This file should rarely be edited.
 * This is the regular drive
 */
public class RobotDrive extends OpMode{

    Components components = null;
    Controlled controlled = null;
    ReturnValues returnValues;

    public RobotDrive(){
        //Constructor
    }

    @Override
    public void init(){
        //initializer
        InitComp initComp = new InitComp(hardwareMap, telemetry);
        components = initComp.getComponents();

        if ((returnValues = initComp.initialize()) != ReturnValues.SUCCESS){
            if (returnValues == ReturnValues.MOTOR_NOT_INIT) {
                telemetry.addData("ERROR", "Motors Failed to Initialize");
            }
            if (returnValues == ReturnValues.SERVO_NOT_INIT) {
                telemetry.addData("ERROR", "Servos Failed to Initialize");
            }
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
