package com.android.device;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.device.communication.Net;
import com.android.device.communication.SimCard;
import com.android.device.software.Screen;
import com.android.device.utils.ULog;

import java.util.TimeZone;


public class MainActivity extends Activity {

    private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION},0x001);
        tvInfo = findViewById(R.id.tv_info);



    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void getDeviceInfo(View view) {
//        tvInfo.setText("hasSimCard:"+SimCard.hasSimCard() + "\n hasIccCard:" + SimCard.hasIccCard() + "\nSimOperator:" + SimCard.getSimOperator() + "\nSimSerialNumber:"+ IDs.getSimSerialNumber());
//        tvInfo.setText(SimCard.getGSMInfo().toString());
//        tvInfo.setText(Build.getBuildInfo().toString());
//        tvInfo.setText(IDs.getImei());
//        tvInfo.setText(SimCard.getSimCardInfo().toString());


        tvInfo.setText("getRefreshRate:"+Screen.getRefreshRate(this));
        ULog.d("SimSerialNumber:"+SimCard.getSimSerialNumber());
        ULog.d("persist.sys.timezone:"+ TimeZone.getDefault().getID());
    }
}