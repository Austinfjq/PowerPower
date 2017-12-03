package cn.edu.zju.powerpower;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class BatteryHealthActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar pbb;
    private Drawable progressDrawable;
    boolean is_batterying = false;
    private int cap;
    private ImageView im1, im2, im3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_health);

        pbb = (ProgressBar) findViewById(R.id.pgb_battery);
        im1 = (ImageView) findViewById(R.id.im1);
        im2 = (ImageView) findViewById(R.id.im2);
        im3 = (ImageView) findViewById(R.id.im3);

        new Thread() {
            @Override
            public void run() {
                int battery_cap = cap;
                while (cap < 100) {
                    try {
                        int sleep_time = battery_cap * 8;
                        Thread.sleep(sleep_time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (is_batterying) {
                        if (battery_cap >= 99)
                            battery_cap = cap;
                        else
                            battery_cap++;
                        pbb.setProgress(battery_cap);
                        battery_cap = ((int) pbb.getProgress());
                    } else {
                        pbb.setProgress(battery_cap);
                        battery_cap = cap;
                    }
                }
            }
        }.start();

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(mReciver, mIntentFilter);

    }


    private BroadcastReceiver mReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            TextView mTemTextView;
            Resources resource = (Resources) getBaseContext().getResources();
            ColorStateList mycolor = (ColorStateList) resource.getColorStateList(R.color.text_color);

            //the temp of the battery
            Integer tem = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            String s = tem / 10 + "." + tem % 10 + "℃";
            mTemTextView = (TextView) findViewById(R.id.temp);
            if (tem < 200) {
                mTemTextView.setTextSize(40);
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_cold);
            } else if (tem > 300) {
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_overheat);
                mTemTextView.setTextSize(40);
            } else {
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_good);
                mTemTextView.setTextSize(25);
            }
            mTemTextView.setTextColor(mycolor);
            mTemTextView.setText(s);

            //the v of the battery
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            s = voltage / 1000 + "." + voltage % 1000 + "V";
            mTemTextView = (TextView) findViewById(R.id.vol);
            if (voltage < 2000) {
                mTemTextView.setTextSize(40);
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_cold);
            } else if (voltage > 5000) {
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_overheat);
                mTemTextView.setTextSize(40);
            } else {
                mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_good);
                mTemTextView.setTextSize(25);
            }
            mTemTextView.setTextColor(mycolor);
            mTemTextView.setText(s);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            cap = level * 100 / scale ;
            pbb = (ProgressBar) findViewById(R.id.pgb_battery);
            if (cap <= 20) {
                progressDrawable = getResources().getDrawable(R.drawable.layer_bar);
                pbb.setProgressDrawable(progressDrawable);
            } else {
                progressDrawable = getResources().getDrawable(R.drawable.layer_bar2);
                pbb.setProgressDrawable(progressDrawable);
            }
            pbb.setProgress(cap);
            s = cap + "%";
            mTemTextView = (TextView) findViewById(R.id.capacity);
            mTemTextView.setText(s);

            double battery_time = cap * 24.0 / 100;
            int int_time_f = (int) battery_time;
            int int_time_b = (int) (battery_time * 60 - int_time_f * 60);
            s = Integer.toString(int_time_f) + " H " + Integer.toString(int_time_b) + " Min";
            mTemTextView = (TextView) findViewById(R.id.time);
            mTemTextView.setText(s);

            mTemTextView = (TextView) findViewById(R.id.hint);
            if (int_time_f < 10)
                mTemTextView.setText("电量不足，请尽快充电");
            else
                mTemTextView.setText("电量充足，无需充电");

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_FULL:
                    is_batterying = false;
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    is_batterying = true;
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    is_batterying = false;
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    is_batterying = false;
                    break;
                default:
                    is_batterying = false;
                    break;
            }
            if ((cap < 70) && !is_batterying) {
                im1.getDrawable().setLevel(4);
                im2.getDrawable().setLevel(1);
                im3.getDrawable().setLevel(1);
            } else if ((cap < 50) && !is_batterying) {
                im1.getDrawable().setLevel(5);
                im2.getDrawable().setLevel(1);
                im3.getDrawable().setLevel(1);
            } else if ((cap < 20) && !is_batterying) {
                im1.getDrawable().setLevel(6);
                im2.getDrawable().setLevel(1);
                im3.getDrawable().setLevel(1);
            } else if (!is_batterying) {
                im1.getDrawable().setLevel(2);
                im2.getDrawable().setLevel(1);
                im3.getDrawable().setLevel(1);
            } else if ((cap < 100) && is_batterying) {
                im1.getDrawable().setLevel(1);
                im2.getDrawable().setLevel(3);
                im3.getDrawable().setLevel(1);
            } else if (is_batterying) {
                im1.getDrawable().setLevel(1);
                im2.getDrawable().setLevel(1);
                im3.getDrawable().setLevel(2);
            }

            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            mTemTextView = (TextView) findViewById(R.id.health);
            mycolor = (ColorStateList) resource.getColorStateList(R.color.text_color);
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    s = "cold";
                    mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_cold);
                    mTemTextView.setTextSize(40);
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    s = "dead";
                    mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_dead);
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    s = "good";
                    mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_good);
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    s = "overheat";
                    mycolor = (ColorStateList) resource.getColorStateList(R.color.battery_overheat);
                    mTemTextView.setTextSize(40);
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    s = "over voltage";
                    break;
                default:
                    s = "unknown";
                    break;
            }
            mTemTextView.setTextColor(mycolor);
            mTemTextView.setText(s);
        }
    };
}
