package com.android.device.software;

import android.content.pm.PackageManager;

import com.android.device.UApplication;
import com.android.device.utils.ULog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Library {

    public static String getLibraries() {
        try {
            StringBuffer buffer = new StringBuffer();
            final List<String> list = new ArrayList<String>();
            PackageManager pm = UApplication.getContext().getPackageManager();
            final String[] rawList = pm.getSystemSharedLibraryNames();
            for (int i = 0; i < rawList.length; i++) {
                list.add(rawList[i]);
            }
            // sort by name
            Collections.sort(list, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    if (o1 == o2) return 0;
                    if (o1 == null) return -1;
                    if (o2 == null) return 1;
                    return o1.compareTo(o2);
                }
            });

            final int count = (list != null) ? list.size() : 0;
            for (int p = 0; p < count; p++) {
                String lib = list.get(p);
                buffer.append("library:");
                buffer.append(lib).append("\n");
            }
            return buffer.toString();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static JSONObject getJavaProperties() {
        JSONObject jsonObject = new JSONObject();
        try {
            Properties prop = System.getProperties();
            Set<String> keySets = prop.stringPropertyNames();
            for (String key : keySets) {
                jsonObject.put(key, prop.getProperty(key));
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }
}
