package org.fhs.robotics.ftcteam10771.lepamplemousse.computations;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam Li on 2/1/2016.
 * The cheap way for me to not interfere with autonomous code and also throw everything into a single file...
 *
 * ...or something like that
 */
public class AtomFunctions {
    Map<DcMotor, Integer> lastEncoderPulse = new HashMap<DcMotor, Integer>();

    StartValues values = null;


    public AtomFunctions(StartValues values){
        this.values = values;
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




}
