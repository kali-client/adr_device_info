package com.android.device.hardware;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;

import com.android.UApplication;

import java.util.HashMap;
import java.util.Map;

public class Battery {
    // 电量信息
    public static Map<String, String> getBatteryInfo(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            return null;
        }
        BatteryManager manager = (BatteryManager) UApplication.getContext().getSystemService(Context.BATTERY_SERVICE);
        Map<String, String> batteryMap = null;
        if (manager != null){
            batteryMap = new HashMap<>();
            try{
                batteryMap.put("battery_capacity", manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) + "");// 电量百分比
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    batteryMap.put("battery_status", manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS) + "");// 充电状态
                }
            } catch (Throwable e){
            }
        }
        return batteryMap;
    }
}
