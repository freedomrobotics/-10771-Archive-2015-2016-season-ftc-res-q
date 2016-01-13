package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Controllers;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.ControllersInit;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.modes.Autonomous;
import org.fhs.robotics.ftcteam10771.lepamplemousse.modes.Controlled;

/**
 * Autonomous OpMode created separately for two reasons, easier to setup and faster to run
 */
public class RobotAuto extends OpMode {

    Components components = null;
    Autonomous autonomous = null;
    ReturnValues returnValues;
    Variables variables = null;
    InitComp initComp = null;
    boolean reset_config;

    public RobotAuto() {
        //Constructor
    }

    @Override
    public void init() {
        //initializer
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
    }

    @Override
    public void loop() {
        //core loop
        autonomous.loop();
    }

    @Override
    public void stop() {
        //stop function
        //run cleanup code and clear everything
        autonomous.cleanup();
        //plus others
        initComp.cleanUp();
    }

}
