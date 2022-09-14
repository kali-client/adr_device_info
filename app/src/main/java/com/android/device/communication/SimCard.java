package com.android.device.communication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.android.device.UApplication;
import com.android.device.utils.ULog;

import org.json.JSONObject;

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

    public static JSONObject getSimCardInfo() {
        JSONObject info = new JSONObject();
        try {
            info.put("hasIccCard", hasIccCard());
            info.put("hasSimCard", hasSimCard());
            info.put("simOperator", getSimOperator());
            info.put("simOperatorName", getSimOperatorName());
            info.put("simSerialNumber", getSimSerialNumber());
            info.put("subscriberId", getSubscriberId());
            info.put("phoneType", getPhoneType());
            info.put("phoneNumber", getPhoneNumber());
            info.put("gsmInfo", getGSMInfo());
        } catch (Exception e) {
            ULog.e(e);
        }

        return info;
    }
}
