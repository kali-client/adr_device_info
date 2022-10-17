package com.android.device.software;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONObject;

import java.io.File;

public final class Storage {
    public static JSONObject getMemInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sdSize", getSDCardSize());
            jsonObject.put("sysSize", getSystemSize());
            jsonObject.put("dataSize", getDataSize());
            jsonObject.put("avaSize", getAvailMemory());
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

    private static String getSDCardSize() {
        try {
            StringBuffer buffer = new StringBuffer();
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(sdcardDir.getPath());
                long blockSize = sf.getBlockSizeLong();
                long blockCount = sf.getBlockCountLong();
                buffer.append(blockSize).append("*");
                buffer.append(blockCount);
            }
            return buffer.toString();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    private static String getSystemSize() {
        try {
            StringBuffer buffer = new StringBuffer();
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File root = Environment.getRootDirectory();

                StatFs sf = new StatFs(root.getPath());
                long blockSize = sf.getBlockSizeLong();
                long blockCount = sf.getBlockCountLong();

                buffer.append(blockSize).append("*");
                buffer.append(blockCount);
            }
            return buffer.toString();
        } catch (Throwable ignored) {
        }
        return "";
    }


    private static String getDataSize() {
        try {
            StringBuffer buffer = new StringBuffer();

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File data = Environment.getDataDirectory();

                StatFs sf = new StatFs(data.getPath());
                long blockSize = sf.getBlockSizeLong();
                long blockCount = sf.getBlockCountLong();

                buffer.append(blockSize).append("*");
                buffer.append(blockCount);
            }
            return buffer.toString();
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String getAvailMemory() {
        try {
            StringBuffer buffer = new StringBuffer();
            ActivityManager am = (ActivityManager) UApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            if (am != null) {
                am.getMemoryInfo(mi);
                buffer.append(mi.availMem).append("*");
                buffer.append(mi.totalMem);
            }
            return buffer.toString();
        } catch (Throwable ignored) {
        }
        return "";
    }
}
