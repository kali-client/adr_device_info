package com.android.device.software;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.device.UApplication;
import com.android.device.utils.ULog;

import org.json.JSONObject;

public final class Screen {

    //获得当前屏幕亮度的模式
    private static int getScreenMode() {
        int screenMode = 0;
        try {
            /**
             *  SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
             *  SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
             */
            screenMode = Settings.System.getInt(UApplication.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Throwable e) {
            ULog.e(e);
        }
        return screenMode;
    }

    //获得当前屏幕亮度值  0--255
    private static int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(UApplication.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Throwable e) {
            ULog.e(e);
        }
        return screenBrightness;
    }

    //屏幕宽、高、密度
    public static String getScreenWHD() {
        try {
            WindowManager wm = (WindowManager) UApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.widthPixels + "x" + outMetrics.heightPixels + "x" + outMetrics.densityDpi;
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static JSONObject getScreenInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", getScreenMode());
            jsonObject.put("brightness", getScreenBrightness());
            jsonObject.put("whd", getScreenWHD());
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

}


