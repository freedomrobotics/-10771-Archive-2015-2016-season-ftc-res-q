package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.entities;

/**
 * Created by Adam Li on 1/13/2016.
 */
public class Debris implements Entities {

    // TODO: 1/13/2016 Measure and fill in
    private static final float sizeXY = 0.0f;

    public Debris(float posX, float posY, float rotRad){
        rotation.setRadians(rotRad);
        position.setX(posX);
        position.setY(posY);
        size.setX(sizeXY);
        size.setY(sizeXY);
        offset.setX(sizeXY/2);
        offset.setY(sizeXY/2);
    }

    public Debris(float posX, float posY){
        this(posX, posY, 0.0f);
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
