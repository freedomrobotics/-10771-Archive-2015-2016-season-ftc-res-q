package org.fhs.robotics.ftcteam10771.lepamplemousse.modes;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Core;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors.camera.ColorGrid;

import java.util.HashMap;
import java.util.Map;

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

    Map<DcMotor, Integer> lastEncoderPulse = new HashMap<DcMotor, Integer>();


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
    public void loop(){
        //To be determined
        //TODO: Come up with something last minute
    }

    /**
     * TODO: javadocs
     */
    private void approach(){
        float offset = Math.abs(values.settings("drivetrain").getSettings("motor_left").getFloat("offset") - values.settings("drivetrain").getSettings("motor_right").getFloat("offset"));
        if (values.settings("drivetrain").getSettings("motor_left").getFloat("offset") >= values.settings("drivetrain").getSettings("motor_right").getFloat("offset")) {
            //TODO: The statement below could be(or is) wrong
            offset = StrictMath.copySign(offset, values.settings("drivetrain").getFloat("motor_max_power"));
            if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed")) {}
            if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed")) {}
        }
        if (values.settings("drivetrain").getSettings("motor_left").getFloat("offset") < values.settings("drivetrain").getSettings("motor_right").getFloat("offset")) {
            offset = StrictMath.copySign(offset, values.settings("drivetrain").getFloat("motor_max_power"));
            if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed")){}
            if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed")){}
        }
    }

    //Joel: Completely made wrong, but the rough idea is that compare the grid location of the blue button and the red button
    // on the color grid.  If the red button's x-coordinate is greater, then it is to the right of blue and vice versa with blue.
    private boolean sensedColorAtRight(String color){
        int redcell = 0;
        int bluecell = 0;
        ColorGrid colorGrid = (ColorGrid)Aliases.cameraMap.get("camera");
        Integer maxGridNumber = 3;
        for (int i=0; i<maxGridNumber; i++){
            int x = i;
            for(int i=0; i<maxGridNumber; i++){
                //zero would not be the right number to compare to I know
                if (colorGrid.getCell(x, i).red() > 0) {
                    redcell = x;
                }
                if (colorGrid.getCell(x, i).blue() > 0){
                    bluecell = x;
                }
            }
        }
        if ((redcell > bluecell) && (color=="red")) return true;
        else if ((bluecell > redcell) && (color=="blue")) return true;
        else return false;
    }

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

    public double getChangeEncoder(DcMotor dcMotor){
        int changeEncoder = 0;
        if (lastEncoderPulse.get(dcMotor) != null) {
            changeEncoder = dcMotor.getCurrentPosition() - lastEncoderPulse.get(dcMotor);
            int last = lastEncoderPulse.get(dcMotor) + changeEncoder;
            lastEncoderPulse.put(dcMotor, last);
        } else {
            lastEncoderPulse.put(dcMotor, dcMotor.getCurrentPosition());
        }
        return changeEncoder;
    }

    /**
     *
     * @param dcMotor   The motor
     * @param settings  The motor settings (drivetrain.wheel.diameter)
     * @return change in distance in millimeters
     */
    public double getChangePosition(DcMotor dcMotor, StartValues.Settings settings){
        float dia = settings.getFloat("diameter");
        double changeEncoder = getChangeEncoder(dcMotor);
        int encoderFull = values.settings("encoder").getInt("output_pulses");
        return Math.PI * dia * (changeEncoder / (float) encoderFull);
    }

    public void cleanup(){
        //cleanup code
        Aliases.clearAll();
    }

}
