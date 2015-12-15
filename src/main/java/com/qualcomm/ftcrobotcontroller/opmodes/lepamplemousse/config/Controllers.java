package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.config;

import java.util.Map;

/**
 * todo actually make
 */
public class Controllers {

    private Map<String, Object> data = null;

    public boolean enabled(Integer gamepadNum, String button){
         return ((Map)((Map)(data.get("gamepad_"+gamepadNum.toString()))).get(button)).get("enabled").equals(true);
    }

    //************************************Controller Mapping Prototype Idea*************************

    interface Controls{
        Float driveLeft();
    }

    Map<String, Controls> controlMap = null;

    public void configureGamepad(){
        //Idea number 1: Map the function string to a function, then put function string under button mapping
        controlMap.put("drivetrain_left", new Controls(){
            public Float driveLeft(){
                return 0f;//just an example
            }
        });
    }

    //************************************Controller Mapping Prototype Idea*************************

}

