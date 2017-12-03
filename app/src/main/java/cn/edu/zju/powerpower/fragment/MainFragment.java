package cn.edu.zju.powerpower.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.edu.zju.powerpower.BatteryHealthActivity;
import cn.edu.zju.powerpower.MainActivity;
import cn.edu.zju.powerpower.MemoryCleanActivity;
import cn.edu.zju.powerpower.PowerAnalysisActivity;
import cn.edu.zju.powerpower.R;
import cn.edu.zju.powerpower.SettingsActivity;
import cn.edu.zju.powerpower.base.BaseFragment;
import cn.edu.zju.powerpower.model.SDCardInfo;
import cn.edu.zju.powerpower.utils.AppUtil;
import cn.edu.zju.powerpower.utils.StorageUtil;
import cn.edu.zju.powerpower.widget.circleprogress.ArcProgress;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static cn.edu.zju.powerpower.MainActivity.batteryCap;
import static cn.edu.zju.powerpower.MainActivity.batteryLevel;
import static cn.edu.zju.powerpower.MainActivity.batteryScale;


public class MainFragment extends BaseFragment {

    @InjectView(R.id.arc_store)
    ArcProgress arcStore;

    @InjectView(R.id.arc_process)
    ArcProgress arcProcess;
    @InjectView(R.id.capacity)
    TextView capacity;

    Context mContext;

    private Timer timer;
    private Timer timer2;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    private void fillData() {
        // TODO Auto-generated method stub
        timer = null;
        timer2 = null;
        timer = new Timer();
        timer2 = new Timer();


//        long l = AppUtil.getAvailMemory(mContext);
//        long y = AppUtil.getTotalMemory(mContext);
//        final double x = (((y - l) / (double) y) * 100);
//        arcProcess.setProgress(batteryCap);

        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);


        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcProcess.getProgress() >= percentStore) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                            capacity.setText( " "  );
                        }

                    }
                });
            }
        }, 50, 20);

        arcStore.setProgress(0);

        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= MainActivity.batteryCap) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);


    }

    @OnClick(R.id.card1)
    void speedUp() {
        startActivity(MemoryCleanActivity.class);
    }


    @OnClick(R.id.card2)
    void BatteryHealth() {
        startActivity(BatteryHealthActivity.class);
    }


    @OnClick(R.id.card3)
    void PowerAnalysis() {
        Intent intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        startActivity(intentBatteryUsage);
    }

    @OnClick(R.id.card4)
    void Settings() {
        startActivity(SettingsActivity.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }
}
