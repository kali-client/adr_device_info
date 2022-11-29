package com.android.device.hardware;

import static android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY;
import static android.os.BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER;
import static android.os.BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE;
import static android.os.BatteryManager.BATTERY_PROPERTY_CURRENT_NOW;
import static android.os.BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER;
import static android.os.BatteryManager.BATTERY_PROPERTY_STATUS;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;

import org.json.JSONObject;

public class Battery {
    // 电量信息
    public static JSONObject getBatteryInfo(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null;
        }
        BatteryManager manager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        JSONObject jsonObject = new JSONObject();
        if (manager != null) {
            try {
                jsonObject.put("charge_counter", manager.getLongProperty(BATTERY_PROPERTY_CHARGE_COUNTER));// 1
                jsonObject.put("current_now", manager.getLongProperty(BATTERY_PROPERTY_CURRENT_NOW));// 2
                jsonObject.put("current_average", manager.getLongProperty(BATTERY_PROPERTY_CURRENT_AVERAGE));// 3
                jsonObject.put("battery_capacity", manager.getLongProperty(BATTERY_PROPERTY_CAPACITY));// 4
                jsonObject.put("energy_counter", manager.getLongProperty(BATTERY_PROPERTY_ENERGY_COUNTER));// 5
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//api >= 26
                    /**
                     *    // values for "status" field in the ACTION_BATTERY_CHANGED Intent
                     *     public static final int BATTERY_STATUS_UNKNOWN = Constants.BATTERY_STATUS_UNKNOWN; 1
                     *     public static final int BATTERY_STATUS_CHARGING = Constants.BATTERY_STATUS_CHARGING; 2
                     *     public static final int BATTERY_STATUS_DISCHARGING = Constants.BATTERY_STATUS_DISCHARGING; 3
                     *     public static final int BATTERY_STATUS_NOT_CHARGING = Constants.BATTERY_STATUS_NOT_CHARGING; 4
                     *     public static final int BATTERY_STATUS_FULL = Constants.BATTERY_STATUS_FULL; 5
                     */

                    jsonObject.put("battery_status", manager.getLongProperty(BATTERY_PROPERTY_STATUS));// 6
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    jsonObject.put("isCharging", manager.isCharging());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
