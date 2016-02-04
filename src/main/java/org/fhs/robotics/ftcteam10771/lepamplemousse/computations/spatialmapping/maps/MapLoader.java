package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps;

import android.os.Environment;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Dynamic;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Static;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam Li on 2/1/2016.
 */
public class MapLoader extends Maps {

    private File mapsDirectory = new File(Environment.getExternalStorageDirectory().toString() + Static.mapsPath);
    private boolean writable = false;

    private static boolean mapsDirExists = false;

    private File mapFile;

    boolean mapLoaded = false;


    public MapLoader(String mapName){
        String fileName = mapName + Static.mapsFileSuffix;
        writable = Environment.getExternalStorageDirectory().canWrite();
        if (!mapsDirExists)
        dirCheck();
        mapFile = new File(mapsDirectory, fileName);
        InputStream map = null;
        try {
            map = new FileInputStream(mapFile);
            mapLoaded = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (mapsDirExists && Static.mapNames.contains(mapName)){
            writeStoredMap(mapName);
        }
        if (!mapLoaded && Static.mapNames.contains(mapName)){
            try {
                map = Dynamic.globalAssets.open(fileName);
                mapLoaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!mapLoaded){
            initialize();
        }
        if (mapLoaded){
            mapFileParser(map);
        }
    }

    //Just to save lines
    private void dirCheck() {
        mapsDirExists = true;
        // Shortened logic to check and create the folder. If it can write and the folder
        // doesn't exist, it will try to create it, marking it as not existing if it fails.
        // If it doesn't exist and write can't happen, it's marked as not existing.
        if (!mapsDirectory.exists() && writable) {
            if (!mapsDirectory.mkdirs()) {
                mapsDirExists = false;
            }
        } else if (!mapsDirectory.exists()) {
            mapsDirExists = false;
        }
    }

    private boolean writeStoredMap(String fileName) {
        try {
            File mapFile = new File(mapsDirectory, fileName);
            if (!mapFile.isFile() && !mapFile.createNewFile()) {
                return false;
            }
            InputStream in = Dynamic.globalAssets.open(fileName);
            OutputStream out = new FileOutputStream(mapFile, false);

            byte[] buffer = new byte[1024];
            int readlen;
            while ((readlen = in.read(buffer)) != -1) {
                out.write(buffer, 0, readlen);
            }
            in.close();
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void mapFileParser(InputStream map){
        Yaml yaml = new Yaml();
        Map<String, Object> data = (Map<String, Object>) yaml.load(map);

        Map<String, Object> values = null;
        Map<String, Object> obstacles = null;

        float sizeX, sizeY, rot;
        sizeX = sizeY = rot = 0;

        List<Obstacle> obs = new ArrayList<Obstacle>();

        mapLoaded = false;

        if (data.containsKey("values")) {
            Pattern p = Pattern.compile("[\\{\\}]");
            Matcher m = p.matcher(data.get("values").toString());
            if (m.find()) {
                values = (Map<String, Object>) data.get("values");
            }
            else{
                return;
            }
        }
        if (data.containsKey("obstacles")) {
            Pattern p = Pattern.compile("[\\{\\}]");
            Matcher m = p.matcher(data.get("obstacles").toString());
            if (m.find()) {
                obstacles = (Map<String, Object>) data.get("obstacles");
            }
        }
        if (values != null){
            if (values.containsKey("size_x"))
                sizeX = (Float) values.get("size_x");
            if (values.containsKey("size_y"))
                sizeY = (Float) values.get("size_y");
            if (values.containsKey("rotation"))
                rot = (Float) values.get("rotation");
            if (sizeX == 0 || sizeY == 0){
                return;
            }
        }
        if (obstacles != null) {
            for (Object o : obstacles.entrySet()) {
                Map.Entry ob = (Map.Entry) o;
                Pattern p = Pattern.compile("[\\{\\}]");
                Matcher m = p.matcher(ob.getValue().toString());
                if (m.find()) {
                    Map<String, Object> stuffs = (Map<String, Object>) ob.getValue();
                    List<List<Double>> ps = (List<List<Double>>) stuffs.get("points");
                    ArrayList<Coordinate> points = new ArrayList<Coordinate>();
                    for (int c = 0; c < ps.size(); c++){
                        Coordinate point = new Coordinate();
                        point.setX(ps.get(c).get(0).floatValue());
                        point.setY(ps.get(c).get(1).floatValue());
                        points.add(point);
                    }
                    float rotob = ((Double)stuffs.get("rotation")).floatValue();
                    boolean portal = stuffs.get("portal").toString().equals("true");
                    obs.add(new Obstacle(points, rotob, portal, stuffs.get("linked_map").toString()));
                }
            }
        }
        initialize(sizeX, sizeY, rot, obs);
        mapLoaded = true;
    }

}
