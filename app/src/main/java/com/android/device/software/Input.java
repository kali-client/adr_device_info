package com.android.device.software;

import android.content.Context;
import android.provider.Settings;

import com.android.utils.ULog;

import org.json.JSONObject;

public class Input {
    /**
     * 用于检测自动化模拟点击
     */
    public static String getDefaultInputMethod(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "default_input_method");
        return string == null ? "" : string;
    }


    public static JSONObject getInputInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("defaultInputMethod", getDefaultInputMethod(context));
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

}
