package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Dynamic;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.Static;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Components file accessor for the everything
 * todo simplify and make some more merges with config.java
 * todo improve
 * todo make more standalone for plug and play capability into config app
 */
public class Components extends Config {

    // Filename with the suffix
    private String fileName = Static.configCompFileName+Static.configFileSufffix;
    // The actual file
    private File configFile = new File(configDirectory, fileName);
    // Does the file exist or no.
    private boolean fileExists = true;
    // variable for telemetry
    private Telemetry telemetry = null;
    // The YAML object.
    private Yaml yaml = new Yaml();
    // An object to string map.
    private Map<String, Object> data = null;
    // Private variable for object
    private Integer objectNum = 0;
    private Integer maxNum = 0;

    /**
     * Debuggable Constructor
     * @param telemetry Telemetry output for Debug
     */
    public Components(Telemetry telemetry){
        //Calling the superclass' (Config.class) constructor.
        super(telemetry);
        this.telemetry = telemetry;
        init();
    }

    /**
     * Non-debuggable constructor
     */
    public Components(){
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
                    telemetry.addData("CompConfFile", "exists");
                } else {
                    telemetry.addData("CompConfFile", "does not exist... creating with defaults...");
                    if (createDefaults(fileName) && writable) {
                        telemetry.addData("CompConfFile", "created successfully");
                    } else {
                        telemetry.addData("CompConfFile", "failed to create... using defaults");
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
            if (Static.Debug && telemetry != null) telemetry.addData("CompConfFile", "using defaults");
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
        if (load()){
            if (!verify()){
                load(true);
                return false;
            }
            return true;
        }
        load(true);
        return false;
    }

    /**
     * Creates the configuration using the default or stored values.
     * If the configuration file exists, it will be replaced.
     *
     * @param useDefaults Whether or not to load the defaults
     * @param loadAfter Whether or not to verify and load the file after replacing
     * @return Success Value based on the ReturnValues enumeration
     */
    @Override
    public ReturnValues create(boolean useDefaults, boolean loadAfter) {
        boolean result;
        if (useDefaults){
            result = createDefaults(fileName);
            if (Static.Debug && telemetry != null) {
                if (result) {
                    telemetry.addData("CreatedCompConfFile", "default created");
                    fileExists = true;
                }else{
                    telemetry.addData("CreatedCompConfFile", "failed to create default");
                }
            }
        }else{
            try {
                FileWriter configWrite = new FileWriter(configFile);
                configWrite.write(yaml.dump(data));
                configWrite.flush();
                configWrite.close();
                if (Static.Debug && telemetry != null) telemetry.addData("CreatedCompConfFile", "created successfully");
                result = fileExists = true;
            } catch (IOException e) {
                e.printStackTrace();
                if (Static.Debug && telemetry != null) telemetry.addData("CreatedCompConfFile", "failed to create");
                result = false;
            }
        }
        if (!loadAfter){
            return (result) ? ReturnValues.SUCCESS : ReturnValues.FAIL;     // Look up Ternary operator if you don't understand
        }else if (!result) {
            return ReturnValues.FAIL;
        }else{
            if (load()){
                if(verify()){
                    if (Static.Debug && telemetry != null) telemetry.addData("LoadedCompConFile", "default loaded");
                    return ReturnValues.SUCCESS;
                }
                else {
                    if (Static.Debug && telemetry != null) telemetry.addData("LoadedCompConFile", "failed to verify");
                    return ReturnValues.UNABLE_TO_VERIFY;
                }
            }
            else {
                if (Static.Debug && telemetry != null) telemetry.addData("LoadedCompConFile", "failed to load");
                return ReturnValues.UNABLE_TO_LOAD;
            }
        }
    }

    /**
     * Verify the stored values. True if verified, false if not.
     * Will replace bad values with defaults
     *
     * @return Verify state
     */
    @Override
    public boolean verify() {
        // TODO: 11/28/2015 Add a range checker
        return true;
    }

    /**
     * Loads values.
     *
     * @param loadDefault Whether to load from defaults or file.
     * @return Loaded successfully or not
     */
    @Override
    public boolean load(boolean loadDefault) {
        InputStream config;
        data = null;
        if (loadDefault) {
            try {
                config = Dynamic.globalAssets.open(fileName);
                if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "default selected");
            } catch (IOException e) {
                if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "failed to load default");
                e.printStackTrace();
                return false;
            }
        }
        else if (fileExists){
            try {
                config = new FileInputStream(configFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "file failed to load");
                return false;
            }
            if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "file selected");
        }
        else{
            return false;
        }
        if (config != null) {
            data = (Map<String, Object>) yaml.load(config);
            //todo fix line 228 because "failed to load string" is not the right message under this condition
            if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "failed to load");
            //****************NEW CODE****************
            //setNumber(objectNum, count("dc_motors", "motor"));
            //****************NEW CODE*****************
            if (data != null){
                if (Static.Debug && telemetry != null) telemetry.addData("LoadCompConfFile", "loaded");
                return true;
            }
        }
        return false;
    }

    //region Getters and Setters

    /**
     * Retrieves the object associated with the string
     *
     * @param key The string to search and retrieve the object from
     * @return The object mapped to the string
     */
    @Override
    public Object retrieve(String key) {
        if (data != null){
            return data.get(key);
        }
        return null;
    }

    /**
     * Stores or replaces the object associated with the string
     *
     * @param key    The string to search and store the object to
     * @param object The Object to store
     */
    @Override
    public boolean store(String key, Object object) {
        if (data != null){
            data.put(key, object);
        }
        return false;
    }
    //**********************New UNTESTED code************************

    //TODO: 12/7/2015 FIX THESE THINGS BECAUSE THEY WERE POORLY MADE

    //method for checking device type's existence
    public boolean exists(String deviceType){
        return data.containsKey(deviceType);
    }

    //method for checking string id's existence
    public boolean exists(String deviceType, String deviceName, Integer index){
        if (exists(deviceType)) {
            return (((Map) data.get(deviceType)).containsKey((deviceName)+(index.toString())));
        }
        else return false;
    }

    //check validity of a device
    public boolean valid(String deviceType, String deviceName, Integer index){
        if (exists(deviceType, deviceName, index)) {
            if (((Map) ((Map) data.get(deviceType)).get((deviceName) + index.toString())).get("enabled").equals(true)) {
                return true;
            } else {
                return false;
            }
        }
        else return false;
    }

    //count parts
    public Integer count(String device, String deviceName){
        int quantity=0;
        if (exists(device)) {
            for (Integer i = 1; i <= ((Map) data.get(device)).size(); i++) {
                if (valid(device, deviceName, i)) {
                    quantity++;
                }
            }
        }
        return quantity;
    }

    //determine max
    public Integer determineMax(String device){
        if (exists(device)){
            return ((Map) data.get(device)).size();
        }
        else return 0;
    }

    //Assigns map name to string
    public Object assignObject(String deviceType, String deviceName, Integer deviceRef){
        //if (exists(deviceType, deviceName, deviceRef))
        return ((Map)((Map)retrieve(deviceType)).get((deviceName)+(deviceRef.toString()))).get("map_name").toString();
    }

    //setters
    public void setNumber(Integer variable, int value){
        variable = value;
    }

    //getters
    public Integer getObjectNumber(){
        return objectNum;
    }
    public Integer getmaxNumber(){
        return maxNum;
    }
    //*******************************END New Untested Code*******************************

    //endregion
}
