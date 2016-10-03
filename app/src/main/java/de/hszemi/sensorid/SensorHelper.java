package de.hszemi.sensorid;

import android.hardware.Sensor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zemanek on 22.09.16.
 */

public class SensorHelper {

    public static Set<String> activeSensors = new HashSet<String>(Arrays.asList("ACCELEROMETER","GAME_ROTATION_VECTOR",
            "GEOMAGNETIC_ROTATION_VECTOR","GRAVITY","GYROSCOPE",
            "GYROSCOPE_UNCALIBRATED","LINEAR_ACCELERATION","MAGNETIC_FIELD","PRESSURE",
            "ROTATION_VECTOR","Magnetometer Uncalibrated (custom)"));

public static String type2string(int type) {
    switch (type) {
        case Sensor.TYPE_ACCELEROMETER:
            return "ACCELEROMETER";
        case Sensor.TYPE_AMBIENT_TEMPERATURE:
            return "AMBIENT_TEMPERATURE";
        case Sensor.TYPE_GAME_ROTATION_VECTOR:
            return "GAME_ROTATION_VECTOR";
        case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
            return "GEOMAGNETIC_ROTATION_VECTOR";
        case Sensor.TYPE_GRAVITY:
            return "GRAVITY";
        case Sensor.TYPE_GYROSCOPE:
            return "GYROSCOPE";
        case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
            return "GYROSCOPE_UNCALIBRATED";
        case Sensor.TYPE_HEART_RATE:
            return "HEART_RATE";
        case Sensor.TYPE_LIGHT:
            return "LIGHT";
        case Sensor.TYPE_LINEAR_ACCELERATION:
            return "LINEAR_ACCELERATION";
        case Sensor.TYPE_MAGNETIC_FIELD:
            return "MAGNETIC_FIELD";
        case Sensor.TYPE_PRESSURE:
            return "PRESSURE";
        case Sensor.TYPE_PROXIMITY:
            return "PROXIMITY";
        case Sensor.TYPE_RELATIVE_HUMIDITY:
            return "RELATIVE_HUMIDITY";
        case Sensor.TYPE_ROTATION_VECTOR:
            return "ROTATION_VECTOR";
        case Sensor.TYPE_SIGNIFICANT_MOTION:
            return "SIGNIFICANT_MOTION";
        case Sensor.TYPE_STEP_COUNTER:
            return "STEP_COUNTER";
        case Sensor.TYPE_STEP_DETECTOR:
            return "STEP_DETECTOR";
        case Sensor.TYPE_ORIENTATION:
            return "ORIENTATION (DEPRECATED)";
        case Sensor.TYPE_TEMPERATURE:
            return "TEMPERATURE (DEPRECATED)";
        case 14:
            return "Magnetometer Uncalibrated (custom)";
        case 22:
            return "Tilt (custom)";
        case 25:
            return "Device Position Classifier (custom)";
    }
    return "unknown (" + type + ")";
}
}
