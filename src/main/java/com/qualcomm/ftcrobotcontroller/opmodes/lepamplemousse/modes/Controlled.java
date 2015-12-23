package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.sensors.Gyrometer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Map;

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
    boolean A_pressed = false;
    boolean lift_plow = false;


    float servo_pos = 0;
    public Controlled(Gamepad gamepad1, Gamepad gamepad2, Variables variables, Telemetry telemetry){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.variables = variables;
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
        lastTime = System.currentTimeMillis();
        this.telemetry = telemetry;
        telemetry.clearData();
    }
    //nowhere near final
    public void loop() {
        long changeTime = System.currentTimeMillis() - lastTime;
        lastTime += changeTime;
        Core.motor[0].setPower(gamepad1.left_stick_y);
        Core.motor[1].setPower(-gamepad1.right_stick_y);
        if (!gamepad1.right_bumper) {
            servo_pos += gamepad2.right_stick_y * ((((Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("max_ang_vel")).floatValue() / ((Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate")).floatValue()) * ((float) changeTime / 1000.0f));
            if (servo_pos > (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("max_rotate") / (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate")) {
                servo_pos = ((Double) ((Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("max_rotate") / (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            }
            if (servo_pos < 0) {
                servo_pos = 0;
            }
            if (gamepad2.left_bumper)
                servo_pos = ((Double) ((Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("preset") / (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            Core.servo[0].setPosition(servo_pos + ((Double) ((Double) ((Map) ((Map) variables.retrieve("winch")).get("left_servo")).get("offset") / (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            Core.servo[1].setPosition(servo_pos + ((Double) ((Double) ((Map) ((Map) variables.retrieve("winch")).get("right_servo")).get("offset") / (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            RB_pressed = false;
            Core.servo[2].setPosition(1 - gamepad1.right_trigger);
        } else if (!RB_pressed) {
            Core.servo[0].getController().pwmDisable();
            Core.servo[1].getController().pwmDisable();
            Core.servo[2].getController().pwmDisable();
            Core.servo[3].getController().pwmDisable();
            RB_pressed = true;
        }
        if (gamepad2.a && !lift_plow && !A_pressed){
            lift_plow = true;
            A_pressed = true;
        }
        else if (gamepad2.a && lift_plow && !A_pressed) {
            lift_plow = false;
            A_pressed = true;
        }
        else if (!gamepad2.a && A_pressed) {
            A_pressed = false;
        }
        if (lift_plow && !RB_pressed){
            Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("up_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());

        }else {
            if (!RB_pressed)
                Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("down_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());
        }
        if (lift_plow) {
            Core.motor[2].setPower(gamepad2.left_stick_y);
        }else{
            Core.motor[2].setPower(0);
        }

        if (gamepad2.b){
            Core.motor[2].setPower(((Double) (servo_pos * (Double) ((Map) ((Map) variables.retrieve("winch")).get("linear_movement")).get("mot_hold_power"))).floatValue());
        }
        telemetry.addData("winch_angle", ((Double) (servo_pos * (Double) ((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
    }
}
