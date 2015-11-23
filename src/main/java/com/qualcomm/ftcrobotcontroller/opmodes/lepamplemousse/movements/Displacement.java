package com.qualcomm.ftcrobotcontroller.opmodes.lepamplemousse.movements;

/**
 * The robot's positional movement will be determined here.
 * Created by joelv on 11/23/2015.
 */
public class Displacement {

    Displacement(){
    //Default Constructor
    }

    private final float maxPower = 1.0f;
    private final float mediumPower = 0.5f;
    private final float minPower = 0f;
    private float leftPower = 0f;
    private float rightPower = 0f;

    enum DIRECTION{
    RIGHT, LEFT;
    }

    public void forward(){
        //Forward Robot movement
    }

    public void reverse(){
        //Robot moves reverse
    }

    public void stop(){
        //Motors would equal 0
    }

    public void turn(){
        //Robot turns a certain direction
    }

    public void rotateRight(){
        //Robot rotates a certain direction
    }

}
