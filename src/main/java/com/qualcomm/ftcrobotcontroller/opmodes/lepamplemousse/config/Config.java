package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import android.os.Environment;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Static;

import java.io.File;

/**
 * Abstract class to use to define what each other class should do
 * or to share a value across them such as the filepath (which is not right)
 */
public abstract class Config {

    //Set the directory for the configuration files
    File configDirectory = new File(Environment.getExternalStorageDirectory().toString() + Static.filePath);

    /**
     * Read the values from the configuration file.
     * Should run create if it fails.
     */
    public void read(){

    }

    /**
     * Create a configuration using the default/current values.
     * If a configuration file exists, it should be replaced
     */
    public void create(){

    }

    /**
     * Verify the retrieved values.
     * If there is an error, use default values.
     */
    public void verify(){

    }
}
