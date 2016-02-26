package org.fhs.robotics.ftcteam10771.lepamplemousse.computations;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps.Maps;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam Li on 2/18/2016.
 */
public class AtomFunctionsTake2 {

    Maps fieldMap;
    StartValues values = null;
    float servo_pos;
    LinearOpMode parent = null;

    public AtomFunctionsTake2(StartValues values, Maps fieldMap, LinearOpMode parent){
        this.values = values;
        this.fieldMap = fieldMap;
        this.parent = parent;
    }

    public static class CommandParser{
        String command;
        List<Object> arguments = new ArrayList<Object>();

        public CommandParser(String command){
            if (command.contains(" ")) {
                this.command = command.split(" ", 2)[0];
                String argumentList = command.split(" ", 2)[1];
                argumentList = argumentList.replaceAll("\\s", "");
                for (String arg : argumentList.split(",")) {
                    arguments.add(arg);
                }
            }
            this.command = command;
        }

        public String command(){
            return command;
        }

        public int getArgsSize(){
            return arguments.size();
        }

        public Object getArgObject(int loc) {
            return arguments.get(loc);
        }

        public boolean getArgBool(int loc) {
            if (arguments.get(loc) != null)
                return arguments.get(loc).toString().equals("true");
            return false;
        }

        public String getArgString(int loc) {
            if (arguments.get(loc) != null)
                return arguments.get(loc).toString();
            return null;
        }

        public int getArgInt(int loc) {
            if (arguments.get(loc) != null) {
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(arguments.get(loc).toString());
                if (m.find()) {
                    return 0;
                }
                if (arguments.get(loc).toString().contains(".")) {
                    return ((Double) Double.parseDouble(arguments.get(loc).toString())).intValue();
                }
                return Integer.parseInt(arguments.get(loc).toString());
            }
            return 0;
        }

        public float getArgFloat(int loc) {
            if (arguments.get(loc) != null) {
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(arguments.get(loc).toString());
                if (m.find()) {
                    return 0;
                }
                if (!arguments.get(loc).toString().contains(".")) {
                    return ((Integer) Integer.parseInt(arguments.get(loc).toString())).floatValue();
                }
                return ((Double) Double.parseDouble(arguments.get(loc).toString())).floatValue();
            }
            return 0;
        }
    }



    public void drive(float power){
        if (values.settings("drivetrain").getSettings("motor_left").getBool("reversed"))
            Aliases.motorMap.get("drive_left").setPower(-power);
        else
            Aliases.motorMap.get("drive_left").setPower(power);
        if (values.settings("drivetrain").getSettings("motor_right").getBool("reversed"))
            Aliases.motorMap.get("drive_right").setPower(-power);
        else
            Aliases.motorMap.get("drive_right").setPower(power);
    }


    // TODO: 2/19/2016 Finish and add Map references back in
    // TODO: 2/19/2016 Make sure robot doesn't ram into wall
    public void move(float distance) throws InterruptedException {
        float wheel_dia = values.settings("drivetrain").getSettings("wheel").getFloat("diameter");
        double startPos = getPosition(Aliases.motorMap.get("drive_left"), wheel_dia);
        //Signs are not intuitive because the motor is reversed.
        double endPos = startPos - distance * 10.0f;
        double travelled;
        while (true){
            if (endPos <= startPos) {
                drive(-1);
                if ((travelled = getPosition(Aliases.motorMap.get("drive_left"), wheel_dia)) >= endPos) {
                    break;
                }
            }else{
                drive(1);
                if ((travelled = getPosition(Aliases.motorMap.get("drive_left"), wheel_dia)) <= endPos) {
                    break;
                }
            }
            parent.waitForNextHardwareCycle();
        }
        drive(0);

        travelled -= startPos;

        travelled *= 1000.0f;

        float x = fieldMap.getRobot().getPosition().getX();
        float y = fieldMap.getRobot().getPosition().getY();
        float rot = fieldMap.getRobot().getRotation().getRadians();

        fieldMap.getRobot().getPosition().setX(x + (float) (Math.cos(rot) * travelled));
        fieldMap.getRobot().getPosition().setY(y + (float) (Math.sin(rot) * travelled));
    }

    public void move(float pointX, float pointY) throws InterruptedException {
        rotate(pointX, pointY);
        float changeX = pointX - fieldMap.getRobot().getPosition().getX();
        float changeY = pointY - fieldMap.getRobot().getPosition().getY();
        float distance = (float)Math.sqrt(changeX*changeX + changeY*changeY);
        move(distance * 100);
    }

    public void rotate(float degrees){

    }

    public void rotate(float pointX, float pointY){

    }

    public void winchServo(float degrees){
        if (degrees > values.settings("winch").getSettings("angular_movement").getFloat("max_rotate") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate")){
            degrees = values.settings("winch").getSettings("angular_movement").getFloat("max_rotate") / values.settings("winch").getSettings("angular_movement").getFloat("full_rotate");
        }
        if (degrees < 0) {
            degrees = 0;
        }/*
        while (degrees != servo_pos)
        if (degrees > servo_pos) {
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
        return false;*/
    }

    public void winchServo(boolean up, float degrees){

    }

    public void clearArm(){

    }


    Map<DcMotor, Integer> lastEncoderPulse = new HashMap<DcMotor, Integer>();


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

    public double getPosition(DcMotor dcMotor, float diameter){
        int pos = dcMotor.getCurrentPosition();
        int encoderFull = values.settings("encoder").getInt("output_pulses");
        return Math.PI * diameter * ((float) pos / (float) encoderFull);
    }

    public void setServoPos(float servo_pos){
        this.servo_pos = servo_pos;
    }

    public float getServo_pos(){
        return servo_pos;
    }
}
