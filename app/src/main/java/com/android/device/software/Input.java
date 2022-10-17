package com.android.device.software;

import android.content.Context;
import android.provider.Settings;

public class Input {
    /**
     * 用于检测自动化模拟点击
     */
    public static String getDefaultInputMethod(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "default_input_method");
        return string == null ? "" : string;
    }

}
