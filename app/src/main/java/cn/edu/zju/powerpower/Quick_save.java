package cn.edu.zju.powerpower;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static cn.edu.zju.powerpower.utils.AppUtil.getAvailMemory;

public class Quick_save extends AppCompatActivity {
    boolean wifiFlag = true;
    boolean GPSFlag = true;
    boolean lightFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_save3);

        CheckBox wifi = (CheckBox)findViewById(R.id.wifi);
//        CheckBox GPS = (CheckBox)findViewById(R.id.GPS);
        CheckBox light = (CheckBox)findViewById(R.id.light);
        Button done = (Button)findViewById(R.id.done);
        Button rtn = (Button)findViewById(R.id.rtn);

        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                   wifiFlag = false;
            }
        });
//        GPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b)
//                    GPSFlag = false;
//            }
//        });

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    lightFlag = false;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** kill all processes*/
//                List<String> infoList = MemoryClean.getBackgroundProcesses(Quick_save.this);
//                ActivityManager mActivityManager = (ActivityManager) Quick_save.this.getSystemService(Context.ACTIVITY_SERVICE);
//                if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
//                    String PackageName = infoList.get(i);
//                    if (!PackageName.equals("cn.edu.zju.powerpower")) {
//                        mActivityManager.killBackgroundProcesses(PackageName);
//                    }
//                }

                closeGPS();
                if(wifiFlag)
                    closeWifi();
                if(lightFlag)
                    downLight();
                Toast.makeText(Quick_save.this, "kill successful", Toast.LENGTH_LONG).show();
            }
        });

        rtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rtnIn = new Intent(Quick_save.this, MemoryCleanActivity.class);
                startActivity(rtnIn);
            }
        });
    }

    /** close WIFI here*/
    private void closeWifi(){
        WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mWifiManager.setWifiEnabled(false);
    }

    /** close liuliang here*/
    private void colseData(){
    }

    private void closeGPS() {
        /** judge whether GSP is open here */
        LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if( gps ) { //GPS is open now
            new AlertDialog
                    .Builder(Quick_save.this)
                    .setTitle("温馨提示")
                    .setMessage("您的GPS信号处于打开状态，\n" +
                            "如果您不需要用,\n" +
                            "请关闭GPS信号")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }

//        Intent GPSIntent = new Intent();
//        GPSIntent.setClassName("com.android.settings",
//                "com.android.settings.widget.SettingsAppWidgetProvider");
//        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
//        GPSIntent.setData(Uri.parse("custom:3"));
//        try {
//            PendingIntent.getBroadcast(this, 0, GPSIntent, 0).send();
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
    }

    /** set down the light of the screen*/
    private void downLight() {
        ContentResolver mContentResolver = this.getContentResolver();

        /** set light mode to manual mode*/
        try {
            int mode = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            if(mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        /** set down system screen light*/
        int value = 255/20;
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, value);
    }

}
