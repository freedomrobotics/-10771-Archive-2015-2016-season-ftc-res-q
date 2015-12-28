package org.fhs.robotics.ftcteam10771.lepamplemousse.movements;

/**
 * The robot's positional movement will be determined here.
 * Created by joelv on 11/23/2015.
 */
public class Displacement {

    private final float maxPower = 1.0f;
    private final float mediumPower = 0.5f;
    private final float minPower = 0f;
    private float leftPower = 0f;
    private float rightPower = 0f;
    Displacement() {
        //Default Constructor
    }

    public void forward() {
        //Forward Robot movement
    }

    public void reverse() {
        //Robot moves reverse
    }

    public void stop() {
        //Motors would equal 0
    }

    public void turn() {
        //Robot turns a certain direction
    }

    public void rotateRight() {
        //Robot rotates a certain direction
    }

    enum DIRECTION {
        RIGHT, LEFT
    }

}
