package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;

/**
 * Just an example of a Linear Op Mode
 * Created by joelv on 2/17/2016.
 */
public class AutoOpMode extends LinearOpMode{

    //empty default constructor
    AutoOpMode(){}

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        straight(true);
        sleep(8000);
        rotateLeft();
        sleep(500);
        straight(true);
        sleep(2000);
        halt();
    }
    //In this instance, wait(long milliseconds) is used to keep the robot moving according to the method before the next line of code
    //Without delay, each code would happen instantly after the next, making the robot displace less than an inch

    public void straight(boolean forward){
        double power;
        if (forward==true) {power = 1.0;}
        else {power = -1.0;}
        Aliases.motorMap.get("drive_left").setPower(power);
        Aliases.motorMap.get("drive_right").setPower(power);
    }

    public void curveLeft(){
        Aliases.motorMap.get("drive_left").setPower(0.6);
        Aliases.motorMap.get("drive_right").setPower(1.0);
    }

    public void curveRight(){
        Aliases.motorMap.get("drive_left").setPower(1.0);
        Aliases.motorMap.get("drive_right").setPower(0.6);
    }

    public void rotateRight(){
        Aliases.motorMap.get("drive_left").setPower(1.0);
        Aliases.motorMap.get("drive_right").setPower(-1.0);
    }

    public void rotateLeft(){
        Aliases.motorMap.get("drive_left").setPower(1.0);
        Aliases.motorMap.get("drive_right").setPower(-1.0);
    }

    public void halt(){
        Aliases.motorMap.get("drive_left").setPower(0.0);
        Aliases.motorMap.get("drive_right").setPower(0.0);
    }
    //Adam my suggestion is to have RobotAuto extend LinearOpMode instead
    //The difference between OpMode and LinearOpMode is that OpMode has function loop() which runs in a loop
    //Meanwhile, LinearOpMode has runOpMode(), which runs down linearly once
    //LinearOpMode allows delays while OpMode does not tolerate it as much
    //sleep() can be used in autonomous to wait for a period of time before a line of code is executed
    //Unique methods of LinearOpMode: void sleep(), boolean opModeIsActive(), void waitOneHardwareCycle(), void waitForStart()
    //For more info, http://ftc.edu.intelitek.com/mod/scorm/view.php?id=90
    // And also http://ftckey.com/apis/ftc/index.html?com/qualcomm/robotcore/eventloop/opmode/LinearOpMode.html
}
