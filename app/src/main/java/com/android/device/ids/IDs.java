package com.android.device.ids;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaDrm;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebSettings;

import androidx.core.app.ActivityCompat;

import com.android.UApplication;
import com.android.utils.MD5;
import com.android.utils.ULog;

import org.json.JSONObject;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

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
    public static String getIMEIs() {
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
    public static String getMEIDs() {
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
    public static String getIMSI() {
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

    public static String getAndroidId() {
        try {
            @SuppressLint("HardwareIds") String adid = Settings.Secure.getString(UApplication.getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
//            Settings.System.getString(UApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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

    public static String getGoogleADID(Context context) {
        return AdvertisingIdClient.getGoogleAdId(context);
    }

    public static String getDrmId() {
        try {
            UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
            MediaDrm wvDrm = new MediaDrm(wideVineUuid);
            byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
//            Arrays.toString(wideVineId)
//            return android.util.Base64.encodeToString(wideVineId, Base64.NO_WRAP);
            return MD5.stringToMD5(new String(wideVineId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    static class AdvertisingIdClient {
        /**
         * 这个方法是耗时的，不能在主线程调用
         */
        public static String getGoogleAdId(Context context) {
//        if (Looper.getMainLooper() == Looper.myLooper()) {
//            return "Cannot call in the main thread, You must call in the other thread";
//        }
            try {
                PackageManager pm = context.getPackageManager();
                pm.getPackageInfo("com.android.vending", 0);
                AdvertisingConnection connection = new AdvertisingConnection();
                Intent intent = new Intent(
                        "com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
                    try {
                        AdvertisingInterface adInterface = new AdvertisingIdClient.AdvertisingInterface(
                                connection.getBinder());
                        return adInterface.getId();
                    } finally {
                        context.unbindService(connection);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        private static final class AdvertisingConnection implements ServiceConnection {
            boolean retrieved = false;
            private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);

            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    this.queue.put(service);
                } catch (InterruptedException localInterruptedException) {
                }
            }

            public void onServiceDisconnected(ComponentName name) {
            }

            public IBinder getBinder() throws InterruptedException {
                if (this.retrieved)
                    throw new IllegalStateException();
                this.retrieved = true;
                return this.queue.take();
            }
        }

        private static final class AdvertisingInterface implements IInterface {
            private IBinder binder;

            public AdvertisingInterface(IBinder pBinder) {
                binder = pBinder;
            }

            public IBinder asBinder() {
                return binder;
            }

            public String getId() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                String id;
                try {
                    data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    binder.transact(1, data, reply, 0);
                    reply.readException();
                    id = reply.readString();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
                return id;
            }

            public boolean isLimitAdTrackingEnabled(boolean paramBoolean)
                    throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                boolean limitAdTracking;
                try {
                    data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    data.writeInt(paramBoolean ? 1 : 0);
                    binder.transact(2, data, reply, 0);
                    reply.readException();
                    limitAdTracking = 0 != reply.readInt();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
                return limitAdTracking;
            }
        }
    }

    public static JSONObject getIDsInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imei", getImei());
            jsonObject.put("imeis", getIMEIs());
            jsonObject.put("deviceIds", getDeviceIds());
            jsonObject.put("meids", getMEIDs());
            jsonObject.put("meids", getMEIDs());
            jsonObject.put("imsi", getIMSI());
            jsonObject.put("serialNo", getSerialNo());
            jsonObject.put("androidId", getAndroidId());
            jsonObject.put("iccid", getSimSerialNumber());
            jsonObject.put("phoneNo", getPhoneNumber());
            jsonObject.put("userAgent", getUserAgent());
            jsonObject.put("googleADID", getGoogleADID(context));
            jsonObject.put("drmId", getDrmId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
