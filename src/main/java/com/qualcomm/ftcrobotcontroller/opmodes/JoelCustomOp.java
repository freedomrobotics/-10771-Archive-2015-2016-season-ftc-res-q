package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by joelv on 11/14/2015.
 * This OpMode is customized to work to configure the controller to work
 * like an X-Box controller settings
 */

//Custom OPmode class that runs like an Xbox race controller
public class JoelCustomOp extends OpMode {

    public JoelCustomOp(){
        // Constructor
    }

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private float reverse;
    private float forward;
    private float y = 0f;
    private float x = 0f;
    private float LeftPower;
    private float RightPower;

    @Override
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop(){

        //Acceleration triggers
        forward = gamepad1.right_trigger;       //RT for forward
        reverse = gamepad1.left_trigger;        //LT for backward
        y = forward - reverse;                  //Y is the acceleration forward/back

        //Turning Left and Right
        x = gamepad1.left_stick_x;              //Left Stick for turning
                                                //X accounts for change in direction
        //Assigns values to motors
        LeftPower = y + x;
        RightPower = y - x;

        //In case values go over 1 or -1
        LeftPower = Range.clip(LeftPower, -1, 1);
        RightPower = Range.clip(RightPower, -1, 1);

        //Run the motors
        leftMotor.setPower(LeftPower);
        rightMotor.setPower(RightPower);

    }

}
