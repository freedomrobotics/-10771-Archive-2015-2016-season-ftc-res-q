package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Create the actual references to the components based on the configuration
 */
public class InitComp {

    HardwareMap hardwareMap = null;

    Components components = null;

    Telemetry telemetry = null;

    public InitComp (HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        components = new Components(telemetry);
        if (!components.load()){
            components.create();
        }
    }

    public ReturnValues initialize(){
        /*
        Integer count = 0;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("enabled").equals(true)) count++;
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("enabled").equals(true)) count++;

        //Sorry Joel! You were right to create the references to the array.
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("enabled").equals(true)){
            Core.motor[0] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor1")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("enabled").equals(true)){
            Core.motor[1] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor2")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor3")).get("enabled").equals(true)){
            Core.motor[2] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor3")).get("map_name").toString());
        }
        if (((Map)((Map)components.retrieve("dc_motors")).get("motor4")).get("enabled").equals(true)){
            Core.motor[3] = hardwareMap.dcMotor.get(((Map)((Map)components.retrieve("dc_motors")).get("motor4")).get("map_name").toString());
        }
        */
        //***************new untested code*******************
        //TODO: 12/14/2015 Make components.count() have shorter hardwareMapping or DYNAMIC parameters
        //TODO: 12/14/2015 Consider putting Array initializations under objectInit
        Core.motor = new DcMotor[components.count(mappedType(hardwareMap.dcMotor))];
        objectInit(hardwareMap.dcMotor, Core.motor);
        Core.servo = new Servo[components.count(mappedType(hardwareMap.servo))];
        objectInit(hardwareMap.servo, Core.servo);
        //**************new code******************
        return ReturnValues.SUCCESS;
    }

    //TODO: 12/7/2015 make each private variable in Components.java work
    //todo            in a way that makes a unique value for each device type
    //TODO: 12/8/2015 FIX THE REALLY BAD PARAMETER NAMES JOEL!!!!!
    //TODO: 12/9/2015 There might be some redundant "if (exists())" statements
    //TODO: 12/11/2015 Make objectInit require less arguments
    /**
     * Initializes each individual object type
     * @param devices   The array to assign to
     * @return ReturnValues whether or not the method succeeded
     */
    public ReturnValues objectInit(HardwareMap.DeviceMapping deviceMapping, Object devices[]){
        String deviceType = mappedType(deviceMapping);
        if (components.deviceExists(deviceType)){
            for (int i = 0; i < components.count(deviceType); i++) {
                int id = i + 1;
                int max = components.maxSubdevices(deviceType);
                while ((!(components.deviceEnabled(deviceType, id)) && (id <= max))) {
                    id++;
                }
                if (components.deviceEnabled(deviceType, id)) {
                    devices[i] = deviceMapping.get(components.getMapName(deviceType, id));
                    // FIXME: 12/15/2015 Add a hook into the HardwareMap class in order to not throw a failure on component load failure
                } else {
                    //Returns FAIL if there are not any existing enabled device keys to initialize the array elements with
                    return ReturnValues.FAIL;
                }
            }
            return ReturnValues.SUCCESS;
        }
        else return ReturnValues.FAIL;
    }

    //TODO: 12/14/2015 Change method to case statements or better yet, mappings(Map<Map, String>)(make DYNAMIC return values)
    //TODO: 12/14/2015 Figure out a way to make these comparisons under objectKey() function // FIXME: 12/15/2015 Moved to the components class by adam
    //Method for retrieving string from hardware maps
    private String mappedType(HardwareMap.DeviceMapping deviceMap){
        if (deviceMap == hardwareMap.dcMotor) return "dc_motors";
        else if (deviceMap == hardwareMap.servo) return "servos";
        else if (deviceMap == hardwareMap.touchSensor) return "touch_sensors";
        else if (deviceMap == hardwareMap.lightSensor) return "light_sensors";
        else if (deviceMap == hardwareMap.colorSensor) return "color_sensors";
        else if (deviceMap == hardwareMap.irSeekerSensor) return "ir_seekers";
        else if (deviceMap == hardwareMap.gyroSensor) return "gyrometers";
        else if (deviceMap == hardwareMap.accelerationSensor) return "accelerometers";
        //(deviceMap for camera does not exist)
        else return "null"; //if no map matches any above return null as string
    }

    /* I got distracted, and I will finish this in next commit
    // Simply stores a key to all the names in the alias.
    private void setAlias(String deviceType, Object devices[], Map<String, Object> alias){
        //for loop for each device type.
        for (int x = 0; x < components.count(deviceType); x++) {
            for (int i = 0; i < components.getSubdevice(deviceType, x)
            Aliases.motorMap.put(, Core.motor[x]);
        }
    }

/*
    //TODO: 12/11/2015 Alias function attempt is a fail:( To be deleted or revised
    private void alias(String deviceType, String deviceName, Integer deviceRef, Object device, HashMap<String, Object> aliasMap) {
        Object[] array;
        aliasMap.putAll(((Map) ((Map) ((Map) components.retrieve(deviceType)).get((deviceName) + (deviceRef.toString()))).get("alias")));
        array = aliasMap.keySet().toArray();
        for (int i=0; i<aliasMap.size(); i++){
            aliasMap.put(array[i].toString(), device);
        }
    }
    */

    public Components getComponents(){return components;}

    public void Run(){

    }
}
