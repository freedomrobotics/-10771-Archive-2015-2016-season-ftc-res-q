package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Loaded;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Initial reset values. Should include the default/starting values loaded
 * from the config or other places and set all variables to such.
 */
public class StartValues {

    Variables variables = null;

    Telemetry telemetry = null;

    public StartValues (Telemetry telemetry){
        this.telemetry = telemetry;
        variables = new Variables(telemetry);
        if (!variables.load()){
            variables.create();
        }
    }

    public ReturnValues loadFromConfig(){
        Loaded.drivetrainEnabled = variables.getDrivetrainExists();
        if (Loaded.drivetrainEnabled) {
            Loaded.drivetrainMotWheel = (Float)variables.getDrivetrainObject("motor_wheel_ratio");
            Loaded.drivetrainMaxMotPow = (Float)variables.getDrivetrainObject("motor_max_power");

        }
        return ReturnValues.SUCCESS;
    }

    public void resetFromConfig(){
        Loaded.drivetrainEnabled = variables.getDrivetrainExists();
        if (Loaded.drivetrainEnabled) {
            Loaded.drivetrainMotWheel = (Float)variables.getDrivetrainObject("motor_wheel_ratio");
            Loaded.drivetrainMaxMotPow = (Float)variables.getDrivetrainObject("motor_max_power");

        }
    }

    public void resetNonConfig(){

    }

    public Variables getVariables(){
        return variables;
    }

}
