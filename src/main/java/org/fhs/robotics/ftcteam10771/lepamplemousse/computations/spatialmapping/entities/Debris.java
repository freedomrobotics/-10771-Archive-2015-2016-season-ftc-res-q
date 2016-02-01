package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.entities;

/**
 * Created by Adam Li on 1/13/2016.
 */
public class Debris implements Entities {

    // TODO: 1/13/2016 Measure and fill in
    private static final float cubeXY = 50f;
    private static final float ballXY = 71.12f;
    private boolean isCube;

    public Debris(float posX, float posY, float rotRad, boolean isCube){
        rotation.setRadians(rotRad);
        position.setX(posX);
        position.setY(posY);
        setCube(isCube);
    }

    public Debris(float posX, float posY, boolean isCube){
        this(posX, posY, 0.0f, isCube);
    }

    public void setCube(boolean isCube) {
        float sizeXY = cubeXY;
        if (!isCube)
            sizeXY = ballXY;
        size.setX(sizeXY);
        size.setY(sizeXY);
        offset.setX(sizeXY/2);
        offset.setY(sizeXY/2);
        this.isCube = isCube;
    }

    public void setCube() {
        setCube(true);
    }

    public void setBall(boolean isBall) {
        setCube(!isBall);
    }

    public void setBall() {
        setCube(false);
    }

    public boolean getCube(){
        return isCube;
    }

    public boolean getBall(){
        return !isCube;
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public Offset getOffset() {
        return offset;
    }
}
