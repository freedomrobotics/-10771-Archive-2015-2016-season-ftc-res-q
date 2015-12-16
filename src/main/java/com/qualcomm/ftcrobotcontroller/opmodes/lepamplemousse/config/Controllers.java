package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.components.Aliases;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Map;

/**
 * todo actually make
 */
public class Controllers {

    private Map<String, Object> data = null;

    private Map<Map, Float> analogMap = null;

    private Map<Map, Boolean> buttonMap = null;

    public boolean gamepadEnabled(Integer gamepadNum, String button){
        if (((Map)((Map)(data.get("gamepad_"+gamepadNum.toString()))).get(button)).get("enabled").equals(true))
            return true;
        return false;
    }

    //************************************Controller Mapping Prototype Idea*************************
    //TODO: 12/16/2015 Adam can you look
    interface Controls{
        public void function(float analog);
        public void function(boolean button);
    }

    Map<String, Controls> controlMap = null;

    public void configureFunctions(){
        //Idea number 1: Map the function string to a function, then put function string under button mapping
        controlMap.put("drivetrain_left", new Controls(){
            @Override
            public void function(float AnalogStickLeft){
                Aliases.motorMap.get("left_drive").setPower(AnalogStickLeft);
            }
            @Override
            public void function(boolean NullButton){}
        });
        controlMap.put("drivetrain_right", new Controls(){
            @Override
            public void function(float AnalogStickRight){
                Aliases.motorMap.get("right_drive").setPower(AnalogStickRight);
            }
            @Override
            public void function(boolean NullButton){}
        });
        //todo Adam, I did not type the rest of the functions because your approval is pending
    }

    public Object gamepad(String gamepad){
        return data.get(gamepad);
    }

    public Map retrieveGamepad(String gamepad){
        return ((Map)data.get(gamepad));
    }

    public Map retrieveButton(String gamepad, String button){
        return ((Map)retrieveGamepad(gamepad).get(button));
    }

    public void mapStick(String gamepad, String stickID, float analogStick){
        analogMap.put(retrieveButton(gamepad, stickID), analogStick);
    }

    public void mapButton(String gamepad, String buttonID, boolean button){
        buttonMap.put(retrieveButton(gamepad, buttonID), button);
    }

    public String getFunction(String gamepad, String button){
        return (retrieveButton(gamepad, button)).get("function").toString();
    }

    //TODO: 12/16/2015 I think I did it(probably not). Adam can you comment on the drawbacks/advantages of this?
    public void executeFunction(String gamepad, String partID){
        controlMap.get(getFunction(gamepad, partID)).function(buttonMap.get(retrieveButton(gamepad, partID)));
    }
    //************************************Controller Mapping Prototype Idea*************************

}

