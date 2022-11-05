package com.android;

import android.Manifest;
import android.app.Activity;
import android.app.TestServiceManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.aliyun.upload.TestMaven;
import com.android.assemble.CollectDeviceInfo;
import com.android.device.R;
import com.android.device.comm.Net;
import com.android.device.comm.SimCard;
import com.android.device.ids.IDs;
import com.android.utils.Cmd;
import com.android.utils.Http;
import com.android.utils.ULog;

import java.util.Locale;


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

        TestServiceManager testServiceManager = (TestServiceManager) getSystemService("test_service");
        tvInfo.setText("TestServiceManager:" + testServiceManager.toString());
        testServiceManager.setValue("test", "12345");
        tvInfo.setText("value:" + testServiceManager.getValue("test"));
//        tvInfo.setText("mac0:" + Net.getMacAddress() + "   mac1：" + Net.getMac1(this));
        Log.d("mac", Net.getMacAddress());
    }

    public void uploadData(View view) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Http.uploadData("kali", Build.MODEL + "_" + Build.VERSION.SDK_INT + "_DeviceInfo", CollectDeviceInfo.getDeviceInfo(), null);
            }
        });
    }
}