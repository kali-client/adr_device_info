package com.android.device.hardware;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONException;
import org.json.JSONObject;

public class USB {

    // 获取USB连接状态
    public static boolean getUsbStatus() {
        try {
            Intent intent = UApplication.getContext().registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
            return intent != null && intent.getBooleanExtra("connected", false);
        } catch (Throwable e) {
        }
        return false;
    }

    public static boolean adbEnable() {
        try {
            return Settings.Secure.getInt(UApplication.getContext().getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0;
        } catch (Exception e) {
            ULog.e(e);
        }
        return false;
    }

    public static JSONObject getUsbInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("usb", getUsbStatus());
            jsonObject.put("adbEnable", adbEnable());
        } catch (JSONException e) {
            ULog.e(e);
        }
        return jsonObject;
    }
}
