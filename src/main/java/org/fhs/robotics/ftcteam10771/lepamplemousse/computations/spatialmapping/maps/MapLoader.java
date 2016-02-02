package org.fhs.robotics.ftcteam10771.lepamplemousse.computations.spatialmapping.maps;

import android.os.Environment;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Dynamic;
import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Static;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Adam Li on 2/1/2016.
 */
public class MapLoader extends Maps {

    private File mapsDirectory = new File(Environment.getExternalStorageDirectory().toString() + Static.mapsPath);
    private boolean writable = false;

    private static boolean mapsDirExists = false;

    private File mapFile;


    public MapLoader(String mapName){
        String fileName = mapName + Static.mapsFileSuffix;
        writable = Environment.getExternalStorageDirectory().canWrite();
        if (!mapsDirExists)
        dirCheck();
        mapFile = new File(mapsDirectory, fileName);
        InputStream map;
        try {
            map = new FileInputStream(mapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeStoredMap(fileName);
        }
        if (mapsDirExists && Static.mapNames.contains(mapName)){
            // TODO: 2/1/2016 CONTINUE LATER
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

}
