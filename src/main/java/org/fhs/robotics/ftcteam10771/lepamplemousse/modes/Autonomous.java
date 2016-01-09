package org.fhs.robotics.ftcteam10771.lepamplemousse.modes;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

/**
 * Autonomous class
 */
public class Autonomous {

    StartValues values = null;
    Telemetry telemetry;
    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())

    //TODO: 12/24/2015 Organize these variables
    float servo_pos = 0.0f;
    boolean servosOff = false;
    boolean plowButton = false;
    long changeTime = 0;
    boolean plowUp = false;


    /**
     * The constructor for the autonomous class
     *
     * @param startValues Reference to the object of an initialized StartValues class
     * @param telemetry   Reference to the Telemetry object of the OpMode
     */
    public Autonomous(StartValues startValues, Telemetry telemetry) {
        this.values = startValues;
        this.telemetry = telemetry;
        lastTime = System.currentTimeMillis();
        servoSetup();
    }

    /**
     * The loop of the autonomous class. Does not contain a loop, since it's expected to be within a loop.
     * It lets the drivers drive.
     */
    public void loop(){}

    /**
     * Sets up the servos
     */
    private void servoSetup() {
        if (values.settings("winch").getSettings("left_servo").getBool("reversed")){
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("winch").getSettings("right_servo").getBool("reversed")){
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("plow").getBool("reversed")){
            Aliases.servoMap.get("plow").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("plow").setDirection(Servo.Direction.FORWARD);
        }
    }

    public void cleanup(){
        //cleanup code
        Aliases.clearAll();
    }

}
