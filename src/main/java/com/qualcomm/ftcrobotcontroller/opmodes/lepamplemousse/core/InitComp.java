package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.HashMap;
import java.util.Map;

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
        Core.motor = new DcMotor[components.count("dc_motors", "motor")];
        objectInit("dc_motors", "motor", hardwareMap.dcMotor, Core.motor);
        Core.servo = new Servo[components.count("servos", "servo")];
        objectInit("servos", "servo", hardwareMap.servo, Core.servo);
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
     * @param deviceType The group of devices to assign
     * @param devices   The array to assign to
     * @return ReturnValues whether or not the method succeeded
     */
    public ReturnValues objectInit(String deviceType, String deviceName, HardwareMap.DeviceMapping deviceMapping, Object devices[]){
        if (components.exists(deviceType)){
            for (int i = 0; i < components.count(deviceType, deviceName); i++) {
                int reference = i + 1;
                int max = components.determineMax(deviceType);
                while ((!(components.valid(deviceType, deviceName, reference)) && (reference <= max))) {
                    reference++;
                }
                if (components.valid(deviceType, deviceName, reference)) {
                    devices[i] = assignObject(deviceType, deviceName, reference, deviceMapping);
                } else {
                    return ReturnValues.FAIL;
                }
            }
            return ReturnValues.SUCCESS;
        }
        else return ReturnValues.FAIL;
    }

    private Object assignObject(String deviceType, String deviceName, Integer deviceRef, HardwareMap.DeviceMapping deviceMapping){
        //if (exists(deviceType, deviceName, deviceRef))
        return deviceMapping.get(((Map)((Map)components.retrieve(deviceType)).get((deviceName)+(deviceRef.toString()))).get("map_name").toString());
    }


    //TODO: 12/11/2015 Alias function attempt is a fail:( To be deleted or revised
    private void alias(String deviceType, String deviceName, Integer deviceRef, Object device, HashMap<String, Object> aliasMap) {
        Object[] array;
        aliasMap.putAll(((Map) ((Map) ((Map) components.retrieve(deviceType)).get((deviceName) + (deviceRef.toString()))).get("alias")));
        array = aliasMap.keySet().toArray();
        for (int i=0; i<aliasMap.size(); i++){
            aliasMap.put(array[i].toString(), device);
        }
    }

    public Components getComponents(){return components;}

    public void Run(){

    }
}
