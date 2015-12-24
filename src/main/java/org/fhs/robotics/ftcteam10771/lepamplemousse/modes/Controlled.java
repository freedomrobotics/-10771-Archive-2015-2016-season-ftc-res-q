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
    boolean use_servos = true;


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
        //Gamepad 1 Left Stick Vertical: Left drivetrain
        //Gamepad 1 Right Stick Vertical: Right drivetrain
        drive();

        //press buttons to toggle things
        //Gamepad 2 A Button: Lift/Drop Plow
        //Gamepad 2 Right Bumper: Turn off/on servos
        toggle();

        //lift or drop the plow
        //Gamepad 2 A Button
        liftPlow(lift_plow);

        //functions for the servos
        //Gamepad 2 Right Bumper: Turn off/on servos
        //Gamepad 2 Left Bumper: Sets preset projection angle
        //Gamepad 2 Right Stick Y: Adjust projection angle
        //Gamepad 2 Right Trigger: Flicks trigger arm
        if (use_servos){
            adjustWinchAngle();
            moveArmTrigger();
        }
        else if (controls.getDigital("servos_off") && !RB_pressed){
            disableServos();
        }

        //adjust the length of extension of winch
        // Gamepad 2 Left Stick Y: Extend/Retract Tape Measure
        extendWinch(lift_plow);
    }
    
    // TODO: 12/24/2015 figure out how to use acutual map_name
    // TODO: 12/20/2015 implement the max_power and map_name
    // TODO: 12/20/2015 To make easier on eyes, put recalled values into a variable or something. The JVM would automatically optimize it.
    /**
     * The function to drive the drivetrain.
     */
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
    //region float value getters

    /**
     * Obtains the float value of a data
     * @param component The component with the value
     * @param quantity The data to get
     * @return The value of the data
     */
    public Float getFloat(String component, String quantity){
        return (Float)((Map) variables.retrieve(component)).get(quantity);
    }

    /**
     * Obtains float value of data
     * @param component The component with the data
     * @param quantity The category of data to get
     * @param data The data to get
     * @return The float quantity of the data
     */
    public Float getFloat(String component, String quantity, String data){
        return (Float)((Map) ((Map) variables.retrieve(component)).get(quantity)).get(data);
    }

    /**
     * Getter for the winch's angular measurements
     * @param data The angular data to get
     * @return The angular quantity
     */
    public Float winchAngular(String data){
        return getFloat("winch", "angular_movement", data);
    }

    /**
     * Converts the winch's angular measurement into a rational value
     * by dividing by the configured full rotational position
     * @param quantity The data to be converted
     * @return The converted value
     */
    public Float convertedWinch(Float quantity){
        return quantity/winchAngular("full_rotate");
    }

    /**
     * Converts the plow's angular measurements into a rational value
     * @param quantity The data to be converted
     * @return The data in a rational type
     */
    public Float convertedPlow(Float quantity){
        return quantity/getFloat("plow", "full_rotate");
    }

    //endregion

    public void press(){
        //might consider putting button logics here
    }

    /**
     * The function for switching
     * positions and statuses of components
     */
    public void toggle(){

        if (!controls.getDigital("servos_off")){//test branch conflict: says gamepad1.right_bumper instead of gamepad2
            use_servos = true;
            RB_pressed = false;
        }
        else if (!RB_pressed){
            RB_pressed = true;
            use_servos = false;
        }
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

    /**
     * Adjusts the winch's
     * projection angle
     */
    public void adjustWinchAngle(){
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
    }

    /**
     * Switches the servos off
     */
    public void disableServos(){
        Aliases.servoMap.get("main_winch").getController().pwmDisable();
        Aliases.servoMap.get("secondary_winch").getController().pwmDisable();
        Aliases.servoMap.get("arm_trigger").getController().pwmDisable();
        Aliases.servoMap.get("plow_lift").getController().pwmDisable();
    }

    /**
     * Moves the arm trigger of the robot
     */
    public void moveArmTrigger(){
        Aliases.servoMap.get("arm_trigger").setPosition(1 - controls.getAnalog("trigger_arm"));//test branch conflict: gamepad1.right_trigger instead of gamepad 2
    }

    /**
     * Extends the tape measure
     * of the winch ONLY IF the
     * plow is lifted
     * @param liftPlow Checker to see if plow is lifted
     */
    public void extendWinch(boolean liftPlow){
        if (liftPlow) {
           Aliases.motorMap.get("winch_motor").setPower(controls.getAnalog("winch_extend_retract"));
        } else{
            (Aliases.motorMap.get("winch_motor")).setPower(0);
        }
    }

    /**
     * Lifts plow if it is down
     * Drops plow if it is lifted
     * @param liftPlow Checker for whether the plow is lifted or dropped
     */
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
