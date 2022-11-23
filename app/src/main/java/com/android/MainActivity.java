package com.android;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.device.DInfo;
import com.android.device.R;


public class MainActivity extends Activity {

    private TextView tvInfo;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, 0x001);
        tvInfo = findViewById(R.id.tv_info);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void getDeviceInfo(final View view) {
//        String xInfo = DInfo.getXInfo(this);
        try {
            Class<?> clazz = Class.forName("com.bun.miitmdid.core.MdidSdkHelper");
            tvInfo.setText("MdidSdkHelper:" + clazz.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            tvInfo.setText("MdidSdkHelper:" + e.getMessage());
        }

//        tvInfo.setText(xInfo);
//        System.out.println(Locale.getDefault().getCountry().toLowerCase());
//        Log.d("xInfo",xInfo);
//        tvInfo.setText("hasSimCard:"+SimCard.hasSimCard() + "\n hasIccCard:" + SimCard.hasIccCard() + "\nSimOperator:" + SimCard.getSimOperator() + "\nSimSerialNumber:"+ IDs.getSimSerialNumber());
//        tvInfo.setText(SimCard.getGSMInfo().toString());
//        tvInfo.setText(Build.getBuildInfo().toString());
//        tvInfo.setText(IDs.getImei());
//        tvInfo.setText(SimCard.getSimCardInfo().toString());
//        tvInfo.setText("isWifi:" + Net.isWifi(this) + "  wlan0_address:" + Cmd.exe("cat /sys/class/net/wlan0/address") + "  countryCode" + Locale.getDefault().getCountry());

//        long start = SystemClock.elapsedRealtime();
//        tvInfo.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                count += 1;
//                long end = SystemClock.elapsedRealtime();
//                tvInfo.setText("" + (end - start));
//                //getikytDeviceInfo(view);
//            }
//        }, 200);
//
//        TestServiceManager testServiceManager = (TestServiceManager) getSystemService("test_service");
//        tvInfo.setText("TestServiceManager:" + testServiceManager.toString());
//        testServiceManager.setValue("test", "12345");
//        tvInfo.setText("value:" + testServiceManager.getValue("test"));
//        tvInfo.setText("mac0:" + Net.getMacAddress() + "   mac1：" + Net.getMac1(this));
//        Log.d("mac", Net.getMacAddress());
    }

    public void uploadData(View view) {
        DInfo.uploadDeviceInfo(this);
    }
}