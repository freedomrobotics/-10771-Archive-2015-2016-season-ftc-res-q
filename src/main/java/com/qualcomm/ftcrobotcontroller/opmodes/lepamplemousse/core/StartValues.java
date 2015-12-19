package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Variables;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Initial reset values. Should include the default/starting values loaded
 * from the config or other places and set all variables to such.
 */
public class StartValues {

    Variables variables = null;

    Map<String, Settings> objects = new HashMap<String, Settings>();

    public StartValues (Variables variables){
        this.variables = variables;
    }

    public class Settings{
        Map<String, Object> data = new HashMap<String, Object>();

        public void addToMap(String key, Object object) {
            data.remove(key);
            if (key != null) {
                data.put(key, object);
            }
        }

        public void addToMap(Map<String, Object> map) {
            if (map != null) {
                data.putAll(map);
            }
        }

        public Object get(String key) {
            if (valueExists(key)){
                return data.get(key);
            }
            return null;
        }

        public void remove(String key) {
            data.remove(key);
        }

        public boolean valueExists(String key) {
            return data.containsKey(key);
        }

        public Object returnObject(String key) {
            if(valueExists(key)){
                return data.get(key);
            }
            return null;
        }

        public boolean returnBool(String key) {
            if(valueExists(key)){
                return data.get(key).toString().equals("true");
            }
            return false;
        }

        public String returnString(String key) {
            if(valueExists(key)){
                return data.get(key).toString();
            }
            return null;
        }

        public int returnInt(String key) {
            if(valueExists(key)){
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(data.get(key).toString());
                if(m.find()){
                    return 0;
                }
                if (data.get(key).toString().contains(".")){
                    return ((Double)data.get(key)).intValue();
                }
                return (Integer)data.get(key);
            }
            return 0;
        }

        public float returnFloat(String key) {
            if(valueExists(key)){
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(data.get(key).toString());
                if(m.find()){
                    return 0;
                }
                return ((Double)data.get(key)).floatValue();
            }
            return 0;
        }

        public Settings returnSettings(String key){
            if(valueExists(key)){
                Pattern p = Pattern.compile("[{}]]");
                Matcher m = p.matcher(data.get(key).toString());
                if(m.find()) {
                    return (Settings) data.get(key);
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public ReturnValues initialize(){
        Iterator settings = variables.getEntrySet().iterator();
        while (settings.hasNext()){
            Map.Entry settingObject = (Map.Entry)settings.next();
            if(iterateOverValues(settingObject.getValue())){
                continue;
            }
            objects.put((String)settingObject.getKey(), new Settings(){);

        }
    }

    private boolean iterateOverValues(Object object){
        Pattern p = Pattern.compile("[{}]]");
        Matcher m = p.matcher(object.toString());
        if(m.find()) {
            Iterator values = ((Map)object).entrySet().iterator();
            while (values.hasNext()){
                Map.Entry valueObject = (Map.Entry)values.next();

            }
        }
    }

    public Settings object(String name){
        if (objects.containsKey("name")){{
            return objects.get("name");
        }}
        return null;
    }

    /*public ReturnValues loadFromConfig(){
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

    }*/

}
