package org.fhs.robotics.ftcteam10771.lepamplemousse.computations;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps.Maps;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Adam Li on 2/1/2016.
 * The cheap way for me to not interfere with autonomous code and also throw everything into a single file...
 *
 * ...or something like that
 */
public class AtomFunctions {
    Map<DcMotor, Integer> lastEncoderPulse = new HashMap<DcMotor, Integer>();

    Maps fieldMap;

    StartValues values = null;

    Timer winchTimer = new Timer();
    boolean winchTimerActive = false;
    boolean winchTimeUp = false;

    long changeTime = 0;
    private float winchServoPos;

    public AtomFunctions(StartValues values, Maps fieldMap){
        this.values = values;
        this.fieldMap = fieldMap;
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
     * @param diameter  The diameter
     * @return change in distance in millimeters
     */
    public double getChangePosition(DcMotor dcMotor, float diameter){
        double changeEncoder = getChangeEncoder(dcMotor);
        int encoderFull = values.settings("encoder").getInt("output_pulses");
        return Math.PI * diameter * (changeEncoder / (float) encoderFull);
    }

    private void drive(float power){
        if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed"))
            Aliases.motorMap.get("drive_left").setPower(-power);
        else
            Aliases.motorMap.get("drive_left").setPower(power);
        if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed"))
            Aliases.motorMap.get("drive_right").setPower(-power);
        else
            Aliases.motorMap.get("drive_right").setPower(power);
    }

    public void startLoop(long changeTime, float winchServoPos){
        drive(0);
        Aliases.motorMap.get("winch").setPower(0);
        this.changeTime = changeTime;
        this.winchServoPos = winchServoPos;
    }

    /**
     * returns completion of function or not
     */
    float moveToX, moveToY;
    boolean moving;
    public boolean move(float distance){
        if (moving)
            return move(moveToX, moveToY);
        float x = fieldMap.getRobot().getPosition().getX();
        float y = fieldMap.getRobot().getPosition().getY();
        float rot = fieldMap.getRobot().getRotation().getRadians();

        //WATCH
        moveToX = x + (float)(Math.cos(rot)*distance);
        moveToY = y + (float)(Math.sin(rot)*distance);
        //end watch

        moving = true;

        return move(moveToX, moveToY);
    }

    public boolean move(float pointX, float pointY){
        if (!rotate(pointX, pointY))
            return false;
        return true;
    }

    float degressRotated = 0;
    public boolean rotate(float degrees){
        if(degrees > 0){
            //if
        }
        return false;
    }

    public boolean rotate(float pointX, float pointY){

        return false;
    }

    public boolean winchMotor(boolean in, float time){
        if(winchTimerActive)
            return winchMotor(in);
        winchTimerActive = true;
        winchTimeUp = false;
        TimerTask stopWinch = new TimerTask() {
            @Override
            public void run() {
                winchTimeUp = true;
            }
        };
        winchTimer.schedule(stopWinch, (long) (time * 1000));
        if (in)
            Aliases.motorMap.get("winch").setPower(1);
        else
            Aliases.motorMap.get("winch").setPower(-1);
        return false;
    }

    private boolean winchMotor(boolean in) {
        if (winchTimeUp){
            winchTimeUp = false;
            winchTimerActive = false;
            return true;
        }
        if (in)
            Aliases.motorMap.get("winch").setPower(-1);
        else
            Aliases.motorMap.get("winch").setPower(1);
        return false;
    }

    float winchServoTarget = -1000.0f;
    public boolean winchServo(boolean up, float degrees){
        if(winchServoTarget != -1000.0f)
            return winchServo(winchServoTarget);

        //calculate target
        if(up)
            winchServoTarget = winchServoPos + degrees;
        if(!up)
            winchServoTarget = winchServoPos - degrees;

        return winchServo(winchServoTarget);
    }

    public boolean winchServo(float degrees){
        if (degrees > values.settings("winch").getSettings("angular_movement").getFloat("max_rotate") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate")){
            degrees = values.settings("winch").getSettings("angular_movement").getFloat("max_rotate") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate");
        }
        if (degrees < 0) {
            degrees = 0;
        }
        if (degrees > winchServoPos) {
            winchServoPos += (values.settings("winch").getSettings("angular_movement").getFloat("max_ang_vel") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate")) * ((float) changeTime / 1000.0f);
            if (winchServoPos > degrees){
                winchServoPos = degrees;
                winchServoTarget = -1000.0f;
                return true;
            }
        }
        if (degrees < winchServoPos) {
            winchServoPos -= (values.settings("winch").getSettings("angular_movement").getFloat("max_ang_vel") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate")) * ((float) changeTime / 1000.0f);
            if (winchServoPos < degrees){
                winchServoPos = degrees;
                winchServoTarget = -1000.0f;
                return true;
            }
        }
        return false;
    }

    public void stop(){
        winchTimer.cancel();
    }

    public float getWinchServoPos() {
        return winchServoPos;
    }
}
