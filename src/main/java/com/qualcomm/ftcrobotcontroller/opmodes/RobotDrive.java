package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Controllers;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.ControllersInit;
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
    ReturnValues returnValues;
    Controllers controllerConfig = null;
    ControllersInit controls = null;
    Variables variables = null;
    boolean reset_config;

    public RobotDrive(){
        //Constructor
    }

    @Override
    public void init(){
        //initializer
        //Check to see if the call to reset from the controller has been called
        reset_config = false;
        if (gamepad1.start && gamepad1.back){
            reset_config = true;
        }

        //load the components object and check for existence and reset
        components = new Components(telemetry);
        if (!components.load() || reset_config){
            components.create();
        }

        // initialize all the components
        // and run the checks
        InitComp initComp = new InitComp(hardwareMap, telemetry, components);
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
        //load the variables object and check for existence and reset
        variables = new Variables(telemetry);
        if (!variables.load() || reset_config){
            variables.create();
        }

        //Load all the variables from the configuration
        StartValues startValues = new StartValues(telemetry, variables);

        //load the controller mappins config and check for existence and reset
        controllerConfig = new Controllers(telemetry);
        if (!controllerConfig.load() || reset_config){
            controllerConfig.create();
        }

        //Initialize the controller aliases for dynamic mapping
        controls = new ControllersInit(gamepad1, gamepad2, controllerConfig);
        //insert init code here
        controlled = new Controlled(controls);
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
