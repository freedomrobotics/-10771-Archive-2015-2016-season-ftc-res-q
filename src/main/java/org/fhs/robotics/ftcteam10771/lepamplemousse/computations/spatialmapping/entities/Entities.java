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

        public void setX(float x){
            this.x = x;
        }
        public void setY(float y){
            this.y = y;
        }

        public float getX(){
            return x;
        }
        public float getY(){
            return y;
        }
    }
    class Position extends Coordinate {
        public void moveX(float x){
            this.x += x;
        }
        public void moveY(float y){
            this.y += y;
        }
        public void moveXY(float x, float y){
            this.x = x;
            this.y = y;
        }
        public void move(float distance, float angRad){
            this.x = (float)Math.cos(angRad) * distance;
            this.x = (float)Math.sin(angRad) * distance;
        }
        public void moveDeg(float distance, float angDeg){
            this.x = (float)Math.cos((angDeg / (float)Math.PI) * 180.0f) * distance;
            this.x = (float)Math.sin((angDeg / (float)Math.PI) * 180.0f) * distance;
        }
    }
    class Size extends Coordinate{
        public void scaleX(float x){
            this.x *= x;
        }
        public void scaleY(float y){
            this.y *= y;
        }
        public void scale(float scalar){
            this.x *= scalar;
            this.y *= scalar;
        }
    }
    class Offset extends Coordinate{
        // nothing yet!
    }
    class Rotation{
        public float rot = 0.0f;

        public void setDegrees(float rotDegrees){
            rot = (rotDegrees / 180.0f) * (float)Math.PI;
        }
        public void setRadians(float rotRadians){
            rot = rotRadians;
        }

        public float getDegrees(){
            return (rot / (float)Math.PI) * 180.0f;
        }
        public float getRadians(){
            return rot;
        }
    }
}
