package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.exception.RobotCoreException;

/**
 * This class will initialize objects by using the information
 * obtained from HardwareInfo.java and variables from Core.java.
 * Created by joelv on 12/2/2015.
 */
public class Create {

    Create(){
        //Default constructor
    }

    private HardwareDeviceManager hardwareDeviceManager;
    private boolean DeviceInterfaceExists = false;
    private boolean LegacyExists = false;
    private boolean MotorConExists = false;
    private boolean ServoConExists = false;

    public boolean deviceInit(HardwareInfo device) throws RobotCoreException, InterruptedException {
        //TODO: Account for exceptions and errors that would return false for this method

        //For modules only
        if (device.deviceType==HardwareInfo.TYPE.MODULE){

            //Device Interface Module initialization
            if (device.deviceName==HardwareInfo.DEVICE.DEVICE_INTERFACE_MODULE){
                Core.mainInterface = hardwareDeviceManager.createDeviceInterfaceModule(device.modSerial);
                DeviceInterfaceExists = true;
            }

            //Legacy Module
            else if (device.deviceName==HardwareInfo.DEVICE.LEGACY_MODULE && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.legacyModule = hardwareDeviceManager.createUsbLegacyModule(device.modSerial);
                LegacyExists = true;
            }

            //Modern Robotics Motor Controller
            else if (device.deviceName==HardwareInfo.DEVICE.MOTOR_CONTROLLER && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.motorControl = hardwareDeviceManager.createUsbDcMotorController(device.modSerial);
                MotorConExists = true;
            }

            //NXT Motor Controller
            else if (device.deviceName==HardwareInfo.DEVICE.MOTOR_CONTROLLER && device.deviceModel==HardwareInfo.MODEL.NXT && LegacyExists){
                Core.motorControl = hardwareDeviceManager.createNxtDcMotorController(Core.legacyModule, device.portNumber);
                MotorConExists = true;
            }

            //MR Servo Controller
            else if (device.deviceName==HardwareInfo.DEVICE.SERVO_CONTROLLER && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.servoControl = hardwareDeviceManager.createUsbServoController(device.modSerial);
                ServoConExists = true;
            }

            //NXT Servo Controller
            else if (device.deviceName==HardwareInfo.DEVICE.SERVO_CONTROLLER && device.deviceModel==HardwareInfo.MODEL.NXT && LegacyExists){
                Core.servoControl = hardwareDeviceManager.createNxtServoController(Core.legacyModule, device.portNumber);
                ServoConExists = true;
            }

            else{
                //Something happened
                return false;
            }
        }

        //For sensors only
        else if (device.deviceType== HardwareInfo.TYPE.SENSOR){
            //TODO: Account for different errors

            //If device is not connected to correct module
            if ((device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS && !DeviceInterfaceExists) ||
                    (device.deviceModel== HardwareInfo.MODEL.NXT && !LegacyExists)){
                //Add to telemetry that device cannot be established
                return false;
            }

            //Touch Sensors
            if (device.deviceName==HardwareInfo.DEVICE.TOUCH_SENSOR && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.touchSensor[device.deviceIndex] = hardwareDeviceManager.createDigitalTouchSensor(Core.mainInterface, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.TOUCH_SENSOR && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.touchSensor[device.deviceIndex] = hardwareDeviceManager.createNxtTouchSensor(Core.legacyModule, device.portNumber);
            }

            //Light Sensors
            else if (device.deviceName==HardwareInfo.DEVICE.LIGHT_SENSOR && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.lightSensor[device.deviceIndex] = hardwareDeviceManager.createAnalogOpticalDistanceSensor(Core.mainInterface, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.LIGHT_SENSOR && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.lightSensor[device.deviceIndex] = hardwareDeviceManager.createNxtLightSensor(Core.legacyModule, device.portNumber);
            }

            //IR Seekers
            else if (device.deviceName==HardwareInfo.DEVICE.IR_SEEKER && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.irSeeker[device.deviceIndex] = hardwareDeviceManager.createI2cIrSeekerSensorV3(Core.mainInterface, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.IR_SEEKER && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.irSeeker[device.deviceIndex] = hardwareDeviceManager.createNxtIrSeekerSensor(Core.legacyModule, device.portNumber);
            }

            //Gyros
            else if (device.deviceName==HardwareInfo.DEVICE.GYRO && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.gyro[device.deviceIndex] = hardwareDeviceManager.createModernRoboticsI2cGyroSensor(Core.mainInterface, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.GYRO && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.gyro[device.deviceIndex] = hardwareDeviceManager.createNxtGyroSensor(Core.legacyModule, device.portNumber);
            }

            //Accelerometer
            else if (device.deviceName==HardwareInfo.DEVICE.ACCELERATION_SENSOR && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.accelerometer[device.deviceIndex] = hardwareDeviceManager.createNxtAccelerationSensor(Core.legacyModule, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.COLOR_SENSOR && device.deviceModel==HardwareInfo.MODEL.ADAFRUIT){
                Core.colorSensor[device.deviceIndex] = hardwareDeviceManager.createModernRoboticsI2cColorSensor(Core.mainInterface, device.portNumber);
            }

            //Color Sensors
            else if (device.deviceName==HardwareInfo.DEVICE.COLOR_SENSOR && device.deviceModel==HardwareInfo.MODEL.MODERN_ROBOTICS){
                Core.colorSensor[device.deviceIndex] = hardwareDeviceManager.createModernRoboticsI2cColorSensor(Core.mainInterface, device.portNumber);
            }
            else if (device.deviceName==HardwareInfo.DEVICE.COLOR_SENSOR && device.deviceModel==HardwareInfo.MODEL.NXT){
                Core.colorSensor[device.deviceIndex] = hardwareDeviceManager.createNxtColorSensor(Core.legacyModule, device.portNumber);
            }

            //If nothing else
            else{
                //Add to telemetry that something happened
                return false;
            }

        }

        //If device is a motor
        else if (device.deviceType==HardwareInfo.TYPE.MECHANICAL){
            if ((device.deviceName==HardwareInfo.DEVICE.MOTOR && !MotorConExists) ||
                    (device.deviceName==HardwareInfo.DEVICE.SERVO && !ServoConExists)){
                //Add to telemetry that its not possible
                return false;
            }
            if (device.deviceName==HardwareInfo.DEVICE.MOTOR){
                Core.motor[device.deviceIndex] = hardwareDeviceManager.createDcMotor(Core.motorControl, device.portNumber);
            }
            else if (device.deviceName== HardwareInfo.DEVICE.SERVO){
                Core.servo[device.deviceIndex] = hardwareDeviceManager.createServo(Core.servoControl, device.portNumber);
            }
        }

        //Add data to telemetry that object is created successfully
        return true;
    }
}
