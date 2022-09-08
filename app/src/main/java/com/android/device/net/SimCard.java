package com.android.device.net;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
}
