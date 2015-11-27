package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import android.os.Environment;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Dynamic;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Static;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.io.File;

/**
 * Abstract class to use to define what each other class should do
 * or to share a value across them such as the filepath (which is not right)
 */
public abstract class Config {

    /**
     * The location of the coded location of the config files
     */
    File configDirectory = new File(Environment.getExternalStorageDirectory().toString() + Static.configPath);
    /**
     * Flag for whether the config file folder exists or not
     */
    // TODO: 11/27/2015 Move to dynamic 
    static boolean configDirExists = true;
    private boolean configDirCheck = false;

    /**
     * Debuggable Constructor
     * @param telemetry Telemetry output for Debug
     */
    public Config(Telemetry telemetry){
        // If this hasn't been done yet, or reset has been called, run the folder check
        if (!configDirCheck || Dynamic.reset) {
            // If debug is enabled, verbose output
            if (Static.Debug) {
                if (Environment.getExternalStorageDirectory().canWrite()) {
                    telemetry.addData("FS-Write", "enabled");
                } else {
                    telemetry.addData("FS-Write", "not enabled");
                }
                if (configDirectory.exists()) {
                    telemetry.addData("ConfigDir", "exists");
                } else {
                    telemetry.addData("ConfigDir", "does not exist... creating..");
                    if (configDirectory.mkdirs()) {
                        telemetry.addData("ConfigDir", "created successfully");
                    } else {
                        telemetry.addData("ConfigDir", "failed to create... using defaults");
                        configDirExists = false;
                    }
                }
            }
            // Runs if debug is not enabled
            dirCheck();
        }
        //Small flag to prevent debug spamming and prevent redundant checks
        configDirCheck = true;
        init();
    }

    /**
     * Non-debuggable constructor
     */
    public Config(){
        // If this hasn't been done yet, or reset has been called, run the folder check
        if (!configDirCheck || Dynamic.reset) {
            dirCheck();
        }
        //Small flag to prevent debug spamming and prevent redundant checks
        configDirCheck = true;
        init();
    }

    //Just to save lines
    private void dirCheck(){
        // Shortened logic to check and create the folder. If it can write and the folder
        // doesn't exist, it will try to create it, marking it as not existing if it fails.
        // If it doesn't exist and write can't happen, it's marked as not existing.
        if (!configDirectory.exists() && Environment.getExternalStorageDirectory().canWrite()) {
            if (!configDirectory.mkdirs()) {
                configDirExists = false;
            }
        }else if (!configDirectory.exists()){
            configDirExists = false;
        }
    }

    /**
     * Identifies and locates the file and such
     */
    public abstract void init();

    /**
     * Read the values from the configuration file.
     * Should run create if it fails.
     */
    public abstract void read();

    /**
     * Create a configuration using the default/current values.
     * If a configuration file exists, it should be replaced.
     * Read should be run after.
     */
    public abstract void create();

    /**
     * Verify the retrieved values. Should run after read.
     * If there is an error, use default values.
     */
    public abstract void verify();
}
