package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars;

/**
 * These are variables that have to be hardcoded. They can't be changed via a config file.
 * Well, in the future, they could be put in a preferences file... but for now, this is good enough
 */
public class Static {
    //region Static Variables/Configurations for the config package
    
    // This is assuming the SD card path is already given.
    // Because this is going to be appended, add "/" in front
    /** Configuration Directory Path relative to /sdcard */
    public static final String configPath = "/GrapefruitConfig";

    /** Configuration File Suffixes. */
    public static final String configFileSufffix = ".yml";

    /** Filename for the config file that config.Variables loads */
    public static final String configVarFileName = "settings";

    /** Filename for the config file that config.Components loads */
    public static final String configCompFileName = "config";

    //endregion

    //region Static Variables/Configurations globally

    // Will be moved to a config file later.
    public static final boolean Debug = true;

    //endregion
}