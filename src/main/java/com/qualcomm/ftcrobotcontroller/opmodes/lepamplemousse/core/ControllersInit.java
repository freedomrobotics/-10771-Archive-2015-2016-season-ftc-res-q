package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Controllers;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Iterator;
import java.util.Map;

/**
 * Dynamic aliasing of various variables to the controller based on configuration
 */
public class ControllersInit {

    //region Variables
    // The gamepads
    private Gamepad gamepad1 = null;
    private Gamepad gamepad2 = null;
    // The Controller config
    private Controllers controllerConfig = null;
    //endregion

    //region Static Variable Fields for polling
    public static float     drivetrain_left         = 0.0f;         // -1.0f to 1.0f
    public static float     drivetrain_right        = 0.0f;         // -1.0f to 1.0f
    public static float     winch_extend_retract    = 0.0f;         // -1.0f to 1.0f
    public static float     winch_angle             = 0.0f;         // -1.0f to 1.0f
    public static float     trigger_arm             = 0.0f;         // 0.0f to 1.0f
    public static boolean   winch_preset            = false;        // true or false
    public static boolean   servos_off              = false;        // true or false
    public static boolean   log_cat                 = false;        // true or false
    //endregion
    
    /**
     * Contructs the ControllersInit class which is for the aliasing of various inputs from the gamepads
     * @param gamepad1 Gamepad1
     * @param gamepad2 Gamepad2
     */
    //The constructor takes the two gamepads as input arguments to assign to the class's variable
    //It also constructs the configuration file
    public ControllersInit(Gamepad gamepad1, Gamepad gamepad2, Controllers controllerConfig){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.controllerConfig = controllerConfig;
    }
    
    public ReturnValues initialize() {
        //Insert code here to alias variables
        /* todo: Think about other way for another lifetime
        for(int i = 1; i <= 2; i++) {
            Iterator Gamepad = controllerConfig.getEntrySet(i).iterator();
            somthing soemthins galsrhehaiufsadf
        }
        */
        //Some similar but more cheaply thrown together way
        for(int i = 1; i <= 2; i++) {
            Iterator Gamepad = controllerConfig.getEntrySet(i).iterator();
            while (Gamepad.hasNext()){
                Map.Entry button = (Map.Entry)Gamepad.next();
                String key = button.getKey().toString();
                if(controllerConfig.inputEnabled(i, key)){
                    String functionName = controllerConfig.getFunction(i, key);
                    boolean inverted = controllerConfig.invertedEnabled(i, key);
                    if(controllerConfig.digitalEnabled(i, key)){
                        if(inverted){
                            //check(functionName) = getGamepad(i, functionName);
                        }
                    }else if(controllerConfig.analogEnabled(i, key)){

                    }else{
                        //What? Use default? Will do later
                    }
                }
            }
        }
        return ReturnValues.FAIL;
    }

    //Joel isn't there templates or something for this?
    private boolean invert(boolean a){
        return !a;
    }
    private float invert(float a){
        return -a;
    }

    //And here's the terrible part :( todo:add an overriding function to objects Float and Boolean to perform checks
    private static Object check(String name){
        if (name.equals("drivetrain_left")) return drivetrain_left;
        if (name.equals("drivetrain_right")) return drivetrain_right;
        if (name.equals("winch_extend_retract")) return winch_extend_retract;
        if (name.equals("winch_angle")) return winch_angle;
        if (name.equals("trigger_arm")) return trigger_arm;
        if (name.equals("winch_preset")) return winch_preset;
        if (name.equals("servos_off")) return servos_off;
        if (name.equals("log_cat")) return log_cat;
        return null;
    }
    private Object getGamepad(Integer id, String name){
        if (name.equals("left_stick_x")){if (id.equals(1)){return gamepad1.left_stick_x;}if (id.equals(2)){return gamepad2.left_stick_x;}}
        if (name.equals("left_stick_y")){if (id.equals(1)){return gamepad1.left_stick_y;}if (id.equals(2)){return gamepad2.left_stick_y;}}
        if (name.equals("right_stick_x")){if (id.equals(1)){return gamepad1.right_stick_x;}if (id.equals(2)){return gamepad2.right_stick_x;}}
        if (name.equals("right_stick_y")){if (id.equals(1)){return gamepad1.right_stick_y;}if (id.equals(2)){return gamepad2.right_stick_y;}}
        if (name.equals("dpad_up")){if (id.equals(1)){return gamepad1.dpad_up;}if (id.equals(2)){return gamepad2.dpad_up;}}
        if (name.equals("dpad_down")){if (id.equals(1)){return gamepad1.dpad_down;}if (id.equals(2)){return gamepad2.dpad_down;}}
        if (name.equals("dpad_left")){if (id.equals(1)){return gamepad1.dpad_left;}if (id.equals(2)){return gamepad2.dpad_left;}}
        if (name.equals("dpad_right")){if (id.equals(1)){return gamepad1.dpad_right;}if (id.equals(2)){return gamepad2.dpad_right;}}
        if (name.equals("a_button")){if (id.equals(1)){return gamepad1.a;}if (id.equals(2)){return gamepad2.a;}}
        if (name.equals("b_button")){if (id.equals(1)){return gamepad1.b;}if (id.equals(2)){return gamepad2.b;}}
        if (name.equals("x_button")){if (id.equals(1)){return gamepad1.x;}if (id.equals(2)){return gamepad2.x;}}
        if (name.equals("y_button")){if (id.equals(1)){return gamepad1.y;}if (id.equals(2)){return gamepad2.y;}}
        if (name.equals("guide_button")){if (id.equals(1)){return gamepad1.guide;}if (id.equals(2)){return gamepad2.guide;}}
        if (name.equals("start_button")){if (id.equals(1)){return gamepad1.start;}if (id.equals(2)){return gamepad2.start;}}
        if (name.equals("back_button")){if (id.equals(1)){return gamepad1.back;}if (id.equals(2)){return gamepad2.back;}}
        if (name.equals("left_bumper")){if (id.equals(1)){return gamepad1.left_bumper;}if (id.equals(2)){return gamepad2.left_bumper;}}
        if (name.equals("right_bumper")){if (id.equals(1)){return gamepad1.right_bumper;}if (id.equals(2)){return gamepad2.right_bumper;}}
        if (name.equals("left_stick_button")){if (id.equals(1)){return gamepad1.left_stick_button;}if (id.equals(2)){return gamepad2.left_stick_button;}}
        if (name.equals("right_stick_button")){if (id.equals(1)){return gamepad1.right_stick_button;}if (id.equals(2)){return gamepad2.right_stick_button;}}
        if (name.equals("left_trigger")){if (id.equals(1)){return gamepad1.left_trigger;}if (id.equals(2)){return gamepad2.left_trigger;}}
        if (name.equals("right_trigger")){if (id.equals(1)){return gamepad1.right_trigger;}if (id.equals(2)){return gamepad2.right_trigger;}}
        return null;
    }
}