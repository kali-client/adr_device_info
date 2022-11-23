package com.android.device.comm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.android.UApplication;
import com.android.utils.ULog;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {

    private static android.location.Location bestLocation;

    public static JSONObject getLocation() {

        try {
            JSONObject jsonObject = new JSONObject();
            if (ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) UApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
                criteria.setAltitudeRequired(false);//不要求海拔
                criteria.setBearingRequired(false);//不要求方位
                criteria.setCostAllowed(true);// 允许有花费
                criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
                String locationProvider = locationManager.getBestProvider(criteria, true);
                bestLocation = locationManager.getLastKnownLocation(locationProvider);
                while (bestLocation == null) {
                    locationManager.requestLocationUpdates(locationProvider, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(android.location.Location location) {
                            bestLocation = location;
                            locationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }

                if (bestLocation != null) {
                    try {
                        jsonObject.put("latitude", bestLocation.getLatitude());
                        jsonObject.put("longitude", bestLocation.getLongitude());
                    } catch (JSONException e) {
                        ULog.e(e);
                    }
                }


            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }


//        List providers = locationManager.getProviders(true);
//        String locationProvider = "";
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {// 如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {// 如果是Network
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        } else {
//            Toast.makeText(UApplication.getContext(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
//            return jsonObject;
//        }

//        String locationProvider = locationManager.getBestProvider(criteria, true);
//        if (locationProvider == null) {
//            Toast.makeText(UApplication.getContext(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
//            return jsonObject;
//        }
        return null;
    }
}