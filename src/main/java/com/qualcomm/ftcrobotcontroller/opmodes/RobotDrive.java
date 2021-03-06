package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Controllers;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.ControllersInit;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.modes.Controlled;

/**
 * Le Pamplemousse DRIVE!
 * <p/>
 * The core robot framework. This file should rarely be edited.
 * This is the regular drive
 */
public class RobotDrive extends OpMode {

    Components components = null;
    Controlled controlled = null;
    ReturnValues returnValues;
    Controllers controllerConfig = null;
    ControllersInit controls = null;
    Variables variables = null;
    InitComp initComp = null;
    boolean reset_config;

    public RobotDrive() {
        //Constructor
    }

    @Override
    public void init() {
        //initializer
        //Check to see if the call to reset from the controller has been called
        /* didn't work so change to add options to each individual yml file
        if (gamepad1.start && gamepad1.right_bumper) {
            telemetry.addData("Reset", "Wipe Configs? A: Confirm; B: Cancel");
            while (!gamepad1.a || !gamepad1.b) {
                if (gamepad1.a) {
                    telemetry.addData("Reset", "Wiping Configs...");
                    reset_config = true;
                }
                telemetry.addData("Reset", "Cancelled");
                break;
            }
        }*/

        //load the components object and check for existence
        components = new Components(telemetry);
        if (!components.load()) {
            components.create();
        }

        // initialize all the components
        // and run the checks
        initComp = new InitComp(hardwareMap, telemetry, components);
        if ((returnValues = initComp.initialize()) != ReturnValues.SUCCESS) {
            if (returnValues == ReturnValues.MOTOR_NOT_INIT) {
                telemetry.addData("ERROR", "Motors Failed to Initialize");
            } else if (returnValues == ReturnValues.SERVO_NOT_INIT) {
                telemetry.addData("ERROR", "Servos Failed to Initialize");
            } else {
                telemetry.addData("ERROR", "Something wrong happened!");
            }
        }
    }

    @Override
    public void start() {
        //set default values
        //load the variables object and check for existence
        variables = new Variables(telemetry);
        if (!variables.load()) {
            variables.create();
        }

        //Load all the variables from the configuration
        StartValues startValues = new StartValues(variables, telemetry);
        startValues.initialize();

        //load the controller mappins config and check for existence
        controllerConfig = new Controllers(telemetry);
        if (!controllerConfig.load()) {
            controllerConfig.create();
        }

        //Initialize the controller aliases for dynamic mapping
        controls = new ControllersInit(gamepad1, gamepad2, controllerConfig);
        //insert init code here
        controlled = new Controlled(controls, startValues, telemetry);
        controls.initialize();
    }

    @Override
    public void loop() {
        //core loop
        controlled.loop();
    }

    @Override
    public void stop() {
        //stop function
        //run cleanup code and clear everything
        controlled.cleanup();
        //plus others
        initComp.cleanUp();
    }

}
