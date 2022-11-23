package com.android.device.hardware;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Hardware {

    public static String getCpuName() {
        String valueStr;
        FileReader fr;
        String cpuName = null;
        BufferedReader bufferedReader = null;

        try {
            fr = new FileReader("/proc/cpuinfo");
            bufferedReader = new BufferedReader(fr);
            while ((valueStr = bufferedReader.readLine()) != null) {
                if (valueStr.contains("Hardware")) {
                    cpuName = valueStr.split(":")[1];
                    break;
                }
            }
        } catch (Throwable ignored) {
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {

                }
            }
        }
        return cpuName != null ? cpuName : "";
    }

    public static String getCpuInfo() { //IO操作
        try {
            StringBuilder cpuInfo = new StringBuilder();
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader reader = new BufferedReader(fr);
            String str = null;
            while ((str = reader.readLine()) != null) {
                cpuInfo.append(str);
                cpuInfo.append("\n");
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    ULog.e(e);
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                    ULog.e(e);
                }
            }
            return cpuInfo.toString();
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getFeatures() {
        try {
            StringBuffer buffer = new StringBuffer();
            if (Build.VERSION.SDK_INT >= 24) {
                PackageManager pm = UApplication.getContext().getPackageManager();
                List<FeatureInfo> list = Arrays.asList(pm.getSystemAvailableFeatures());
                // sort by name
                Collections.sort(list, new Comparator<FeatureInfo>() {
                    public int compare(FeatureInfo o1, FeatureInfo o2) {
                        if (o1.name == o2.name) return 0;
                        if (o1.name == null) return -1;
                        if (o2.name == null) return 1;
                        return o1.name.compareTo(o2.name);
                    }
                });

                final int count = (list != null) ? list.size() : 0;
                for (int p = 0; p < count; p++) {
                    FeatureInfo fi = list.get(p);
                    buffer.append("feature:");
                    if (fi.name != null) {
                        buffer.append(fi.name);
                        if (fi.version > 0) {
                            buffer.append("=");
                            buffer.append(fi.version);
                        }
                        buffer.append("\n");
                    } else {
                        buffer.append("reqGlEsVersion=0x"
                                + Integer.toHexString(fi.reqGlEsVersion));
                    }
                }
            }
            return buffer.toString();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static boolean isSupportNFC() {
        NfcManager nfcManager = (NfcManager) UApplication.getContext().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = nfcManager.getDefaultAdapter();
        return adapter != null;
    }

    public static JSONObject getHardwareInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cpuName", getCpuName());
            jsonObject.put("cpuInfo", getCpuInfo());
            jsonObject.put("features", getFeatures());
            jsonObject.put("supportNFC", isSupportNFC());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
