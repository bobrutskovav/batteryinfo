package com.example.user23.battery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private ImageView batteryImageView;
    private TextView batteryPercentageView;
    private TextView batteryTechView;
    private TextView batteryStatusView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int capacity = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            float fPercent = ((float) level / (float) capacity) * 100f;
            int percent = Math.round(fPercent);
            int drawableLevel = percent * 100;
            batteryImageView.getDrawable().setLevel(drawableLevel);
            batteryPercentageView.setText(getString(R.string.battery_percent_format, percent));
            String batteryTechString = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            batteryTechView.setText(batteryTechString);
            int state = getBatteryStatus(intent);
            batteryStatusView.setText(getResources().getStringArray(R.array.battary_states)[state]);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryImageView = (ImageView) findViewById(R.id.battery_image);
        batteryPercentageView = (TextView) findViewById(R.id.battery_percentage);
        batteryTechView = (TextView) findViewById(R.id.battery_tech);
        batteryStatusView = (TextView) findViewById(R.id.battery_status);

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private boolean isBatteryPresent(Intent intent){
        return intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT,true);
    }
    private int getBatteryStatus(Intent intent){
        int status = 0;
        if (isBatteryPresent(intent)){
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,BatteryManager.BATTERY_STATUS_UNKNOWN);
        }
        return status;
    }
}
