package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.ColorGrid;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;

/**
 * Autonomous OpMode created separately for two reasons, easier to setup and faster to run
 */
public class RobotAuto extends OpMode {

    Components components;
    InitComp initComp;
    ReturnValues returnValues;
    ColorGrid colorGrid;

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
        InitComp initComp = new InitComp(hardwareMap, telemetry, components);
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
        if (Aliases.cameraMap.get("color_grid") != null){
            colorGrid = (ColorGrid)Aliases.cameraMap.get("color_grid");
        }
    }

    @Override
    public void loop() {
        //core loop
        if (colorGrid != null) {
            telemetry.addData("colorgrid", colorGrid.getCell(1, 1).toString());
        }
    }

    @Override
    public void stop() {
        //stop function
        if (colorGrid != null) {
            colorGrid.closeGrid();
        }
    }

}
