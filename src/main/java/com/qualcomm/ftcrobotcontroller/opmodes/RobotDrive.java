package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
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
    }

    @Override
    public void start(){
        Components components = new Components(telemetry);
        Variables variables = new Variables(telemetry);

        components.load();
        variables.load();
        telemetry.addData("dc_motor_load_test", components.retrieve("dc_motors"));
        telemetry.addData("drivetrain_load_test", variables.retrieve("drivetrain"));
        telemetry.addData("encoder_load_test", variables.retrieve("encoder"));
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
