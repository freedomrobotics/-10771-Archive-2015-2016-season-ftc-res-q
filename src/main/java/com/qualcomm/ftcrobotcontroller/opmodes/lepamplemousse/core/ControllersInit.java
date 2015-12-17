package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Controllers;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.Gamepad;

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
    
    public ReturnValues initialize(){
        //Insert code here to alias variables
        return ReturnValues.FAIL;
    }
}