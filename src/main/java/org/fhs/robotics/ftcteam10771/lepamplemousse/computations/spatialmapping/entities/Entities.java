package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.entities;

/**
 * Created by Adam Li on 1/13/2016.
 */
public interface Entities {

    Rotation rotation = new Rotation();
    Position position = new Position();
    Size size = new Size();
    Offset offset = new Offset();

    Rotation getRotation();
    Position getPosition();
    Size getSize();
    Offset getOffset();

    class Coordinate {
        float x = 0.0f;
        float y = 0.0f;

        void setX(float x){
            this.x = x;
        }
        void setY(float y){
            this.y = y;
        }

        float getX(){
            return x;
        }
        float getY(){
            return y;
        }
    }
    class Position extends Coordinate {
        void moveX(float x){
            this.x += x;
        }
        void moveY(float y){
            this.y += y;
        }
        void moveXY(float x, float y){
            this.x = x;
            this.y = y;
        }
        void move(float distance, float angRad){
            this.x = (float)Math.cos(angRad) * distance;
            this.x = (float)Math.sin(angRad) * distance;
        }
        void moveDeg(float distance, float angDeg){
            this.x = (float)Math.cos((angDeg / (float)Math.PI) * 180.0f) * distance;
            this.x = (float)Math.sin((angDeg / (float)Math.PI) * 180.0f) * distance;
        }
    }
    class Size extends Coordinate{
        void scaleX(float x){
            this.x *= x;
        }
        void scaleY(float y){
            this.y *= y;
        }
        void scale(float scalar){
            this.x *= scalar;
            this.y *= scalar;
        }
    }
    class Offset extends Coordinate{
        // nothing yet!
    }
    class Rotation{
        float rot = 0.0f;

        void setDegrees(float rotDegrees){
            rot = (rotDegrees / 180.0f) * (float)Math.PI;
        }
        void setRadians(float rotRadians){
            rot = rotRadians;
        }

        float getDegrees(){
            return (rot / (float)Math.PI) * 180.0f;
        }
        float getRadians(){
            return rot;
        }
    }
}
