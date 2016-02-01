package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps;

import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.entities.Entities;
import org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.entities.Robots;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam Li on 1/13/2016.
 */
public class Maps {

    MapType mapType = MapType.SQUARE;

    Coordinate mapSize = new Coordinate();

    List<Obstacle> obstacles = new ArrayList<Obstacle>();

    List<Entities> entities = new ArrayList<Entities>();

    Robots robot;

    public Maps(float sizeX, float sizeY, List<Obstacle> obstacles, List<Entities> entities, Robots robot){
        mapSize.setX(sizeX);
        mapSize.setY(sizeY);
        this.obstacles.addAll(obstacles);
        this.entities.addAll(entities);
        this.robot = robot;
    }

    public Coordinate getMapSize(){
        return mapSize;
    }

    public MapType getMapType(){
        return mapType;
    }

    public List<Obstacle> getObstacles(){
        return obstacles;
    }

    public List<Entities> getEntities(){
        return entities;
    }

    public Robots getRobot(){
        return robot;
    }

    enum MapType{
        SQUARE,     //The only one that matters for now.
        CIRCLE,
        TRIANGLE,
        PENTAGON
    }
    public class Coordinate {
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
    class Obstacle {
        ArrayList<Coordinate> points;

        boolean portal;

        Maps portalMap;

        Rotation rotation = new Rotation();

        public Obstacle(ArrayList<Coordinate> points, float rotRad, boolean portal, Maps portalMap){
            rotation.setRadians(rotRad);
            this.points = points;
            this.portal = portal;
            this.portalMap = portalMap;
        }

        public Obstacle(ArrayList<Coordinate> points, boolean portal, Maps portalMap){
            this(points, 0.0f, portal, portalMap);
        }

        public Obstacle(ArrayList<Coordinate> points, float rotRad){
            this(points, rotRad, false, null);
        }

        public Obstacle(ArrayList<Coordinate> points){
            this(points, 0.0f, false, null);
        }

        public class Rotation{
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
}
