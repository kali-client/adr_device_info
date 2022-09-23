package com.android.device.ids;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebSettings;

import androidx.core.app.ActivityCompat;

import com.android.device.UApplication;
import com.android.device.utils.ULog;

import java.util.Locale;

public class IDs {

    public static String getImei() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                return telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }


    public static String getDeviceIds() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                StringBuffer buffer = new StringBuffer();
                TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Service.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (!TextUtils.isEmpty(tm.getDeviceSoftwareVersion())) {
                        buffer.append(tm.getDeviceSoftwareVersion());
                    }
                    if (Build.VERSION.SDK_INT >= 23) {
                        int count = tm.getPhoneCount();
                        for (int i = 0; i < count; i++) {
                            if (buffer.length() > 0) {
                                buffer.append("*");
                            }
                            buffer.append(tm.getDeviceId(i));
                        }
                    }
                }
                return buffer.toString();
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    /**
     * MEID for GSM phones
     */
    public static String getImeis() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                StringBuffer buffer = new StringBuffer();
                TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Service.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        int count = tm.getPhoneCount();
                        for (int i = 0; i < count; i++) {
                            if (i != 0) {
                                buffer.append("*");
                            }
                            String imei = tm.getImei(i);
                            if (!TextUtils.isEmpty(imei)) {
                                buffer.append(imei);
                            }
                        }
                        return buffer.toString();
                    }
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    /**
     * MEID for CDMA phones
     */
    public static String getMeids() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                StringBuffer buffer = new StringBuffer();
                TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Service.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        int count = tm.getPhoneCount();
                        for (int i = 0; i < count; i++) {
                            if (i != 0) {
                                buffer.append("*");
                            }
                            String meid = tm.getMeid(i);
                            if (!TextUtils.isEmpty(meid)) {
                                buffer.append(meid);
                            }
                        }
                        return buffer.toString();
                    }
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }


    /**
     * International Mobile Subscriber Identity
     */
    public static String getImsi() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    return telephonyManager.getSubscriberId();
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getSerialNo() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Build.getSerial();
                }
                return Build.SERIAL;
            }
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getAdid() {
        try {
            String adid = Settings.Secure.getString(UApplication.getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(adid)) {
                return adid;
            }
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }


    public static String getSimSerialNumber() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    return telephonyManager.getSimSerialNumber();
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }


    public static String getPhoneNumber() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                TelephonyManager manager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                return manager.getLine1Number();
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }


    public static String getUserAgent() {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                return WebSettings.getDefaultUserAgent(UApplication.getContext());
            }
            Locale locale = Locale.getDefault();
            StringBuilder buffer = new StringBuilder();
            final String version = Build.VERSION.RELEASE;
            if (version.length() > 0) {
                buffer.append(version);
            } else {
                buffer.append("1.0");
            }
            buffer.append("; ");
            final String language = locale.getLanguage();
            if (language != null) {
                buffer.append(language.toLowerCase());
                final String country = locale.getCountry();
                if (country != null) {
                    buffer.append("-");
                    buffer.append(country.toLowerCase());
                }
            } else {
                buffer.append("en");
            }
            // add the model for the release build
            if ("REL".equals(Build.VERSION.CODENAME)) {
                final String model = Build.MODEL;
                if (model.length() > 0) {
                    buffer.append("; ");
                    buffer.append(model);
                }
            }
            final String id = Build.ID;
            if (id.length() > 0) {
                buffer.append(" Build/");
                buffer.append(id);
            }
            Resources resources = UApplication.getContext().getResources();
            int uaid = resources.getIdentifier("android:string/web_user_agent", "string", "android");
            final String base = resources.getText(uaid).toString();
            return String.format(base, buffer, "Mobile ");
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }
}
