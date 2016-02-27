package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.AtomFunctionsTake2;
import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps.MapLoader;
import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps.Maps;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Components;
import org.fhs.robotics.ftcteam10771.lepamplemousse.config.Variables;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.InitComp;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.StartValues;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.components.Aliases;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.ReturnValues;

import java.util.List;
import java.util.Map;

/**
 * Created by Adam Li on 2/18/2016.
 */
public class RobotAutoTake2 extends LinearOpMode{
    Components components = null;
    ReturnValues returnValues;
    Variables variables = null;
    StartValues values = null;
    InitComp initComp = null;
    AtomFunctionsTake2 atomFunctions = null;
    boolean reset_config;
    boolean red_alliance = false;

    private float servo_pos;

    float armVert_pos = 0.0f;
    float armSweep_pos = 0.0f;


    @Override
    public void runOpMode() throws InterruptedException {
        //load the components object and check for existence
        components = new Components(telemetry);
        if (!components.load()) {
            components.create();
        }

        // initialize all the components
        // and run the checks
        initComp = new InitComp(hardwareMap, telemetry, components);
        if ((returnValues = initComp.initialize()) != ReturnValues.SUCCESS) {
            if (returnValues == ReturnValues.MOTOR_NOT_INIT) {
                telemetry.addData("ERROR", "Motors Failed to Initialize");
            } else if (returnValues == ReturnValues.SERVO_NOT_INIT) {
                telemetry.addData("ERROR", "Servos Failed to Initialize");
            } else {
                telemetry.addData("ERROR", "Something wrong happened!");
            }
        }

        sleep(500);

        //set default values
        //load the variables object and check for existence
        variables = new Variables(telemetry);
        if (!variables.load()) {
            variables.create();
        }

        //Load all the variables from the configuration
        values = new StartValues(variables, telemetry);
        values.initialize();

        sleep(500);


        Maps fieldMap = fieldMap();
        for (int x = 0; fieldMap == null && x < 3; x++){
            fieldMap = fieldMap();
        }
        if (fieldMap == null)
            return;

        atomFunctions = new AtomFunctionsTake2(values, fieldMap, this, telemetry);

        telemetry.addData("field_map", fieldMap.toString());

        servoSetup();

        //PREP THE COMMAND LIST!
        if (values.settings("autonomous").getObject("command_list") == null)
            return;

        List<String> commandList = (List<String>) values.settings("autonomous").getObject("command_list");

        int counter = 1; //debug

        atomFunctions.setServoPos(servo_pos);

        //WAIT FOR THE STARTI!@HTIOgRFOIWUQ#GQIOH
        waitForStart();

        while(opModeIsActive()){
            for(String command : commandList){
                //check if opmode is active
                if (!opModeIsActive()) break;

                AtomFunctionsTake2.CommandParser commandParser = new AtomFunctionsTake2.CommandParser(command);

                commandPicker(commandParser);

                if (commandParser.command().equalsIgnoreCase("stop")){
                    cleanUp();
                    return;
                }

                //Debug section
                telemetry.addData("field_map", fieldMap.toString());
                telemetry.addData(counter + "", command);
                counter++;
            }
        }
        cleanUp();
    }

    //CHEAP NAMES
    private void commandPicker(AtomFunctionsTake2.CommandParser commandParser) throws InterruptedException {
        /**
         * move distance
         * move pointX,pointY
         */
        if (commandParser.command().equalsIgnoreCase("move")){
            if (commandParser.getArgsSize() == 1)
                atomFunctions.move(commandParser.getArgFloat(0));
            if (commandParser.getArgsSize() >= 2)
                atomFunctions.move(commandParser.getArgFloat(0), commandParser.getArgFloat((1)));
            return;
        }
        /**
         * moveTime time_in_millis
         */
        if (commandParser.command().equalsIgnoreCase("moveTime")){
            atomFunctions.drive(1);
            sleep(commandParser.getArgInt(0));
            atomFunctions.drive(0);
            return;
        }
        /**
         * rotate degrees
         * rotate pointX,pointY
         */
        if (commandParser.command().equalsIgnoreCase("rotate")){
            if (commandParser.getArgsSize() == 1)
                atomFunctions.rotate(commandParser.getArgFloat(0));
            if (commandParser.getArgsSize() >= 2)
                atomFunctions.rotate(commandParser.getArgFloat(0), commandParser.getArgFloat(1));
            return;
        }
        /**
         * winchMotor in/out,time_in_millis
         */
        if (commandParser.command().equalsIgnoreCase("winchMotor")){
            if (commandParser.getArgsSize() >= 2) {
                if (commandParser.getArgString(0).equalsIgnoreCase("in"))
                    Aliases.motorMap.get("winch").setPower(-1);
                else
                    Aliases.motorMap.get("winch").setPower(1);
                sleep(commandParser.getArgInt(1));
                Aliases.motorMap.get("winch").setPower(0);
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("winchServo")){
            if (commandParser.getArgsSize() == 1)
                atomFunctions.winchServo(commandParser.getArgFloat(0));
            if (commandParser.getArgsSize() >= 2)
                atomFunctions.winchServo(commandParser.getArgString(0).equalsIgnoreCase("up"), commandParser.getArgFloat(1));
            return;
        }
        if (commandParser.command().equalsIgnoreCase("travel_to_beacon")){
            StartValues.Settings settings = values.settings("autonomous").getSettings("blue_alliance");
            if (red_alliance)
                settings = values.settings("autonomous").getSettings("red_alliance");
            if (settings.getObject("drop_off_people") == null)
                return;

            List<String> commandList = (List<String>) settings.getObject("drop_off_people");
            for(String command : commandList) {
                if (!opModeIsActive()) break;

                AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                commandPicker(internalScript);

                if (internalScript.command().equalsIgnoreCase("stop")){
                    return;
                }
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("drop_off_people")){
            if (values.settings("autonomous").getObject("drop_off_people") == null)
                return;

            List<String> commandList = (List<String>) values.settings("autonomous").getObject("drop_off_people");
            for(String command : commandList) {
                if (!opModeIsActive()) break;

                AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                commandPicker(internalScript);

                if (internalScript.command().equalsIgnoreCase("stop")){
                    return;
                }
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("travel_to_park")){
            StartValues.Settings settings = values.settings("autonomous").getSettings("blue_alliance");
            if (red_alliance)
                settings = values.settings("autonomous").getSettings("red_alliance");
            if (settings.getObject("travel_to_park") == null)
                return;

            List<String> commandList = (List<String>) settings.getObject("travel_to_park");
            for(String command : commandList) {
                if (!opModeIsActive()) break;

                AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                commandPicker(internalScript);

                if (internalScript.command().equalsIgnoreCase("stop")){
                    return;
                }
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("travel_to_low")){
            if (commandParser.getArgsSize() == 1) {
                StartValues.Settings settings = values.settings("autonomous").getSettings("blue_alliance");
                if (red_alliance)
                    settings = values.settings("autonomous").getSettings("red_alliance");

                if (commandParser.getArgString(0).equalsIgnoreCase("near")) {
                    settings = settings.getSettings("near");
                    if (settings.getObject("travel_to_low") == null)
                        return;

                    List<String> commandList = (List<String>) settings.getObject("travel_to_low");
                    for (String command : commandList) {
                        if (!opModeIsActive()) break;

                        AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                        commandPicker(internalScript);

                        if (internalScript.command().equalsIgnoreCase("stop")) {
                            return;
                        }
                    }
                }

                if (commandParser.getArgString(0).equalsIgnoreCase("far")) {
                    settings = settings.getSettings("far");
                    if (settings.getObject("travel_to_low") == null)
                        return;

                    List<String> commandList = (List<String>) settings.getObject("travel_to_low");
                    for (String command : commandList) {
                        if (!opModeIsActive()) break;

                        AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                        commandPicker(internalScript);

                        if (internalScript.command().equalsIgnoreCase("stop")) {
                            return;
                        }
                    }
                }
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("travel_to_mid")){
            if (commandParser.getArgsSize() == 1) {
                StartValues.Settings settings = values.settings("autonomous").getSettings("blue_alliance");
                if (red_alliance)
                    settings = values.settings("autonomous").getSettings("red_alliance");

                if (commandParser.getArgString(0).equalsIgnoreCase("near")) {
                    settings = settings.getSettings("near");
                    if (settings.getObject("travel_to_mid") == null)
                        return;

                    List<String> commandList = (List<String>) settings.getObject("travel_to_mid");
                    for (String command : commandList) {
                        if (!opModeIsActive()) break;

                        AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                        commandPicker(internalScript);

                        if (internalScript.command().equalsIgnoreCase("stop")) {
                            return;
                        }
                    }
                }

                if (commandParser.getArgString(0).equalsIgnoreCase("far")) {
                    settings = settings.getSettings("far");
                    if (settings.getObject("travel_to_mid") == null)
                        return;

                    List<String> commandList = (List<String>) settings.getObject("travel_to_mid");
                    for (String command : commandList) {
                        if (!opModeIsActive()) break;

                        AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

                        commandPicker(internalScript);

                        if (internalScript.command().equalsIgnoreCase("stop")) {
                            return;
                        }
                    }
                }
            }
            return;
        }
        if (commandParser.command().equalsIgnoreCase("clear_arm")){
            atomFunctions.clearArm();
            return;
        }
        if (commandParser.command().equalsIgnoreCase("pause")){
            sleep(commandParser.getArgInt(0));
            return;
        }

        if (values.settings("autonomous").getObject(commandParser.command()) == null)
            return;

        List<String> commandList = (List<String>) values.settings("autonomous").getObject(commandParser.command());
        for(String command : commandList) {
            if (!opModeIsActive()) break;

            AtomFunctionsTake2.CommandParser internalScript = new AtomFunctionsTake2.CommandParser(command);

            commandPicker(internalScript);

            if (internalScript.command().equalsIgnoreCase("stop")){
                return;
            }
        }
        return;
    }

    private void cleanUp(){
        //it is over
        initComp.cleanUp();
        Aliases.clearAll();
    }

    /**
     * Sets up the servos
     */
    private void servoSetup() {
        if (values.settings("winch").getSettings("left_servo").getBool("reversed")){
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.REVERSE);
        }else{
            Aliases.servoMap.get("winch_left").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("winch").getSettings("right_servo").getBool("reversed")){
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("winch_right").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("trigger_arm").getString("side").equals("right")){
            //Aliases.servoMap.get("arm_trigger").setDirection(Servo.Direction.REVERSE);
        }else{
            //Aliases.servoMap.get("arm_trigger").setDirection(Servo.Direction.FORWARD);
        }
        //if (values.settings("plow").getBool("reversed")){
        //    Aliases.servoMap.get("plow").setDirection(Servo.Direction.REVERSE);
        //}else{
        //    Aliases.servoMap.get("plow").setDirection(Servo.Direction.FORWARD);
        //}


        StartValues.Settings winch = values.settings("winch");
        StartValues.Settings angular = winch.getSettings("angular_movement");
        float range = angular.getFloat("full_rotate");
        servo_pos = angular.getFloat("start_pos") / range;
        Aliases.servoMap.get("winch_left").setPosition(servo_pos + winch.getSettings("left_servo").getFloat("offset") / range);
        Aliases.servoMap.get("winch_right").setPosition(servo_pos + winch.getSettings("right_servo").getFloat("offset") / range);



        if (values.settings("robot_arm").getSettings("sweep_servo").getBool("reversed")){
            Aliases.servoMap.get("arm_side").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("arm_side").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("robot_arm").getSettings("vert_servo").getBool("reversed")){
            Aliases.servoMap.get("arm_up").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("arm_up").setDirection(Servo.Direction.FORWARD);
        }
        if (values.settings("all_clear").getBool("reversed")){
            Aliases.servoMap.get("arm_up").setDirection(Servo.Direction.REVERSE);
        } else {
            Aliases.servoMap.get("arm_up").setDirection(Servo.Direction.FORWARD);
        }

        StartValues.Settings sweep = values.settings("robot_arm").getSettings("sweep_servo");
        StartValues.Settings vert = values.settings("robot_arm").getSettings("vert_servo");

        range = sweep.getFloat("full_rotate");
        armSweep_pos = sweep.getFloat("start_pos") / range;
        range = vert.getFloat("full_rotate");
        armVert_pos = vert.getFloat("start_pos") / range;

        Aliases.servoMap.get("arm_up").setPosition(armVert_pos);
        Aliases.servoMap.get("arm_side").setPosition(armSweep_pos);


        StartValues.Settings a = values.settings("all_clear");
        float fullRange = a.getFloat("full_rotate");
        float offset = a.getFloat("offset") / fullRange;
        float up = a.getFloat("up_angle") / fullRange;
        Aliases.servoMap.get("allclear").setPosition(up + offset);

        //Aliases.motorMap.get("trigger_arm").setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public Maps fieldMap(){
        Maps fieldMap = new MapLoader("fieldmap");
        if (!((MapLoader)fieldMap).mapLoaded)
            return null;

        float robotX, robotY, robotRot;

        red_alliance = false;
        if (values.settings("trigger_arm").getString("side").equals("right")){
            //red alliance, left half
            red_alliance = true;
        }
        StartValues.Settings autonomous = values.settings("autonomous").getSettings("blue_alliance");
        if (red_alliance){
            autonomous = values.settings("autonomous").getSettings("red_alliance");
        }
        robotX = autonomous.getSettings("starting_pos").getFloat("x");
        robotY = autonomous.getSettings("starting_pos").getFloat("y");
        robotRot = autonomous.getSettings("starting_pos").getFloat("rot");

        fieldMap.getRobot().getPosition().setX(robotX);
        fieldMap.getRobot().getPosition().setY(robotY);
        fieldMap.getRobot().getRotation().setRadians(robotRot);
        return fieldMap;
    }
}
