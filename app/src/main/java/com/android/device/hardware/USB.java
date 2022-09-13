package com.android.device.hardware;

import android.content.Intent;
import android.content.IntentFilter;

import com.android.device.UApplication;

public class USB {

    // 获取USB连接状态
    public static boolean getUsbStatus(){
        try{
            Intent intent = UApplication.getContext().registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
            return intent != null && intent.getBooleanExtra("connected", false);
        } catch (Throwable e) {
        }
        return false;
    }
}
