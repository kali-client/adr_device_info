package com.android.device;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.android.device.communication.SimCard;
import com.android.device.ids.IDs;
import com.android.device.software.Build;
import com.android.device.software.Media;


public class MainActivity extends Activity {

    private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},0x001);
        tvInfo = findViewById(R.id.tv_info);




    }

    public void getDeviceInfo(View view) {
//        tvInfo.setText("hasSimCard:"+SimCard.hasSimCard() + "\n hasIccCard:" + SimCard.hasIccCard() + "\nSimOperator:" + SimCard.getSimOperator() + "\nSimSerialNumber:"+ IDs.getSimSerialNumber());
//        tvInfo.setText(SimCard.getGSMInfo().toString());
//        tvInfo.setText(Build.getBuildInfo().toString());
        tvInfo.setText(SimCard.getSimCardInfo().toString());
    }
}