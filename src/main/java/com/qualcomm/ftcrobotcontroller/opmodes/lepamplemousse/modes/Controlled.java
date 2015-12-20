package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.modes;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.ControllersInit;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core.StartValues;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Map;

/**
 * Driver controlled class
 */
public class Controlled {
    ControllersInit controls;
    StartValues values = null;
    boolean RB_pressed = false;
    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())
    float servo_pos = 0;
    Telemetry telemetry;

    public Controlled(ControllersInit controls, StartValues startValues, Telemetry telemetry){
        this.controls = controls;
        this.values = startValues;
        this.telemetry = telemetry;
        /*if (((Map)((Map) values.get("winch")).get("left_servo")).get("reversed").equals(true)){
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.FORWARD);
        }
        if (((Map) ((Map) values.get("winch")).get("right_servo")).get("reversed").equals(true)){
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.FORWARD);
        }
        if (((Map) values.get("plow")).get("reversed").equals(true)){
            Aliases.servoMap.get("plow").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("plow").setDirection(Servo.Direction.FORWARD);
        }*/
        lastTime = System.currentTimeMillis();
    }
    //nowhere near fina
    public void loop(){
        long changeTime = System.currentTimeMillis()-lastTime;
        lastTime += changeTime;
        telemetry.addData("1getSetting", values.settings("winch").getSettings("angular_movement"));
        telemetry.addData("2getInt", values.settings("drivetrain").getInt("motor_max_power"));
        telemetry.addData("3getInt float", values.settings("drivetrain").getInt("motor_wheel_ratio"));
        telemetry.addData("4getInt other", values.settings("winch").getInt("enabled"));
        telemetry.addData("5getFloat", values.settings("drivetrain").getFloat("motor_wheel_ratio"));
        telemetry.addData("6getFloat int", values.settings("drivetrain").getFloat("motor_max_power"));
        telemetry.addData("7getFloat other", values.settings("winch").getFloat("enabled"));
        telemetry.addData("8getBool", values.settings("winch").getBool("enabled"));
        telemetry.addData("8getBool other", values.settings("winch").getBool("linear_movement"));
        telemetry.addData("9getString", values.settings("trigger_arm").getString("side"));
        telemetry.addData("0Setting in setting", values.settings("winch").getSettings("angular_movement").getObject("preset"));
        telemetry.addData("flag", values.flags().getBool("test"));
        //Aliases.motorMap.get("drive_left").setPower(controls.getAnalog("drivetrain_left"));
        //Aliases.motorMap.get("drive_right").setPower(controls.getAnalog("drivetrain_right"));
        /*if (!controls.getDigital("servos_off")) {
            servo_pos += controls.getAnalog("winch_angle")*((((Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("max_ang_vel")).floatValue()/((Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("full_rotate")).floatValue())*((float)changeTime/1000.0f));
            if (servo_pos > (Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("max_rotate")/(Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("full_rotate")){
                servo_pos = ((Double)((Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("max_rotate")/(Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            }
            if (servo_pos < 0){
                servo_pos = 0;
            }
            if(controls.getDigital("winch_preset"))
                servo_pos = ((Double)((Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("preset")/(Double)((Map)((Map)values.get("winch")).get("angular_movement")).get("full_rotate"))).floatValue();
            Aliases.servoMap.get("winch_left").setPosition(servo_pos + ((Double) ((Double) ((Map) ((Map) values.get("winch")).get("left_servo")).get("offset") / (Double) ((Map) ((Map) values.get("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            Aliases.servoMap.get("winch_right").setPosition(servo_pos + ((Double) ((Double) ((Map) ((Map) values.get("winch")).get("right_servo")).get("offset") / (Double) ((Map) ((Map) values.get("winch")).get("angular_movement")).get("full_rotate"))).floatValue());
            RB_pressed = false;
            Aliases.servoMap.get("trigger_arm").setPosition(1 - controls.getAnalog("trigger_arms"));
        }else if (!RB_pressed){
            Aliases.servoMap.get("winch_left").getController().pwmDisable();
            Aliases.servoMap.get("winch_right").getController().pwmDisable();
            Aliases.servoMap.get("trigger_arm").getController().pwmDisable();
            Aliases.servoMap.get("plow").getController().pwmDisable();
            RB_pressed = true;
        }
        if (controls.getDigital("winch_preset")){
            Aliases.servoMap.get("plow").setPosition(((Double) ((Double) ((Map) values.get("plow")).get("offset") / (Double) ((Map) values.get("winch")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) values.get("plow")).get("up_angle") / (Double) ((Map) values.get("winch")).get("full_rotate"))).floatValue());

        }else {
            Aliases.servoMap.get("plow").setPosition(((Double) ((Double) ((Map) values.get("plow")).get("offset") / (Double) ((Map) values.get("winch")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) values.get("plow")).get("down_angle") / (Double) ((Map) values.get("winch")).get("full_rotate"))).floatValue());
            Aliases.motorMap.get("winch").setPower(controls.getAnalog("winch_extend_retract"));
        }*/
    }
}
