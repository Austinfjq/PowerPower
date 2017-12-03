package cn.edu.zju.powerpower;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.edu.zju.powerpower.base.BaseFragment;
import cn.edu.zju.powerpower.R;
import cn.edu.zju.powerpower.base.ActivityTack;
import cn.edu.zju.powerpower.base.BaseActivity;
import cn.edu.zju.powerpower.fragment.MainFragment;
import cn.edu.zju.powerpower.utils.SystemBarTintManager;
import cn.edu.zju.powerpower.utils.T;
import cn.edu.zju.powerpower.utils.UIElementsHelper;

import java.util.Date;

import butterknife.InjectView;


public class MainActivity extends BaseActivity   {

    @InjectView(R.id.container)
    FrameLayout container;

    ActionBar ab;
    private CharSequence mTitle;
    static public Integer batteryCap, batteryLevel, batteryScale;

    private View mFragmentContainerView;

    MainFragment mMainFragment;
    public static final long TWO_SECOND = 2 * 1000;
    long preTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        applyKitKatTranslucency();

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(mReciver, mIntentFilter);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new MainFragment();
            fm.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }

    private BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryCap = batteryLevel * 100 / batteryScale ;
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            // mTintManager.setTintColor(0xF00099CC);

            mTintManager.setTintDrawable(UIElementsHelper
                    .getGeneralActionBarBackground(this));

            getActionBar().setBackgroundDrawable(
                    UIElementsHelper.getGeneralActionBarBackground(this));

        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 截获后退键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();

            // 如果时间间隔大于2秒, 不处理
            if ((currentTime - preTime) > TWO_SECOND) {
                // 显示消息
                T.showShort(mContext, "再按一次退出应用程序");

                // 更新时间
                preTime = currentTime;

                // 截获事件,不再处理
                return true;
            } else {
                ActivityTack.getInstanse().exit(mContext);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}





//public class MainActivity extends AppCompatActivity {
//    private Button mMemoryCleanButton;
//    private Button mBatteryHealthButton;
//    private Button mPowerAnalysisButton;
//    private Button mSettingsButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//        mMemoryCleanButton = (Button) findViewById(R.id.memory_clean_button);
//        mMemoryCleanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, MemoryCleanActivity.class);
//                startActivity(i);
//            }
//        });
//        mBatteryHealthButton = (Button) findViewById(R.id.battery_health_button);
//        mBatteryHealthButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, BatteryHealthActivity.class);
//                startActivity(i);
//            }
//        });
//        mPowerAnalysisButton = (Button) findViewById(R.id.power_analysis_button);
//        mPowerAnalysisButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, PowerAnalysisActivity.class);
//                startActivity(i);
//            }
//        });
//        mSettingsButton = (Button) findViewById(R.id.settings_button);
//        mSettingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(i);
//            }
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

