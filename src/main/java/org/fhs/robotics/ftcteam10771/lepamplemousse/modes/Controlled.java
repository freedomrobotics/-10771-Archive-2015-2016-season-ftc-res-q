package org.fhs.robotics.ftcteam10771.lepamplemousse.modes;

import com.qualcomm.robotcore.robocol.Telemetry;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.ControllersInit;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

/**
 * Driver controlled class
 */
public class Controlled {
    ControllersInit controls;
    StartValues values = null;
    Telemetry telemetry;
    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())

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
}
