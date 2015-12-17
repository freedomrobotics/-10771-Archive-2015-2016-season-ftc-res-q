package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.core;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Core;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config.Components;
import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.vars.ReturnValues;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Create the actual references to the components based on the configuration
 */
public class InitComp {

    HardwareMap hardwareMap = null;

    Components components = null;

    Telemetry telemetry = null;

    public InitComp (HardwareMap hardwareMap, Telemetry telemetry, Components components){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.components = components;
    }

    public ReturnValues initialize(){
        //TODO: 12/14/2015 Make components.count() have shorter hardwareMapping or DYNAMIC parameters
        //TODO: 12/14/2015 Consider putting Array initializations under objectInit
        Core.motor = new DcMotor[components.count(mappedType(hardwareMap.dcMotor))];
        if (objectInit(hardwareMap.dcMotor, Core.motor) != ReturnValues.SUCCESS){
            return ReturnValues.MOTOR_NOT_INIT;
        }
        Core.servo = new Servo[components.count(mappedType(hardwareMap.servo))];
        if (objectInit(hardwareMap.servo, Core.servo) != ReturnValues.SUCCESS){
            return ReturnValues.SERVO_NOT_INIT;
        }
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
                if (components.deviceEnabled(deviceType, id) && componentExists(components.getMapName(deviceType, id), deviceMapping)) {
                    devices[i] = deviceMapping.get(components.getMapName(deviceType, id));
                    setAlias(deviceType, devices[i], aliasMap(deviceMapping), id);
                } else {
                    //Returns FAIL if there are not any existing enabled device keys to initialize the array elements with
                    return ReturnValues.FAIL;

                }
            }
            return ReturnValues.SUCCESS;
        }
        return ReturnValues.DEVICE_DOES_NOT_EXIST;
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

    /**
     * A method derived from the HardwareMap.DeviceMapping Class to determine if a named component exists.
     * @param mapName       The name of the mapping to check
     * @param deviceMapping The HardwareMap.DeviceMapping object used in the OpMode
     * @return  A boolean stating whether the component exists or not.
     */
    private boolean componentExists(String mapName, HardwareMap.DeviceMapping deviceMapping){
        // Now this is a little complex.
        // So I decompiled the HardwareMap class to figure this out since there is no built in way to check
        // First I call to get the entry set of the map in the class, which similar to a list of unique entries of map key/values
        // Then I call iterator on it to run through each entry.
        Iterator devices = deviceMapping.entrySet().iterator();
        // From here I say, while there is another entry in the set, do...
        while (devices.hasNext()){
            // This! I pull out the entry from the iterator and run a check on it
            Map.Entry entry = (Map.Entry)devices.next();
            // If it's a HardwareDevice and the mapName and the key of the entry have the same content
            if(entry.getValue() instanceof HardwareDevice && mapName.equals(entry.getKey())){
                // Then the component exists on the hardwareMap and we don't have to perform a non-remote robot restart because of annoying exceptions.
                return true;
            }
        }
        // Otherwise the component doesn't exist and we return a failure.
        return false;
    }

    /**
     * A method to store to the alias map. Figures out which aliases to get based on the rule that the device name is either the same or without an extra s
     * @param deviceType    The type of the device
     * @param device        The object to the device
     * @param alias         The appropriate alias map
     * @param id            The id of the device
     */
    private void setAlias(String deviceType, Object device, Map<String, Object> alias, Integer id){
        //for loop for each alias.
        for (int i = 0; i < components.getAlias(deviceType, id).size(); i++){
            alias.put(components.getAlias(deviceType, id).get(i), device);
        }
    }

    /**
     * A method to test and return the respective map based off of the mappedType method
     * @param deviceMap     The HardwareMap.DeviceMapping object / The device map
     * @return  The appropriate alias Map
     */
    private Map aliasMap(HardwareMap.DeviceMapping deviceMap){
        if (deviceMap == hardwareMap.dcMotor)               return Aliases.motorMap;
        if (deviceMap == hardwareMap.servo)                 return Aliases.servoMap;
        if (deviceMap == hardwareMap.touchSensor)           return Aliases.touchSensorMap;
        if (deviceMap == hardwareMap.lightSensor)           return Aliases.lightSensorMap;
        if (deviceMap == hardwareMap.colorSensor)           return Aliases.colorSensorMap;
        if (deviceMap == hardwareMap.irSeekerSensor)        return Aliases.irSeekerMap;
        if (deviceMap == hardwareMap.gyroSensor)            return Aliases.gyrometerMap;
        if (deviceMap == hardwareMap.accelerationSensor)    return Aliases.accelerometerMap;
            //(deviceMap for camera does not exist)
        return null;
    }

    public Components getComponents(){return components;}
}
