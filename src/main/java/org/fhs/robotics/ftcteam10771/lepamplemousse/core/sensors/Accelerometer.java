package org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.hardware.AccelerationSensor;

/**
 * Allows for polling and provides a listener to "interrupt" using the phone's built-in accelerometer.
 */
// TODO: 12/23/2015 add a way to construct with a change in orientation. Same with gyroscope.
// FIXME: 12/28/2015 THIS DOES NOT WORK PROPERLY
public class Accelerometer extends AccelerationSensor implements SensorEventListener{

    SensorManager sensorManager;
    Sensor accelerometer;

    Acceleration acceleration = new Acceleration();

    /**
     * Constructs a accelerometer sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor uses the normal delay.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Accelerometer(Context context){
        this(context, false, false);
    }

    /**
     * Constructs a accelerometer sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor allows for less delay.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Accelerometer(Context context, boolean lessDelay){
        this(context, lessDelay, false);
    }

    /**
     * Constructs a accelerometer sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor allows for the fastest delay. I'm also getting lazy with my implementation FYI.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Accelerometer(Context context, boolean lessDelay, boolean fastest){
        //Pulled from the Android Motion Sensor API guide page.
        //Get the sensors!
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //Call to the calibrated Gyroscope since I'm lazy and don't feel like doing the math.
        //It should be able to provide the "raw" values that the SDK refers to
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (lessDelay){
            if (fastest){
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                return;
            }
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            return;
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public Acceleration getAcceleration() {
        return acceleration;
    }


    @Override
    public String status() {
        return "Built-in acceleration sensor implemented by Adam Li. This one was easy";
    }

    @Override
    public String getDeviceName() {
        return "Built-in Gyro";
    }

    @Override
    public String getConnectionInfo() {
        return "null";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    /**
     * I don't know what FTC has this for; but, I have an idea. For mah sensors, you have to call this otherwise you will have problems!
     */
    @Override
    public void close() {
        sensorManager.unregisterListener(this, accelerometer);
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //Check if we received an linearAccelerometer event or not
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            acceleration.x = event.values[0];
            acceleration.y = event.values[1];
            acceleration.z = event.values[2];
        }
    }

    /**
     * Called when the accuracy of a sensor has changed.
     * <p>See {@link SensorManager SensorManager}
     * for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //LAFKLASLKFHAJLFHSADLJKFhsdaF
    }
}
