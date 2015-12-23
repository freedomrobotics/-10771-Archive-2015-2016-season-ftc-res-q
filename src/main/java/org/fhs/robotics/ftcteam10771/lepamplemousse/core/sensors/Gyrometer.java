package org.fhs.robotics.ftcteam10771.lepamplemousse.core.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.hardware.GyroSensor;

import org.fhs.robotics.ftcteam10771.lepamplemousse.core.vars.Static;

/**
 * Allows for polling of the phone's built-in gyrometer.
 * todo allow for uncalibrated version without noise and drift compensation
 */
//maybe  and provides a listener to "interrupt" using
public class Gyrometer extends GyroSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroSensor;

    private float[] changeRotation = new float[4];
    private float[] changeRotationMatrix = new float[9];
    private float[] currentRotation = new float[3];
    private float[] currentRotationMatrix = new float[9];

    private static final float marginOfError = 0.5f;

    private float timestamp;

    /**
     * Constructs a gyroscope sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor uses the normal delay.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Gyrometer(Context context){
        this(context, false, false);
    }

    /**
     * Constructs a gyroscope sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor allows for less delay.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Gyrometer(Context context, boolean lessDelay){
        this(context, lessDelay, false);
    }

    /**
     * Constructs a gyroscope sensor given the app Context based off of the FTC gyroscope sensor interfaces. Trust me, for the custom sensor classes, you want to manually call close when you're done.
     * This constructor allows for the fastest delay. I'm also getting lazy with my implementation FYI.
     * @param context   The context of the app. In the FTC SDK, hardwareMap.appContext provides the context.
     */
    public Gyrometer(Context context, boolean lessDelay, boolean fastest){
        //Pulled from the Android Motion Sensor API guide page.
        //Get the sensors!
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //Call to the calibrated Gyroscope since I'm lazy and don't feel like doing the math.
        //It should be able to provide the "raw" values that the SDK refers to
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        if (lessDelay){
            if (fastest){
                sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
                return;
            }
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
            return;
        }
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        //Check if we received a gyroscope event or not
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // This timestep's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float changeTime = (event.timestamp - timestamp) * Static.nanoSecondsToSeconds;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                float changeRotSpeed = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // So we don't get tons of miniature readings
                if (changeRotSpeed > marginOfError) {
                    axisX /= changeRotSpeed;
                    axisY /= changeRotSpeed;
                    axisZ /= changeRotSpeed;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                // ^^^^Whatever this said...^^^
                double thetaOverTwo = changeRotSpeed * changeTime / 2.0f;
                double sinThetaOverTwo = Math.sin(thetaOverTwo);
                double cosThetaOverTwo = Math.cos(thetaOverTwo);
                changeRotation[0] = (float) (sinThetaOverTwo * axisX);
                changeRotation[1] = (float) (sinThetaOverTwo * axisY);
                changeRotation[2] = (float) (sinThetaOverTwo * axisZ);
                changeRotation[3] = (float) cosThetaOverTwo;

                //First do whatever this does
                SensorManager.getRotationMatrixFromVector(changeRotationMatrix, changeRotation);

                //then this
                currentRotationMatrix = matrixMultiplication(currentRotationMatrix, changeRotationMatrix);

                //then end up with the current rotation!
                SensorManager.getOrientation(currentRotationMatrix, currentRotation);

                /*
                Issues I see are that I have no idea if this works or how well it's going to work,
                and that the orientation might set it to the world rather than relative to the
                start position of the robot.
                 */
            }
            timestamp = event.timestamp;
        }
    }

    //To concatenate the matrices and add the change in rotation to the rotation position to get the current rotation.
    private float[] matrixMultiplication(float[] a, float[] b)
    {
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }

    /**
     * Called when the accuracy of a sensor has changed.
     * <p>See {@link SensorManager SensorManager}
     * for details. Not functional for this blah stuff made by Adam.
     *
     * @param sensor    Something....
     * @param accuracy The new accuracy of this sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Blah, blah, I'm dumb. and I don't care.
    }

    @Override
    public void calibrate() {
        // TODO: 12/23/2015
    }

    /**
     * THIS DOESN'T MATTER TO ME!
     * @return NOTHING USEFUL
     */
    @Override
    public boolean isCalibrating() {
        return false;
    }

    @Override
    public int getHeading() {
        return ((Float)currentRotation[0]).intValue();
    }

    /**
     * Returns the Z axis rotation
     * @return Z axis rotation
     */
    @Override
    public double getRotation() {
        return currentRotation[0];
    }

    @Override
    public int rawX() {
        return ((Float)currentRotation[1]).intValue();
    }

    @Override
    public int rawY() {
        return ((Float)currentRotation[2]).intValue();
    }

    @Override
    public int rawZ() {
        return ((Float)currentRotation[0]).intValue();
    }

    @Override
    public void resetZAxisIntegrator() {
        // TODO: 12/23/2015
    }

    @Override
    public String status() {
        return "Built-in gyroscope sensor implemented by Adam Li. Who knows what I even did here?";
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
        sensorManager.unregisterListener(this, gyroSensor);
    }
}
