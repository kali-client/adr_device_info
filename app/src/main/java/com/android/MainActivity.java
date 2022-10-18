package com.android;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.assemble.CollectDeviceInfo;
import com.android.device.R;
import com.android.device.comm.Net;
import com.android.device.comm.SimCard;
import com.android.device.ids.IDs;
import com.android.utils.Http;
import com.android.utils.ULog;


public class MainActivity extends Activity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, 0x001);
        tvInfo = findViewById(R.id.tv_info);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void getDeviceInfo(View view) {
//        tvInfo.setText("hasSimCard:"+SimCard.hasSimCard() + "\n hasIccCard:" + SimCard.hasIccCard() + "\nSimOperator:" + SimCard.getSimOperator() + "\nSimSerialNumber:"+ IDs.getSimSerialNumber());
//        tvInfo.setText(SimCard.getGSMInfo().toString());
//        tvInfo.setText(Build.getBuildInfo().toString());
//        tvInfo.setText(IDs.getImei());
//        tvInfo.setText(SimCard.getSimCardInfo().toString());
        tvInfo.setText(IDs.getAdid());
        ULog.d("AndroidID:" + IDs.getAdid());
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