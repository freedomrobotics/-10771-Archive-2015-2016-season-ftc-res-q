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
        Aliases.motorMap.get("drive_left").setPower(power);
        Aliases.motorMap.get("drive_right").setPower(power);
    }

    public void startLoop(long changeTime){
        drive(0);
        Aliases.motorMap.get("winch").setPower(0);
        this.changeTime = changeTime;
    }

    /**
     * returns completion of function or not
     */
    public boolean move(float distance){

    }

    public boolean move(float pointX, float pointY){

    }

    float degressRotated = 0;
    public boolean rotate(float degrees){
        if(degrees > 0){
            if
        }
    }

    public boolean rotate(float pointX, float pointY){

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
            Aliases.motorMap.get("winch").setPower(1);
        else
            Aliases.motorMap.get("winch").setPower(-1);
        return false;
    }

    public boolean winchServo(boolean up, float degrees){

    }

    public boolean winchServo(float degrees){

    }

    public void stop(){
        winchTimer.cancel();
    }
}
