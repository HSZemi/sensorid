package de.hszemi.sensorid;

import android.hardware.Sensor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides auxiliary data and methods for managing sensor data and display.
 */

public class SensorHelper {

    // The list of sensor types we selected for further inspection
    public static Set<String> activeSensors = new HashSet<String>(Arrays.asList("TYPE_ACCELEROMETER","TYPE_GAME_ROTATION_VECTOR",
            "TYPE_GEOMAGNETIC_ROTATION_VECTOR","TYPE_GRAVITY","TYPE_GYROSCOPE",
            "TYPE_GYROSCOPE_UNCALIBRATED","TYPE_LINEAR_ACCELERATION","TYPE_MAGNETIC_FIELD","TYPE_PRESSURE",
            "TYPE_ROTATION_VECTOR","Magnetometer Uncalibrated (custom)"));

public static String type2string(int type) {
    switch (type) {
        case Sensor.TYPE_ACCELEROMETER:
            return "TYPE_ACCELEROMETER";
        case Sensor.TYPE_AMBIENT_TEMPERATURE:
            return "TYPE_AMBIENT_TEMPERATURE";
        case Sensor.TYPE_GAME_ROTATION_VECTOR:
            return "TYPE_GAME_ROTATION_VECTOR";
        case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
            return "TYPE_GEOMAGNETIC_ROTATION_VECTOR";
        case Sensor.TYPE_GRAVITY:
            return "TYPE_GRAVITY";
        case Sensor.TYPE_GYROSCOPE:
            return "TYPE_GYROSCOPE";
        case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
            return "TYPE_GYROSCOPE_UNCALIBRATED";
        case Sensor.TYPE_HEART_RATE:
            return "TYPE_HEART_RATE";
        case Sensor.TYPE_LIGHT:
            return "TYPE_LIGHT";
        case Sensor.TYPE_LINEAR_ACCELERATION:
            return "TYPE_LINEAR_ACCELERATION";
        case Sensor.TYPE_MAGNETIC_FIELD:
            return "TYPE_MAGNETIC_FIELD";
        case Sensor.TYPE_PRESSURE:
            return "TYPE_PRESSURE";
        case Sensor.TYPE_PROXIMITY:
            return "TYPE_PROXIMITY";
        case Sensor.TYPE_RELATIVE_HUMIDITY:
            return "TYPE_RELATIVE_HUMIDITY";
        case Sensor.TYPE_ROTATION_VECTOR:
            return "TYPE_ROTATION_VECTOR";
        case Sensor.TYPE_SIGNIFICANT_MOTION:
            return "TYPE_SIGNIFICANT_MOTION";
        case Sensor.TYPE_STEP_COUNTER:
            return "TYPE_STEP_COUNTER";
        case Sensor.TYPE_STEP_DETECTOR:
            return "TYPE_STEP_DETECTOR";
        case Sensor.TYPE_ORIENTATION:
            return "TYPE_ORIENTATION (DEPRECATED)";
        case Sensor.TYPE_TEMPERATURE:
            return "TYPE_TEMPERATURE (DEPRECATED)";
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
