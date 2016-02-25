package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;

/**
 * Roughly made back up Autonomous mode
 * Created by joelv on 2/17/2016.
 */
public class AutoOpMode extends LinearOpMode {

    //empty default constructor
    AutoOpMode() {
    }

    Components components = null;
    ReturnValues returnValues;
    Variables variables = null;
    StartValues values = null;
    InitComp initComp = null;
    boolean red_alliance = false;       //TODO: create a config setting for alliance
    boolean park = true;                //TODO: create a config setting for floor vs. mountain

    float servo_pos;

    @Override
    /**
     * The main method that runs the robot in autonomous
     */
    public void runOpMode() throws InterruptedException {

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

        //set default values
        //load the variables object and check for existence
        variables = new Variables(telemetry);
        if (!variables.load()) {
            variables.create();
        }

        //Load all the variables from the configuration
        values = new StartValues(variables, telemetry);
        values.initialize();

        //Set up the encoders
        Aliases.motorMap.get("drive_left").setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //Set up the servo
        servoSetup();

        //Press start button
        waitForStart();

        //Left for red, right for blue
        boolean left = red_alliance;
        long turnTime;
        //90 degrees for mountain, 45 for floor
        if (park){turnTime=500;}
        else {turnTime=1000;}

        //Very rough commands for robot movement
        sleep(1000);
        straight(true);
        sleep(4000);
        rotate(left);
        sleep(turnTime);
        straight(true);
        sleep(2000);
        halt();

        //Clean up the code
        cleanUp();
    }

    /**
     * Drives straight
     * @param forward forward or reverse
     */
    public void straight(boolean forward) {
        double power;
        if (forward) {
            power = 1.0;
        } else {
            power = -1.0;
        }
        Aliases.motorMap.get("drive_left").setPower(power);
        Aliases.motorMap.get("drive_right").setPower(power);
    }

    /**
     * Sets a curve path for robot
     * @param left The direction of the curve
     */
    public void curve(boolean left) {
        if (left) {
            Aliases.motorMap.get("drive_left").setPower(0.6);
            Aliases.motorMap.get("drive_right").setPower(1.0);
        } else {
            Aliases.motorMap.get("drive_left").setPower(1.0);
            Aliases.motorMap.get("drive_right").setPower(0.6);
        }
    }

    /**
     * Rotates the robot for a duration of time
     * @param left The direction of rotation
     */
    public void rotate(boolean left) {
        if (left) {
            Aliases.motorMap.get("drive_left").setPower(1.0);
            Aliases.motorMap.get("drive_right").setPower(-1.0);
        } else {
            Aliases.motorMap.get("drive_left").setPower(-1.0);
            Aliases.motorMap.get("drive_right").setPower(1.0);
        }

    }

    /**
     * Stops the robot
     */
    public void halt() {
        Aliases.motorMap.get("drive_left").setPower(0.0);
        Aliases.motorMap.get("drive_right").setPower(0.0);
    }

    /**
     * Clears initialization and aliasing
     */
    private void cleanUp(){
        //it is over
        initComp.cleanUp();
        Aliases.clearAll();
    }

    /**
     * Sets up the servos
     */
    private void servoSetup() {
        if (values.settings("winch").getSettings("left_servo").getBool("reversed")) {
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("winch").getSettings("right_servo").getBool("reversed")) {
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("trigger_arm").getString("side").equals("right")) {
            //Aliases.servoMap.get("arm_trigger").setDirection(Servo.Direction.REVERSE);
        } else {
            //Aliases.servoMap.get("arm_trigger").setDirection(Servo.Direction.FORWARD);
        }
        //if (values.settings("plow").getBool("reversed")){
        //    Aliases.servoMap.get("plow").setDirection(Servo.Direction.REVERSE);
        //}else{
        //    Aliases.servoMap.get("plow").setDirection(Servo.Direction.FORWARD);

        StartValues.Settings winch = values.settings("winch");
        StartValues.Settings angular = winch.getSettings("angular_movement");
        float range = angular.getFloat("full_rotate");
        servo_pos = angular.getFloat("start_pos") / range;
        Aliases.servoMap.get("winch_left").setPosition(servo_pos + winch.getSettings("left_servo").getFloat("offset") / range);
        Aliases.servoMap.get("winch_right").setPosition(servo_pos + winch.getSettings("right_servo").getFloat("offset") / range);
    }
}
