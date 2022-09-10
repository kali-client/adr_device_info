package com.android.device;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.device.software.Media;


public class MainActivity extends Activity {

    private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tv_info);

    }

    public void getDeviceInfo(View view) {
        tvInfo.setText(Media.getMediaInfo().toString());
    }
}