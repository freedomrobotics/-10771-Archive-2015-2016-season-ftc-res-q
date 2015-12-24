package org.fhs.robotics.ftcteam10771.lepamplemousse.modes;

import com.qualcomm.robotcore.hardware.HardwareMap;
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

    //TODO: 12/24/2015 Organize these variables
    float servo_pos = 0.0f;
    boolean RB_pressed = false;
    boolean A_pressed = false;
    long changeTime = 0;
    boolean lift_plow = false;


    /**
     * The constructor for the driver controlled class
     *
     * @param controls    Reference to the object of an initialized ControllersInit class
     * @param startValues Reference to the object of an initialized StartValues class
     * @param telemetry   Reference to the Telemetry object of the OpMode
     */
    public Controlled(ControllersInit controls, StartValues startValues, Telemetry telemetry) {
        this.controls = controls;
        this.values = startValues;
        this.telemetry = telemetry;
        lastTime = System.currentTimeMillis();
    }

    /**
     * The loop of the controlled class. Does not contain a loop, since it's expected to be within a loop.
     * It lets the drivers drive.
     */
    public void loop() {
        //for any physics, we have the change in time in milliseconds given by changeTime
        changeTime = System.currentTimeMillis() - lastTime;
        lastTime += changeTime;

        //run the drive function
        drive();

        //press buttons to toggle things
        toggle();

        //lift or drop the plow
        liftPlow(lift_plow);

        //adjust the angle of projection of winch
        adjustWinchAngle();

        //adjust the length of extension of winch
        extendWinch(lift_plow);
    }

    /**
     * The function to drive the drivetrain.
     */
    // TODO: 12/24/2015 determine gamepad mappings
    // TODO: 12/24/2015 figure out how to use acutual map_name
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

    public Float getFloat(String component, String quantity){
        return (Float)((Map) variables.retrieve("winch")).get("angular_movement");
    }

    public Float getFloat(String component, String quantity, String data){
        return (Float)((Map) ((Map) variables.retrieve("winch")).get("angular_movement")).get("max_rotate");
    }

    public Float winchAngular(String data){
        return getFloat("winch", "angular_movement", data);
    }

    public Float convertedWinch(Float quantity){
        return quantity/winchAngular("full_rotate");
    }

    public Float convertedPlow(Float quantity){
        return quantity/getFloat("plow", "full_rotate");
    }

    public void press(){
        //might consider putting button logics here
    }

    public void toggle(){
        if (controls.getDigital("plow") && !lift_plow && !A_pressed){
            lift_plow = true;
            A_pressed = true;
        }
        else if (controls.getDigital("plow") && lift_plow && !A_pressed) {
            lift_plow = false;
            A_pressed = true;
        }
        else if (!controls.getDigital("plow") && A_pressed) {
            A_pressed = false;
        }
    }

    public void adjustWinchAngle(){
        if (!controls.getDigital("servos_off")) { //test branch conflict: says gamepad1.right_bumper instead of gamepad2
            servo_pos += controls.getAnalog("winch_angle") * ((winchAngular("max_ang_velocity") / winchAngular("full_rotate")) * ((float) changeTime / 1000.0f));
            if (servo_pos > convertedWinch(winchAngular("max_rotate"))){
                servo_pos = convertedWinch(winchAngular("max_rotate"));
            }
            if (servo_pos < 0) {
                servo_pos = 0;
            }
            if (controls.getDigital("winch_preset"))
                servo_pos = convertedWinch(winchAngular("preset"));
            Aliases.servoMap.get("main_winch").setPosition(servo_pos + convertedWinch(getFloat("winch", "left_servo", "offset")));
            Aliases.servoMap.get("secondary_winch").setPosition(servo_pos + convertedWinch(getFloat("winch", "left_servo", "offset")));
            RB_pressed = false;
            Aliases.servoMap.get("arm_trigger").setPosition(1 - controls.getAnalog("trigger_arm"));//test branch conflict: gamepad1.right_trigger instead of gamepad 2
        } else if (!RB_pressed) {
            Aliases.servoMap.get("main_winch").getController().pwmDisable();
            Aliases.servoMap.get("secondary_winch").getController().pwmDisable();
            Aliases.servoMap.get("arm_trigger").getController().pwmDisable();
            Aliases.servoMap.get("plow_lift").getController().pwmDisable();
            RB_pressed = true;
        }
    }

    public void extendWinch(boolean liftPlow){
        if (liftPlow) {
           Aliases.motorMap.get("winch_motor").setPower(controls.getAnalog("winch_extend_retract"));
        } else{
            (Aliases.motorMap.get("winch_motor")).setPower(0);
        }
    }

    public void liftPlow(boolean liftPlow){
        if (liftPlow && !RB_pressed){
            Aliases.servoMap.get("plow_lift").setPosition(convertedPlow(getFloat("plow", "offset")) + convertedPlow(getFloat("plow", "up_angle")));
        }else {
            if (!RB_pressed)
            Aliases.servoMap.get("plow_lift").setPosition(convertedPlow(getFloat("plow", "offset")) + convertedPlow(getFloat("plow", "down_angle")));
        }
    }

    public void cleanup(){
        //cleanup code
    }
}
