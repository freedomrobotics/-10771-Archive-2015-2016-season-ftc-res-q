package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.AtomFunctions;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Controllers;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.ControllersInit;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.modes.Controlled;

/**
 * Autonomous OpMode created separately for two reasons, easier to setup and faster to run
 */
public class RobotAuto extends OpMode {
    Components components = null;
    Controlled controlled = null;
    ReturnValues returnValues;
    Controllers controllerConfig = null;
    ControllersInit controls = null;
    Variables variables = null;
    StartValues values = null;
    InitComp initComp = null;
    AtomFunctions atomFunctions = null;
    boolean reset_config;

    public RobotAuto() {
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
        values = new StartValues(variables, telemetry);
        values.initialize();

        //load the controller mappings config and check for existence
        controllerConfig = new Controllers(telemetry);
        if (!controllerConfig.load()) {
            controllerConfig.create();
        }

        //Initialize the controller aliases for dynamic mapping
        controls = new ControllersInit(gamepad1, gamepad2, controllerConfig);
        //insert init code here
        controls.initialize();
        Aliases.motorMap.get("drive_left").setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        atomFunctions = new AtomFunctions(values);
    }

    @Override
    public void loop() {
        //core loop
    }

    @Override
    public void stop() {
        //stop function
        initComp.cleanUp();
        Aliases.clearAll();
    }


    public void rampClimbMid() {

    }

    public void rampClimbLow() {

    }

    //Distance as centimeters
    public void driveDistance(float distance){

    }

    public void drivePower(float power){
        
    }
}
