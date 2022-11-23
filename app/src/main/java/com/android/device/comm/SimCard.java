package com.android.device.comm;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SimCard {

    public static boolean hasIccCard() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.hasIccCard();
        } catch (Exception e) {
            ULog.e(e);
        }
        return false;
    }

    public static boolean hasSimCard() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                int simState = telephonyManager.getSimState();
                switch (simState) {
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                    case TelephonyManager.SIM_STATE_ABSENT:
                        return false;
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return true;
    }

    public static String getSimOperator() {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                return telephonyManager.getSimOperator();
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        //返回 中国联通或China Unicom，中国电信或China Telecom，中国移动或China Mobile 返回什么根据当前设备使用的数据卡而定
        return tm.getSimOperatorName();
    }

    /**
     * int NO_PHONE = 0;
     * int GSM_PHONE = 1;
     * int CDMA_PHONE = 2;
     * int SIP_PHONE  = 3;
     */
    public static int getPhoneType() {
        TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static CharSequence getSimCarrierIdName() {
        TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCarrierIdName();
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public static int getSimCarrierId() {
        TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCarrierId();
    }


    public static JSONObject getGSMInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                    && ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                TelephonyManager mTelephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephonyManager != null) {
                    String operator = mTelephonyManager.getNetworkOperator();
                    if (!TextUtils.isEmpty(operator) && operator.length() >= 5) {
                        String mcc = operator.substring(0, 3);
                        String mnc = operator.substring(3, 5);
                        CellLocation location = mTelephonyManager.getCellLocation();
                        if (location != null && location instanceof GsmCellLocation) {
                            GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
                            int lac = gsmCellLocation.getLac();
                            int cellId = gsmCellLocation.getCid();
                            jsonObject.put("mcc", mcc);
                            jsonObject.put("mnc", mnc);
                            jsonObject.put("lac", lac);
                            jsonObject.put("cid", cellId);

                        } else if (location != null && location instanceof CdmaCellLocation) {
                            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) location;
                            int lac = cdmaCellLocation.getNetworkId();
                            int cellId = cdmaCellLocation.getBaseStationId();
                            jsonObject.put("mcc", mcc);
                            jsonObject.put("mnc", mnc);
                            jsonObject.put("lac", lac);
                            jsonObject.put("cid", cellId);
                        }
                    }
                    return jsonObject;
                }
            }

        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

    public static String getSubscriberId() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED
                    && Build.VERSION.SDK_INT < 29) {
                TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    return telephonyManager.getSubscriberId();
                }
            }
        } catch (Throwable e) {

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

        }
        return "";
    }

    public static JSONObject queryTelephonySimInfo() {
        Uri uri = Uri.parse("content://telephony/siminfo"); //访问raw_contacts表
        ContentResolver resolver = UApplication.getContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "icc_id", "sim_id", "display_name", "carrier_name", "name_source", "color", "number", "display_number_format", "data_roaming", "mcc", "mnc"}, null, null, null);
        JSONObject jsonObject = new JSONObject();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    String key = cursor.getColumnName(i);
                    String value = cursor.getString(i);
                    try {
                        jsonObject.put(key, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            cursor.close();
        }
        return jsonObject;
    }

    public static JSONObject getSimCardInfo() {
        JSONObject info = new JSONObject();
        try {
            info.put("hasIccCard", hasIccCard());
            info.put("hasSimCard", hasSimCard());
            info.put("phoneType", getPhoneType());

            info.put("simOperator", getSimOperator());
            info.put("simSerialNumber", getSimSerialNumber());
            info.put("subscriberId", getSubscriberId() + "");
            info.put("phoneNumber", getPhoneNumber());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.put("simCarrierId", getSimCarrierId());
            }
            info.put("gsmInfo", getGSMInfo());
            info.put("simOperatorName", getSimOperatorName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.put("simCarrierIdName", getSimCarrierIdName());
            }
            info.put("queryTelephonySimInfo", queryTelephonySimInfo());
        } catch (Exception e) {
            ULog.e(e);
        }

        return info;
    }


}
