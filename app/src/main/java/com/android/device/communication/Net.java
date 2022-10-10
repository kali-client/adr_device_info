package com.android.device.communication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.device.UApplication;
import com.android.device.utils.ULog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

public class Net {
    @SuppressLint("Range")
    public static String getApn() {//网络接入点名称  电信:ctnet 移动:cmnet
        final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        String apnName = "";
        try {
            Cursor cursor = UApplication.getContext().getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, (String) null, (String[]) null, (String) null);
            if (cursor != null) {
                cursor.moveToFirst();
                int counts = cursor.getCount();
                if (counts != 0 && !cursor.isAfterLast()) {
                    apnName = cursor.getString(cursor.getColumnIndex("apn"));
                }

                cursor.close();
            } else {
                cursor = UApplication.getContext().getContentResolver().query(PREFERRED_APN_URI, (String[]) null, (String) null, (String[]) null, (String) null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    apnName = cursor.getString(cursor.getColumnIndex("user"));
                    cursor.close();
                }
            }
        } catch (Exception var7) {
            try {
                ConnectivityManager conManager = (ConnectivityManager) UApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = conManager.getActiveNetworkInfo();
                apnName = ni.getExtraInfo();
            } catch (Exception var6) {
                apnName = "";
            }
        }
        return apnName == null ? "" : apnName.replace("\"", "");
    }

    public static String getIp() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !(inetAddress instanceof Inet6Address)) {
                        if (!first) {
                            result.append(',');
                        }

                        result.append(inetAddress.getHostAddress());
                        first = false;
                    }
                }
            }
            return result.toString();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getLinkedWifi() {
        try {
            WifiManager wifiManager = (WifiManager) UApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            JSONObject jsonObject = new JSONObject();
            if (connectionInfo != null) {
                jsonObject.put("ssid", connectionInfo.getSSID().replace("\"", ""));//wifi名称
                jsonObject.put("bssid", connectionInfo.getBSSID());//mac
            }
            try {
                if (connectionInfo != null) {
                    jsonObject.put("ip", FormatString(dhcpInfo.ipAddress));
                    jsonObject.put("mask", FormatString(dhcpInfo.netmask));
                    jsonObject.put("gateway", FormatString(dhcpInfo.gateway));
                    jsonObject.put("dns", FormatString(dhcpInfo.dns1));
                }
            } catch (Exception e) {
                ULog.e(e);
            }
            return jsonObject.toString();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getNetworkType() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) UApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return networkInfo.getTypeName();
            }
        } catch (Throwable e) {
            ULog.e(e);

        }
        return "";
    }

    public static String getMacAddress() {
        StringBuilder sb = new StringBuilder();
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }

            if (networkInterface == null) {
                return "";
            } else {
                byte[] address = networkInterface.getHardwareAddress();
                byte[] var4 = address;
                int var5 = address.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    byte b = var4[var6];
                    sb.append(String.format("%02X:", b));
                }

                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                return sb.toString();
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getWifiList(int limit) {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                JSONArray jsonArray = new JSONArray();
                WifiManager wifiManager = (WifiManager) UApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResults = wifiManager.getScanResults();
                Collections.sort(scanResults, new Comparator<ScanResult>() {
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return rhs.level - lhs.level;
                    }
                });
                for (int i = 0; i < scanResults.size() && i < limit; ++i) {
                    JSONObject jsonObject = new JSONObject();
                    ScanResult scanResult = scanResults.get(i);
                    jsonObject.put("ssid", scanResult.SSID);
                    jsonObject.put("ssid", scanResult.SSID);
//                    jsonObject.put("bssid", scanResult.BSSID.replace("2", "1").replace("a", "b"));
                    jsonObject.put("bssid", scanResult.BSSID);
                    jsonObject.put("level", WifiManager.calculateSignalLevel(scanResult.level, 1001));
                    jsonObject.put("capabilities", scanResult.capabilities);
                    jsonObject.put("frequency", scanResult.frequency);
                    jsonObject.put("describeContents", scanResult.describeContents());
                    jsonArray.put(jsonObject);
                }
                return jsonArray.toString();
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getBaseStationId() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                    if (cdmaCellLocation != null) {
                        int cid = cdmaCellLocation.getBaseStationId(); //获取cdma基站识别标号 BID
                        int lac = cdmaCellLocation.getNetworkId(); //获取cdma网络编号NID
                        return cid + "," + lac;
                        //int sid = cdmaCellLocation.getSystemId(); //用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
                    }
                } else {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                    if (gsmCellLocation != null) {
                        int cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
                        int lac = gsmCellLocation.getLac(); //获取gsm网络编号
                        return cid + "," + lac;
                    }
                }
            }
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    public static String getBaseStationId1() {
        try {
            Context context = UApplication.getContext();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED //
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return String.valueOf(telephonyManager.getCellLocation());
        } catch (Throwable ignored) {
        }
        return "";
    }

    public static String getBluetoothAddress() {
        try {
            String adr = Settings.Secure.getString(UApplication.getContext().getContentResolver(), "bluetooth_address");
            if (TextUtils.isEmpty(adr)) {
                adr = "02:00:00:00:00:00";
            }
            return adr;
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }

    public static JSONObject getAllCellInfo() {
        try {
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                JSONArray gsmArr = new JSONArray();
                JSONArray cdmaArr = new JSONArray();
                JSONArray lteArr = new JSONArray();
                List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoGsm) {
                        try {
                            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                            CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();

                            JSONObject gsm = new JSONObject();
                            if (cellIdentity.getCid() != Integer.MAX_VALUE) {
                                gsm.put("cid", cellIdentity.getCid());
                            }

                            if (cellIdentity.getLac() != Integer.MAX_VALUE) {
                                gsm.put("lac", cellIdentity.getLac());
                            }
                            if (cellIdentity.getMcc() != Integer.MAX_VALUE) {
                                gsm.put("mcc", cellIdentity.getMcc());
                            }

                            if (cellIdentity.getMnc() != Integer.MAX_VALUE) {
                                gsm.put("mnc", cellIdentity.getMnc());
                            }
                            gsmArr.put(gsm);
                        } catch (Throwable e) {
                            ULog.e(e);
                        }

                    } else if (cellInfo instanceof CellInfoCdma) {
                        try {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                            CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
                            JSONObject cdma = new JSONObject();
                            if (cellIdentity.getNetworkId() != Integer.MAX_VALUE) {
                                cdma.put("mNetworkId", cellIdentity.getNetworkId());
                            }

                            if (cellIdentity.getSystemId() != Integer.MAX_VALUE) {
                                cdma.put("mSystemId", cellIdentity.getSystemId());
                            }

                            if (cellIdentity.getBasestationId() != Integer.MAX_VALUE) {
                                cdma.put("mBasestationId", cellIdentity.getBasestationId());
                            }

                            if (cellIdentity.getLatitude() != Integer.MAX_VALUE) {
                                cdma.put("mLatitude", cellIdentity.getLatitude());
                            }

                            if (cellIdentity.getLongitude() != Integer.MAX_VALUE) {
                                cdma.put("mLongitude", cellIdentity.getLongitude());
                            }
                            cdmaArr.put(cdma);
                        } catch (Throwable e) {
                            ULog.e(e);
                        }

                    } else if (cellInfo instanceof CellInfoLte) {
                        try {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
                            JSONObject lte = new JSONObject();
                            if (cellIdentity.getCi() != Integer.MAX_VALUE) {
                                lte.put("ci", cellIdentity.getCi());
                            }
                            if (cellIdentity.getMcc() != Integer.MAX_VALUE) {
                                lte.put("mnc", cellIdentity.getMcc());
                            }

                            if (cellIdentity.getPci() != Integer.MAX_VALUE) {
                                lte.put("pic", cellIdentity.getPci());
                            }

                            if (cellIdentity.getTac() != Integer.MAX_VALUE) {
                                lte.put("tac", cellIdentity.getTac());
                            }

                            if (cellIdentity.getMcc() != Integer.MAX_VALUE) {
                                lte.put("mcc", cellIdentity.getMcc());
                            }
                            lteArr.put(lte);
                        } catch (Throwable e) {
                            ULog.e(e);
                        }
                    }
                }
                JSONObject cellInfo = new JSONObject();
                if (gsmArr.length() > 0) {
                    cellInfo.put("gsm", gsmArr);
                }

                if (cdmaArr.length() > 0) {
                    cellInfo.put("cdma", cdmaArr);
                }

                if (lteArr.length() > 0) {
                    cellInfo.put("lte", lteArr);
                }
                return cellInfo;
            }
        } catch (Exception e) {
            ULog.e(e);
        }
        return null;
    }

    public static JSONArray getNeighboringCellInfo() {
//        try {
//            TelephonyManager manager = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
//            int strength = 0;
//            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT <= 29) {
//                List<NeighboringCellInfo> infoLists = manager.getNeighboringCellInfo();
//                if (infoLists != null && infoLists.size() > 0) {
//                    JSONArray nbcis = new JSONArray();
//                    for (NeighboringCellInfo info : infoLists) {
//                        strength += (-133 + 2 * info.getRssi());// 获取邻区基站信号强度
//                        JSONObject jobj = new JSONObject();
//                        jobj.put("rssi", info.getRssi());
//                        jobj.put("strength", strength);
//                        jobj.put("cid", info.getCid());
//                        jobj.put("lac", info.getLac());
//                        nbcis.put(jobj);
//                    }
//                    return nbcis;
//                }
//
//            }
//        } catch (Throwable e) {
//        }

        return null;
    }

    public static String getWifiProxy() {
        try {
            String result = "";
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");
            if (!TextUtils.isEmpty(proxyHost)) {
                result = proxyHost + ":" + proxyPort;
            }
            return result;
        } catch (Exception e) {
            ULog.e(e);
        }
        return "";
    }

    static String FormatString(int value) {
        String strValue = "";
        byte[] ary = intToByteArray(value);
        for (int i = ary.length - 1; i >= 0; i--) {
            strValue += (ary[i] & 0xFF);
            if (i > 0) {
                strValue += ".";
            }
        }
        return strValue;
    }

    static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getNetWorkInfo() {
        try {
            TelephonyManager tm = (TelephonyManager) UApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String networkCountryIso = tm.getNetworkCountryIso();
            String networkOperator = tm.getNetworkOperator();
            String networkSpecifier = tm.getNetworkSpecifier();
            int networkType = tm.getNetworkType();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("networkCountryIso", networkCountryIso);
            jsonObject.put("networkOperator", networkOperator);
            jsonObject.put("networkSpecifier", networkSpecifier);
            jsonObject.put("networkType", networkType);
            return jsonObject.toString();
        } catch (Exception e) {
            ULog.e(e);
        }


        return null;
    }


    public static boolean isWifi(Context context) {
        return Wifi.getNetWorkStatus(context) == 1;
    }

    static class Wifi {
        private static Integer networkType;

        public final static int getNetWorkStatus(Context context) {
            Integer num = networkType;
            Integer num2 = 0;
            if (num != null && (num == null || num.intValue() != 0)) {
                Integer num3 = networkType;
                if (num3 != null) {
                    return num3.intValue();
                }
                return 0;
            } else if (Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED) {
                Object systemService = context.getSystemService(Context.CONNECTIVITY_SERVICE);
                TelephonyManager telephonyManager = null;
                if (!(systemService instanceof ConnectivityManager)) {
                    systemService = null;
                }
                ConnectivityManager connectivityManager = (ConnectivityManager) systemService;
                if (connectivityManager != null) {
                    if (!isNetworkAvailable(connectivityManager)) {
                        networkType = num2;
                        return 0;
                    } else if (isWiFiNetwork(connectivityManager)) {
                        networkType = 1;
                        return 1;
                    }
                }

                //TODO  mobileNetworkType
//            Object systemService2 = context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (systemService2 instanceof TelephonyManager) {
//                telephonyManager = (TelephonyManager) systemService2;
//            }
//            Integer valueOf = Integer.valueOf(INSTANCE.mobileNetworkType(context, telephonyManager, connectivityManager));
//            networkType = valueOf;
//            if (valueOf != null) {
//                return valueOf.intValue();
//            }
                return 0;
            } else {
                networkType = num2;
                if (num2 != null) {
                    return num2.intValue();
                }
                return 0;
            }
        }

        private static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
            NetworkCapabilities networkCapabilities;
            if (connectivityManager == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                if (activeNetwork == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
                    return false;
                }
                return isNetworkValid(networkCapabilities);
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @SuppressLint("WrongConstant")
        @TargetApi(Build.VERSION_CODES.M)
        private static boolean isNetworkValid(NetworkCapabilities networkCapabilities) {
            if (networkCapabilities == null || Build.VERSION.SDK_INT < 21) {
                return false;
            }
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    || networkCapabilities.hasTransport(7)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    || networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }

        private static boolean isWiFiNetwork(ConnectivityManager connectivityManager) {
            NetworkCapabilities networkCapabilities;
            NetworkInfo networkInfo = null;
            Network network = null;
            if (Build.VERSION.SDK_INT >= 23) {
                if (connectivityManager != null) {
                    network = connectivityManager.getActiveNetwork();
                }
                if (network == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(network)) == null) {
                    return false;
                }
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getNetworkInfo(1);
            }
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }
}
