package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components;

import com.qualcomm.hardware.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.exception.RobotCoreException;

/**
 * This class will initialize objects, corresponding to hardware and model,
 * and assign them to the abstract reference.
 * Created by joelv on 12/2/2015.
 */
public class Create {

    Create(){
        //Default constructor
    }

    public static HardwareDeviceManager hardwareDeviceManager;

    DeviceInterfaceModule mainInterface;
    LegacyModule createdLegacy;
    DcMotorController createdMotorControl;
    ServoController createdServoControl;
    //TODO: Assign these
    public void initializeModules() throws RobotCoreException, InterruptedException {
        mainInterface = hardwareDeviceManager.createDeviceInterfaceModule(ConfigVars.interfaceSerial);
        createdLegacy = hardwareDeviceManager.createUsbLegacyModule(ConfigVars.legacySerial);
        createdMotorControl = hardwareDeviceManager.createUsbDcMotorController(ConfigVars.motorConSerial);
        createdServoControl = hardwareDeviceManager.createUsbServoController(ConfigVars.servoConSerial);
    }

    TouchSensor createdTouchSensor;
    LightSensor createdLightSensor;
    ColorSensor createdColorSensor;
    IrSeekerSensor createdIRSeeker;
    GyroSensor createdGyro;
    AccelerationSensor createdAccelerometer;

    //TODO: Make the class handle both models
    //TODO: Make light sensor and accelerometer handle non-abstract object declaration
    //TODO: (Idea1)Make Create the abstract class, while having NXTCreate and ModRobCreate extend the class
    //TODO: (Idea2)Use enumerations to initialize and assign each individual device according to its model.
    public void initializeSensors() throws RobotCoreException, InterruptedException{
        Core.ModRobTouchSensor = new ModernRoboticsDigitalTouchSensor(mainInterface, ConfigVars.touchPort);
        //Light Sensor
        Core.ModRobirSeeker = new ModernRoboticsI2cIrSeekerSensorV3(mainInterface, ConfigVars.irPort);
        Core.ModRobGyro = new ModernRoboticsI2cGyro(mainInterface, ConfigVars.gryoPort);
        //Accelerometer
        Core.AdaFruitColorSensor = new AdafruitI2cColorSensor(mainInterface, ConfigVars.colorPort);
    }

    public void assignSensors(){
        Core.ModRobTouchSensor = new ModernRoboticsDigitalTouchSensor(mainInterface, 1);
        createdTouchSensor = Core.ModRobTouchSensor;
        //Light Sensor assign missing
        createdIRSeeker = Core.ModRobirSeeker;
        createdGyro = Core.ModRobGyro;
        //Accelerometer assignment missing
        createdColorSensor = Core.AdaFruitColorSensor;
    }


    //hypothetical steps to creating an object of the right model
    //1. determine model
    //2. determine device
    //3. create device of correct
    //4. assign it to the reference variable of the right class

    //different characteristics of a device
    // module/controller vs. sensor vs. motor
    // NXT vs. ModernRobotics
    // Port number vs. serial number
    //
}
