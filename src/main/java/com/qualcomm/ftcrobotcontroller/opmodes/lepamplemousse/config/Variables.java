package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Static;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.io.File;

/**
 * Configuration fie accessor for a Variables Config File
 */
public class Variables extends Config{

    String fileName = Static.configVarFileName;

    public Variables(Telemetry telemetry){
        super(telemetry);
    }

    public Variables(){
        super();
    }

    @Override
    public void init() {
        if (configDirExists){

        }
    }

    @Override
    public void read() {

    }

    @Override
    public void create() {

    }

    @Override
    public void verify() {

    }
}
