package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Adam Li on 10/16/2015.
 * The class is structured to be easy to follow and is heavily annotated. It's designed to
 * help people with enough programming knowledge easily pick up how to setup a basic mode
 * on the robot.
 *
 * After creating an OpMode (Operational Mode) class, you have to add it to
 * FtcOpModeRegister within the package com.qualcomm.ftcrobotcontroller.opmodes.
 * Basic syntax is: manager.register ("MenuItemName", Classname.class);
 * For this OpMode: manager.register ("Adam Drive Example", AdamBasicDriveExample.class);
 *
 * This class will allow manual control of a simple drive robot with a joystick controlling
 * a side (Tank style), and with the press of a button (left trigger) it will cycle to Arcade
 * Drive (left joystick only), and three other modes that are uncommon and impractical because
 * they are made up (Racing game style and digital joysticks for arcade and tank style). With
 * the press of another button (right trigger), the robot will make a complete circle or square
 * to the left or right, depending on which horizontal direction the left joystick (circle) or
 * right joystick (square) is moved to within a second. If both joysticks are activated at the
 * same moment, the action is canceled and the robot will move back a bit.
 */
public class AdamBasicDriveExample extends OpMode{

    // Empty class constructor
    public AdamBasicDriveExample ()
    {
        // Needed to create the class object (Java requires it), but since there isn't
        // anything being passed to it and there's no needed setup on creation (at
        // least in this case), it's empty.
    }


    // Below I'm declaring variables in the scope of the entire class. The reason I'm not
    // declaring them in the init is because those variables would only be local to the
    // init function.
    // Normally I declare these right in the beginning, before the constructor or anything.

    // The SDK includes a few new object types, one is the DcMotor object (which should
    // be fairly straightforward). Below I'm declaring the two drive motors typical of
    // most robots.
    private DcMotor leftMotor;  // Notice how I named it. There are many ways to name variables.
    private DcMotor rightMotor; // The way I name them is a mix of naming schemes, which you'll see sooner or later.
    private int driveMode;      // Refer to top. Drive modes are in the order written.

    private double currentVel;  // Current Velocity. Used for calculations with acceleration.
    private double leftVel;     // Velocity of left wheel. Used for calculations with acceleration.
    private double rightVel;    // Velocity of right wheel. Used for calculations with acceleration.
    private double maxVel;      // Max Velocity. Calculated in the program.

    private long lastTime;      // The time at the last time check (using System.currentTimeMillis())
    private long changeTime;    // The change in time (in millis) from last time check

    private boolean LTpressed;  // Flags to avoid the pressing of a button twice.
    private boolean RTpressed;  // TODO: 10/26/2015 Find a better method, if possible


    // The below are "flags" that I like to use to make code easier to change in many areas
    // Normally I would set these before I would even declare the variables.
    private static final boolean motorLeftReverse = false;  // All the "gibberish" before the "flag" name
    private static final boolean motorRightReverse = true;  // is to make sure the variable can't change

    private static final boolean curvedJoystickMap = true;  // This flag changes the joystick mapping between linear and squared


    // Variables that affect the behavior of the program (can be static final or not, doesn't matter too much, but it should be done for optimization)
    private static final float wheelDiameter = 4;       // Wheel size in inches (I know it's annoying, but we live in the US)
    private static final float motorPowerRotation = .8f;// This is about how much power the motor needs to make one rotation per second.
        // Differs per robot. I could have implemented the use of a rotary encoder and a calibration
        // function, but I would need to do some experimenting first.
    private static final float buttonFAccel = 1.2f;     // Set in m/s^2. For use with the three digital drives. Forwards Acceleration
    private static final float buttonFDecel = 1.2f;     // Set in m/s^2. For use with the three digital drives. Forwards Deceleration
    private static final float buttonBAccel = 1.2f;     // Set in m/s^2. For use with the three digital drives. Backwards Acceleration
    private static final float buttonBDecel = 1.2f;     // Set in m/s^2. For use with the three digital drives. Backwards Deceleration
    private static final float steeringPower = .8f;     // Multiplied by the current velocity of the robot while in digital drive to adjust steering
        // Notice the f in 1.2f, f is used to show that it's a float, not a double. Java automatically
        // interprets a.b as a double (64-bit floating point number), which is more precise than a
        // float (32-bit floating point number). To denote a float, write add an f.

    private static final float trigThreshold = .7f;     // When to say that the analogue triggers are pressed
    private static final float stickThreshold = .3f;    // When to say that a joystick is one way or the other for autoCircle()

    //This one might change, so it's not static final.
    private float maxMotorSpeed = 1;    // Set on a floating scale of 0 < x <= 1. If out of range, will default to .8/

    private static final float circleRadius = 1;     // Radius of circle and square for automatic circle/square in meters
    private static final float maxCircleSpeed = .8f; // This*maxMotorSpeed = maximum speed the robot will go when doing the circle


    // Have you ever worked with Arduino before?
    // This is designed similarly, there's two functions that are required, init() and loop(),
    // and because they are already setup, they should be overridden with @Override a line
    // before. It isn't necessary because the compiler is capable of figuring out that you
    // want to override it, but it's a good habit to do so and makes code easier to understand.
    //
    // The init function below setups up the robot; it runs once on startup or reset
    // IT IS NOTE DESIGNED TO BE CALLED ON WITHIN ANY OTHER FUNCTION THAT YOU WILL
    // PROBABLY WRITE. I'VE SEEN PEOPLE DO THIS BEFORE, AND IT IS A TERRIBLE TRICK.
    //
    // The loop function does as it says, it loops when the program is running. Also never
    // call this function anywhere within this function. It will results in a stack overflow
    // then a crash, and nobody likes it when programs crash.
    @Override
    public void init(){
        // Assign the motor mapped to "left_drive" and "right_drive" to the appropriate
        // objects.
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        // If statements with brackets or without brackets? I say with. It's easier to edit,
        // even if it's just one line of code, and easier to understand.
        if (motorLeftReverse){                                      // These two if statements
            leftMotor.setDirection(DcMotor.Direction.REVERSE);      // read the flag set above
        }                                                           // and set reverse the motor
        if (motorRightReverse){                                     // direction if needed.
            rightMotor.setDirection(DcMotor.Direction.REVERSE);     // They aren't reversed by
        }                                                           // default.

        // Check for out of range variables
        if (!(0 < maxMotorSpeed && maxMotorSpeed <= 1)){            // Just to show negation.
            maxMotorSpeed = .8f;                                    // Default Motor Speed
        }

        driveMode = 0;                          // Default Mode: Tank Drive
        currentVel = leftVel = rightVel = 0;    // Initial Velocity (obviously!)
        LTpressed = RTpressed = false;          // Neither of the triggers are started pressed.
        // TODO: 10/22/2015 Explain comment
        maxVel = ((double)maxMotorSpeed/(double)motorPowerRotation)*wheelDiameter*0.0254*Math.PI;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void loop(){
        changeTime = System.currentTimeMillis()-lastTime;
        lastTime += changeTime;

        // Read the values from the first gamepad joysticks.
        float leftStickY = -gamepad1.left_stick_y;      // The SDK already maps the gamepads to
        float leftStickX = gamepad1.left_stick_x;       // two variables, gamepad1 and gamepad2.
        float rightStickY = -gamepad1.right_stick_y;    // The reason for the negative sign in
        float rightStickX = gamepad1.right_stick_x;     // front is because the y-values are inverted.

        // Read the curvedJoystickMap flag, and if it's true, curve the joystick values.
        if (curvedJoystickMap){
            // The *= operator: a *= b is the same as a = a*b.
            // To get a simple curve, all you need to do is square the joystick value. It won't go
            // above 1, the max, because 1^2 = 1. To make sure negatives stay, the values is
            // multiplied by the absolute value of itself.
            leftStickY *= Math.abs(leftStickY);
            leftStickX *= Math.abs(leftStickX);
            rightStickY *= Math.abs(rightStickY);
            rightStickX *= Math.abs(rightStickX);
        }

        // Check the driveMode to see how to drive the robot. Could use if statements, or switch cases.
        switch(driveMode) {
            // Check for Tank Drive
            case 0:
                mapToMotor(leftStickY, leftMotor);      // Call to the one line function that maps
                mapToMotor(rightStickY, rightMotor);    // the joystick to the motor.
                break;              // Break breaks out of the switch.

            // Check for Arcade Drive (although my drive is a modified arcade drive to work better...)
            case 1:
                // I don't feel like explaining the math :(
                mapToMotor(Math.copySign(Math.abs(leftStickY) + leftStickX, leftStickY), leftMotor);
                mapToMotor(Math.copySign(Math.abs(leftStickY) - leftStickX, leftStickY), rightMotor);
                // First I take the absolute value of the y-value and I add or subtract the x-value.
                // Because it removes the sign, the robot can only move forwards, so I use
                // Math.copySign to copy the sign from the Y stick the the result and pass that to
                // the mapToMotor(float, DcMotor) function.
                // That makes the drive make sense, where going to the bottom left makes the robot
                // move to the left while going backwards, and vice versa. Their version makes the
                // robot move to the right when the joystick is at the bottom left.
                break;

            // YAY Racing Game Style! (not very good...) // TODO: 10/24/2015 Comments for whole digital section
            case 2:
                // TODO: 10/26/2015 See if the if statements can be optimized
                if (gamepad1.a && currentVel + buttonFAccel <= maxVel) {
                    currentVel += buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.b && currentVel - buttonBAccel >= -maxVel) {
                    currentVel -= buttonBAccel * (1000 / changeTime);
                }
                if (!gamepad1.a && !gamepad1.b && currentVel > 0) {
                    currentVel -= buttonFDecel * (1000 / changeTime);
                    if (currentVel < 0) {
                        currentVel = 0;
                    }
                }
                if (!gamepad1.a && !gamepad1.b && currentVel < 0) {
                    currentVel += buttonBDecel * (1000 / changeTime);
                    if (currentVel > 0) {
                        currentVel = 0;
                    }
                }
                leftVel = Math.copySign(Math.abs(currentVel) + leftStickX * steeringPower, currentVel);
                rightVel = Math.copySign(Math.abs(currentVel) - leftStickX * steeringPower, currentVel);
                velToMotor(leftVel, leftMotor);
                velToMotor(rightVel, rightMotor);
                // TODO: 10/24/2015 Explain flaws in this code section and how to improve them
                break;

            // Digital Tank...
            case 3:
                //Right side
                if (gamepad1.y && rightVel + buttonFAccel <= maxVel) {
                    rightVel += buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.a && rightVel - buttonBAccel >= -maxVel) {
                    rightVel -= buttonBAccel * (1000 / changeTime);
                }
                if (!gamepad1.y && !gamepad1.a && rightVel > 0) {
                    rightVel -= buttonFDecel * (1000 / changeTime);
                    if (rightVel < 0) {
                        rightVel = 0;
                    }
                }
                if (!gamepad1.y && !gamepad1.a && rightVel < 0) {
                    rightVel += buttonBDecel * (1000 / changeTime);
                    if (rightVel > 0) {
                        rightVel = 0;
                    }
                }
                //Left Side
                if (gamepad1.dpad_up && leftVel + buttonFAccel <= maxVel) {
                    leftVel += buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.dpad_down && leftVel - buttonBAccel >= -maxVel) {
                    leftVel -= buttonBAccel * (1000 / changeTime);
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && leftVel > 0) {
                    leftVel -= buttonFDecel * (1000 / changeTime);
                    if (leftVel < 0) {
                        leftVel = 0;
                    }
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && leftVel < 0) {
                    leftVel += buttonBDecel * (1000 / changeTime);
                    if (leftVel > 0) {
                        leftVel = 0;
                    }
                }
                currentVel = (leftVel + rightVel) / 2;
                velToMotor(leftVel, leftMotor);
                velToMotor(rightVel, rightMotor);
                break;

            // Digital Arcade... (YOU WILL HATE DRIVING THIS) (And it may not work perfectly... My head isn't always 100% accurate)
            case 4:
                // using the dpad
                if (gamepad1.dpad_up && currentVel + buttonFAccel <= maxVel) {
                    currentVel += buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.dpad_down && currentVel - buttonBAccel >= -maxVel) {
                    currentVel -= buttonBAccel * (1000 / changeTime);
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && currentVel > 0) {
                    currentVel -= buttonFDecel * (1000 / changeTime);
                    if (currentVel < 0) {
                        currentVel = 0;
                    }
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && currentVel < 0) {
                    currentVel += buttonBDecel * (1000 / changeTime);
                    if (currentVel > 0) {
                        currentVel = 0;
                    }
                }
                if (gamepad1.dpad_left && leftVel + buttonFAccel <= maxVel && currentVel > 0) {
                    rightVel += buttonFAccel * (1000 / changeTime);
                    leftVel -= buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.dpad_right && rightVel + buttonFAccel <= maxVel && currentVel > 0) {
                    leftVel += buttonFAccel * (1000 / changeTime);
                    rightVel -= buttonFAccel * (1000 / changeTime);
                }
                if (!gamepad1.dpad_left && !gamepad1.dpad_right && leftVel < rightVel && currentVel > 0) {
                    leftVel += buttonFAccel * (1000 / changeTime);
                    rightVel -= buttonFAccel * (1000 / changeTime);
                    if (leftVel > rightVel) {
                        leftVel = rightVel = (leftVel + rightVel) / 2;
                    }
                }
                if (!gamepad1.dpad_left && !gamepad1.dpad_right && leftVel > rightVel && currentVel > 0) {
                    rightVel += buttonFAccel * (1000 / changeTime);
                    leftVel -= buttonFAccel * (1000 / changeTime);
                    if (leftVel < rightVel) {
                        leftVel = rightVel = (leftVel + rightVel) / 2;
                    }
                }
                if (gamepad1.dpad_left && leftVel + buttonFAccel <= maxVel && currentVel < 0) {
                    rightVel -= buttonFAccel * (1000 / changeTime);
                    leftVel += buttonFAccel * (1000 / changeTime);
                }
                if (gamepad1.dpad_right && rightVel + buttonFAccel <= maxVel && currentVel < 0) {
                    leftVel -= buttonFAccel * (1000 / changeTime);
                    rightVel += buttonFAccel * (1000 / changeTime);
                }
                if (!gamepad1.dpad_left && !gamepad1.dpad_right && leftVel < rightVel && currentVel < 0) {
                    leftVel -= buttonFAccel * (1000 / changeTime);
                    rightVel += buttonFAccel * (1000 / changeTime);
                    if (leftVel > rightVel) {
                        leftVel = rightVel = (leftVel + rightVel) / 2;
                    }
                }
                if (!gamepad1.dpad_left && !gamepad1.dpad_right && leftVel > rightVel && currentVel < 0) {
                    rightVel -= buttonFAccel * (1000 / changeTime);
                    leftVel += buttonFAccel * (1000 / changeTime);
                    if (leftVel < rightVel) {
                        leftVel = rightVel = (leftVel + rightVel) / 2;
                    }
                }
                double difference = currentVel - ((leftVel + rightVel) / 2);
                leftVel += difference;
                rightVel += difference;
                velToMotor(leftVel, leftMotor);
                velToMotor(rightVel, rightMotor);
                break;
            default:
                driveMode = 0;
                break;
        }

        // TODO: 10/26/2015 COMMENTS COMMENTS COMMENTS
        if (gamepad1.left_trigger > trigThreshold && !LTpressed) {
            LTpressed = true;
            driveMode++;
        }
        else if (gamepad1.left_trigger < (trigThreshold-.1f) && LTpressed){
            LTpressed = false;
        }

        if (gamepad1.right_trigger > trigThreshold && !RTpressed) {
            RTpressed = true;
            while (lastTime > System.currentTimeMillis()-1000){
                if (Math.abs(gamepad1.left_stick_x) > stickThreshold && Math.abs(gamepad1.right_stick_x) > stickThreshold){
                    mapToMotor(.5f, leftMotor);
                    mapToMotor(.5f,rightMotor);
                    break;
                } else if (Math.abs(gamepad1.left_stick_x) > stickThreshold){
                    if (gamepad1.left_stick_x < 0) autoCircle(true,true);
                    if (gamepad1.left_stick_x > 0) autoCircle(true,false);
                    break;
                } else if (Math.abs(gamepad1.right_stick_x) > stickThreshold){
                    if (gamepad1.right_stick_x < 0) autoCircle(false,true);
                    if (gamepad1.right_stick_x > 0) autoCircle(false,false);
                    break;
                }
            }
        }
        else if (gamepad1.right_trigger < (trigThreshold-.1f) && RTpressed){
            RTpressed = false;
        }
    }

    // TODO: 10/17/2015 create the autoCircle function
    // It's private so that it isn't accessible outside of this class.
    // circle tells the class if it's making a circle of a square, and left tells
    // it to turn left or right. Remember that Java is case-sensitive.
    private void autoCircle(boolean circle, boolean left){

    }

    // A simple function to map values on a scale of -1 to 1 to whatever the maxMotorSpeed was set to.
    // Throttle is the power value you want to map to the max, and motor is the motor to actually
    // change. In C++ and stuff, you'd need a pointer to affect the original object, but Java doesn't
    // have pointers. Instead everything is a reference, and the JVM makes the decisions for us.
    private void mapToMotor(float throttle, DcMotor motor){
        // This makes sure to avoid an out of range error by limiting it.
        Range.clip(throttle, -1, 1);
        // Simple mathematical formula for mapping. Expanded to be easier to read. Didn't feel like
        // looking for a native Java function. Edit: DANG IT THERE IS A SCALE FUNCTION, don't care.
        motor.setPower(((double)throttle - (-1) ) / ( (1) - (-1) ) * ( (maxMotorSpeed) - (-maxMotorSpeed) ) + -maxMotorSpeed);
    }

    // Another function to convert the meters per second calculated in the main function to the
    // power that the motor requires to move to that speed as defined around the flags. Reason
    // behind doing this is because we have the processing power and working with more standard
    // units is soo much easier. Do not use this on the final robot as we will probably have
    // rotary encoders and that allows for a much more consistent measurements.
    // At this point, it might have been more effective to just create a map function or look for
    // one because it's basically the same as mapToMotor, but OH WELL.
    // TODO: 10/22/2015 Elaborate
    private void velToMotor(double velocity, DcMotor motor){
        Range.clip(velocity, -maxVel, maxVel);
        motor.setPower((velocity - (-maxVel)) / ((maxVel) - (-maxVel)) * ((maxVel) - (-maxVel)) + -maxVel);
    }
}
