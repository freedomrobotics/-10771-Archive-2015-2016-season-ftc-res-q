package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Dynamic;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Static;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.io.File;

/**
 * Configuration fie accessor for a Variables Config File
 */
public class Variables extends Config{

    // Filename with the suffix
    private String fileName = Static.configVarFileName+Static.configFileSufffix;
    // The actual file
    private File configFile = new File(configDirectory, fileName);
    // Does the file exist or no.
    private boolean fileExists = true;
    // variable for telemetry
    private Telemetry telemetry = null;

    /**
     * Debuggable Constructor
     * @param telemetry Telemetry output for Debug
     */
    public Variables(Telemetry telemetry){
        //Calling the superclass' (Config.class) constructor.
        super(telemetry);
        init();
    }

    /**
     * Non-debuggable constructor
     */
    public Variables(){
        //Calling the superclass' (Config.class) constructor.
        super();
        init();
    }

    /**
     * Identifies and locates the file and such.
     * Runs within the constructor
     */
    @Override
    protected void init() {
        // saves code by not typing true a billion times.
        fileExists = true;
        // if the configuration directory even exists, then check of the config exists
        if (Dynamic.configDirExists){
            //region Debug block
            // If debug is enabled, verbose output
            if (Static.Debug && telemetry != null) {
                if (configFile.exists()) {
                    telemetry.addData("VarConfFile", "exists");
                } else {
                    telemetry.addData("VarConfFile", "does not exist... creating with defaults...");
                    if (createDefaults(fileName) && writable) {
                        telemetry.addData("VarConfFile", "created successfully");
                    } else {
                        telemetry.addData("VarConfFile", "failed to create... using defaults");
                        fileExists = false;
                    }
                }
            }
            //endregion
            else{
                // shortened logic
                if(!configFile.exists() && writable){
                    if(!createDefaults(fileName)){
                        fileExists = false;
                    }
                }else if (!configFile.exists()) {
                    fileExists = false;
                }
            }
        }
        // If config doesn't exists, obviously the file doesn't and the system can't be
        // writable, so just say it doesn't exist
        else{
            if (Static.Debug && telemetry != null) telemetry.addData("VarConfFile", "using defaults");
            fileExists = false;
        }
    }

    /**
     * Read the values from the configuration file.
     * Runs and passes the value from verify and load.
     *
     * @return True if file loaded; false if defaults loaded.
     */
    @Override
    public boolean read() {
        return false;
    }

    /**
     * Creates the configuration using the default values.
     * If the configuration file exists, it will be replaced.
     *
     * @param loadAfter Whether or not to verify and load the file after replacing
     * @return Creation state.
     */
    @Override
    public boolean create(boolean loadAfter) {
        return false;
    }

    /**
     * Verify the retrieved values. True if verified, false if not.
     *
     * @param loadAfter Whether or not to load values after verification.
     * @retun Verify state
     */
    @Override
    public boolean verify(boolean loadAfter) {
        return false;
    }

    /**
     * Loads values.
     *
     * @param loadDefault Whether to load from defaults or file.
     * @return Loaded successfully or not
     */
    @Override
    public boolean load(boolean loadDefault) {
        return false;
    }
}
