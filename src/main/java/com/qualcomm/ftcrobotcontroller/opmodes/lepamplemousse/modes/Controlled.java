package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;


import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Map;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.ControllersInit;

/**
 * Driver controlled class
 */
public class Controlled {
    Gamepad gamepad1 = null;
    Gamepad gamepad2 = null;
    Variables variables = null;
    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())
    Telemetry telemetry = null;
    boolean RB_pressed = false;
    ControllersInit controls;

    float servo_pos = 0;
    public Controlled(ControllersInit controls, Variables variables, Telemetry telemetry){
        this.variables = variables;
        /*
        if (((Map)((Map)variables.retrieve("winch")).get("left_servo")).get("reversed").equals(true)){
            Core.servo[0].setDirection(Servo.Direction.REVERSE);
        }else{
            Core.servo[0].setDirection(Servo.Direction.FORWARD);
        }
        if (((Map)((Map)variables.retrieve("winch")).get("right_servo")).get("reversed").equals(true)){
            Core.servo[1].setDirection(Servo.Direction.REVERSE);
        }else{
            Core.servo[1].setDirection(Servo.Direction.FORWARD);
        }
        */
        lastTime = System.currentTimeMillis();
        this.telemetry = telemetry;
        this.controls = controls;
    }
    //nowhere near final
    public void loop(){

        long changeTime = System.currentTimeMillis()-lastTime;
        lastTime += changeTime;
        telemetry.addData("drivetrain_left", controls.getAnalog("drivetrain_left"));
        telemetry.addData("drivetrain_right", controls.getAnalog("drivetrain_right"));
        telemetry.addData("winch_extend_retract", controls.getAnalog("winch_extend_retract"));
        telemetry.addData("winch_angle", controls.getAnalog("winch_angle"));
        telemetry.addData("trigger_arm", controls.getAnalog("trigger_arm"));
        telemetry.addData("winch_preset", controls.getDigital("winch_preset"));
        telemetry.addData("servos_off", controls.getDigital("servos_off"));
        //telemetry.addData("log_cat", controls.getDigital("log_cat"));
        /*
        Core.motor[0].setPower(gamepad1.left_stick_y);
        Core.motor[1].setPower(-gamepad1.right_stick_y);
        if (!gamepad2.right_bumper) {
            servo_pos += gamepad2.right_stick_y*((((Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("max_ang_vel")).floatValue()/((Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate")).floatValue())*((float)changeTime/1000.0f));
            if (servo_pos > (Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("max_rotate")/(Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate")){
                servo_pos = ((Double)((Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("max_rotate")/(Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            }
            if (servo_pos < 0){
                servo_pos = 0;
            }
            if(gamepad2.left_bumper)
                servo_pos = ((Double)((Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("preset")/(Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            Core.servo[0].setPosition(servo_pos + ((Double)((Double)((Map)((Map)variables.retrieve("winch")).get("left_servo")).get("offset")/(Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            Core.servo[1].setPosition(servo_pos + ((Double)((Double)((Map)((Map)variables.retrieve("winch")).get("right_servo")).get("offset")/(Double)((Map)((Map)variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            RB_pressed = false;
            Core.servo[2].setPosition(1-gamepad2.right_trigger);
        }else if (!RB_pressed){
            Core.servo[0].getController().pwmDisable();
            Core.servo[1].getController().pwmDisable();
            Core.servo[2].getController().pwmDisable();
            RB_pressed = true;
        }
        Core.motor[2].setPower(gamepad2.left_stick_y);
        */

        /* Check
        Aliases.motorMap.get("drive_left").setPower(controls.getAnalog("drivetrain_left"));
        Aliases.motorMap.get("drive_right").setPower(controls.getAnalog("drivetrain_right"));*/

    }
}
