package org.fhs.robotics.ftcteam10771.lepamplemousse.modes;

import com.qualcomm.robotcore.robocol.Telemetry;

import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.ControllersInit;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

import java.util.Map;

/**
 * Driver controlled class
 */
public class Controlled {
    ControllersInit controls;
    StartValues values = null;
    Telemetry telemetry;
    Variables variables = null;
    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())
    float servo_pos = 0;

    /**
     * The constructor for the driver controlled class
     *
     * @param controls    Reference to the object of an initialized ControllersInit class
     * @param startValues Reference to the object of an initialized StartValues class
     * @param telemetry   Reference to the Telemetry object of the OpMode
     */
    public Controlled(ControllersInit controls, StartValues startValues, Telemetry telemetry, Variables variables) {
        this.controls = controls;
        this.values = startValues;
        this.telemetry = telemetry;
        this.variables = variables;
        lastTime = System.currentTimeMillis();
    }

    /**
     * The loop of the controlled class. Does not contain a loop, since it's expected to be within a loop.
     * It lets the drivers drive.
     */
    public void loop() {
        //for any physics, we have the change in time in milliseconds given by changeTime
        long changeTime = System.currentTimeMillis() - lastTime;
        lastTime += changeTime;

        //run the drive function
        drive();
    }

    /**
     * The function to drive the drivetrain.
     */
    // TODO: 12/20/2015 implement the max_power and map_name
    // TODO: 12/20/2015 To make easier on eyes, put recalled values into a variable or something. The JVM would automatically optimize it.
    public void drive() {
        float offset = Math.abs(values.settings("drivetrain").getSettings("motor_left").getFloat("offset") - values.settings("drivetrain").getSettings("motor_right").getFloat("offset"));
        if (values.settings("drivetrain").getSettings("motor_left").getFloat("offset") >= values.settings("drivetrain").getSettings("motor_right").getFloat("offset")) {
            offset = StrictMath.copySign(offset, controls.getAnalog("drivetrain_right"));
            if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed")) {
                Aliases.motorMap.get("drive_left").setPower(-controls.getAnalog("drivetrain_left"));
            } else {
                Aliases.motorMap.get("drive_left").setPower(controls.getAnalog("drivetrain_left"));
            }
            if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed")) {
                Aliases.motorMap.get("drive_right").setPower(-(controls.getAnalog("drivetrain_right") - offset));
            } else {
                Aliases.motorMap.get("drive_right").setPower(controls.getAnalog("drivetrain_right") - offset);
            }
        }
        if (values.settings("drivetrain").getSettings("motor_left").getFloat("offset") < values.settings("drivetrain").getSettings("motor_right").getFloat("offset")) {
            offset = StrictMath.copySign(offset, controls.getAnalog("drivetrain_left"));
            if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed")) {
                Aliases.motorMap.get("drive_right").setPower(-controls.getAnalog("drivetrain_right"));
            } else {
                Aliases.motorMap.get("drive_right").setPower(controls.getAnalog("drivetrain_right"));
            }
            if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed")) {
                Aliases.motorMap.get("drive_left").setPower(-(controls.getAnalog("drivetrain_left") - offset));
            } else {
                Aliases.motorMap.get("drive_left").setPower(controls.getAnalog("drivetrain_left") - offset);
            }
        }
    }
    /*
    public void useWinch() {
        if (!(Boolean)controls.getGamepad(1, "right_bumper")) {
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
    }

    public void liftPlow() {
        if (gamepad2.a && !lift_plow && !A_pressed) {
            lift_plow = true;
            A_pressed = true;
        } else if (gamepad2.a && lift_plow && !A_pressed) {
            lift_plow = false;
            A_pressed = true;
        } else if (!gamepad2.a && A_pressed) {
            A_pressed = false;
        }
        if (lift_plow && !RB_pressed) {
            Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("up_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());

        } else {
            if (!RB_pressed)
                Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("down_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());
        }
    }

    public void extendWinch() {
        if (lift_plow) {
            Core.motor[2].setPower(gamepad2.left_stick_y);
        } else {
            Core.motor[2].setPower(0)
        }
    }

    public Float getFloat(String component, String quantity, String data){
        return (Float)((Map) ((Map) variables.retrieve(component)).get(quantity)).get(data);
    }

    public Float winchAngular(String data){
        return getFloat("winch", "angular_movement", data);
    }

    public Float winch_MaxAngVel_over_FullRotate(){
        return winchAngular("max_ang_vel") / winchAngular("full_rotate");
    }

    public void test() {
        long changeTime = System.currentTimeMillis() - lastTime;
        lastTime += changeTime;
        Core.motor[0].setPower(gamepad1.left_stick_y);
        Core.motor[1].setPower(-gamepad1.right_stick_y);
        if (!gamepad1.right_bumper) {
            servo_pos += gamepad2.right_stick_y * (winch_MaxAngVel_over_FullRotate() * ((float) changeTime / 1000.0f));
            if (servo_pos > winch_MaxAngVel_over_FullRotate()) {
                servo_pos = winch_MaxAngVel_over_FullRotate();
            }
            if (servo_pos < 0) {
                servo_pos = 0;
            }
            if ((Boolean)controls.getGamepad(2, "left_bumper"))
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
        if (gamepad2.a && !lift_plow && !A_pressed) {
            lift_plow = true;
            A_pressed = true;
        } else if (gamepad2.a && lift_plow && !A_pressed) {
            lift_plow = false;
            A_pressed = true;
        } else if (!gamepad2.a && A_pressed) {
            A_pressed = false;
        }
        if (lift_plow && !RB_pressed) {
            Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("up_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());

        } else {
            if (!RB_pressed)
                Core.servo[3].setPosition(((Double) ((Double) ((Map) variables.retrieve("plow")).get("offset") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue() + ((Double) ((Double) ((Map) variables.retrieve("plow")).get("down_angle") / (Double) ((Map) variables.retrieve("plow")).get("full_rotate"))).floatValue());
        }
        if (lift_plow) {
            Core.motor[2].setPower(gamepad2.left_stick_y);
        } else {
            Core.motor[2].setPower(0)
        }

        if (gamepad2.b) {
            Core.motor[2].setPower(((Double) (servo_pos * (Double) ((Map) ((Map) variables.retrieve("winch")).get("linear_movement")).get("mot_hold_power"))).floatValue());
        }
    }
    */
    public void cleanup(){
        //cleanup code
    }
}
