package com.android.device.ext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class XhsInfo {


    public static String getCellIp(Context context) {
        NetworkInfo activeNetworkInfo;
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE") != 0) {
            return "<absent>";
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 0) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress nextElement = inetAddresses.nextElement();
                        if (!nextElement.isLoopbackAddress() && !nextElement.isLinkLocalAddress()) {
                            return nextElement.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e12) {
                e12.printStackTrace();
            }
        }
        return "null";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String getImeiBySlot(Context context, int i8) {
        if (true) {
            int i12 = Build.VERSION.SDK_INT;
            if (i12 > 28) {
                return null;
            }
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return "<absent>";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return "null";
            }
            return i12 >= 26 ? telephonyManager.getImei(i8) : telephonyManager.getDeviceId(i8);
        }
        return "<absent>";
    }

    public static String getNetworkCountryIso(Context context) {
        TelephonyManager telephonyManager;
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0 || (telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)) == null) ? "<absent>" : telephonyManager.getNetworkCountryIso();
    }

    public static String getOperatorCode(Context context) {
        TelephonyManager telephonyManager;
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0 || (telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)) == null) ? "<absent>" : telephonyManager.getNetworkOperator();
    }

    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager;
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0 || (telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)) == null) ? "<absent>" : telephonyManager.getNetworkOperatorName();
    }

    public static boolean isExternalStorageEnable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isSupportCameraFlash(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public static int simState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return 0;
        }
        return telephonyManager.getSimState();
    }


    public static JSONObject getXInfo(Context context) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cellIp", getCellIp(context));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                jsonObject.put("imeiBySlot", getImeiBySlot(context, 0));
            }
            jsonObject.put("networkCountryIso", getNetworkCountryIso(context));
            jsonObject.put("operatorCode", getOperatorCode(context));
            jsonObject.put("operatorName", getOperatorName(context));
            jsonObject.put("externalStorageEnable", isExternalStorageEnable());
            jsonObject.put("supportCameraFlash", isSupportCameraFlash(context));
            jsonObject.put("simState", simState(context));
            jsonObject.put("jsfingerPrint", "jsfingerPrint");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
