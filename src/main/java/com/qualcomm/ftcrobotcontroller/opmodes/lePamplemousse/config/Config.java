package com.qualcomm.ftcrobotcontroller.opmodes.lePamplemousse.config;

/**
 * Created by Adam Li on 11/14/2015.
 */
public interface Config {

    /**
     * Read the values from the configuration file.
     * Should run create if it fails.
     */
    void read();

    /**
     * Create a configuration using the default/current values.
     * If a configuration file exists, it should be replaced
     */
    void create();

    /**
     * Verify the retrieved values.
     * If there is an error, use default values.
     */
    void verify();
}
