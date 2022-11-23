package com.android.device.hardware;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.SensorManager;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONObject;

import java.util.List;

public class Sensor {
    public static JSONObject getSensorInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            SensorManager sm = (SensorManager) UApplication.getContext().getSystemService(SENSOR_SERVICE);
            List<android.hardware.Sensor> allSensors = sm.getSensorList(android.hardware.Sensor.TYPE_ALL);
            for (android.hardware.Sensor sensor : allSensors) {
                jsonObject.put(sensor.getName(), sensor.toString());
            }
        } catch (Exception e) {
            ULog.e(e);
        }
        return jsonObject;
    }
}
