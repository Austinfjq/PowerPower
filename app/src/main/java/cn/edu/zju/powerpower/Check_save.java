package cn.edu.zju.powerpower;

import android.app.ActivityManager;
        import android.content.Context;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;

        import com.classic.adapter.CommonRecyclerAdapter;

        import java.lang.reflect.InvocationTargetException;
        import java.lang.reflect.Method;
        import java.util.ArrayList;
        import java.util.List;

        import rx.Observable;
        import rx.Subscriber;
        import rx.android.schedulers.AndroidSchedulers;
        import rx.schedulers.Schedulers;
        import rx.subscriptions.CompositeSubscription;

        import static cn.edu.zju.powerpower.utils.AppUtil.getAvailMemory;
        import android.accessibilityservice.AccessibilityServiceInfo;
        import android.app.ActivityManager;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.accessibility.AccessibilityManager;
        import android.widget.Toast;

        import com.classic.adapter.CommonRecyclerAdapter;
        import java.util.ArrayList;
        import java.util.List;
        import rx.Observable;
        import rx.Subscriber;
        import rx.android.schedulers.AndroidSchedulers;
        import rx.schedulers.Schedulers;
        import rx.subscriptions.CompositeSubscription;
        import java.text.SimpleDateFormat;

public class Check_save extends AppCompatActivity implements CommonRecyclerAdapter.OnItemClickListener {
    private Context mAppContext;
    private ArrayList<String> mPackageList;
    private PackageAdapter mPackageAdapter;
    private RecyclerView mRecyclerView;
    private CompositeSubscription mCompositeSubscription;
    private ActivityManager mActivityManager;
    static private long timecurrentTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_save);

        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        setSupportActionBar(toolbar);

        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mPackageList = new ArrayList<>();
        mCompositeSubscription = new CompositeSubscription();
        mAppContext = getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.memory_clean_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mPackageAdapter = new PackageAdapter(mAppContext);
        mRecyclerView.setAdapter(mPackageAdapter);
        mPackageAdapter.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                if ((System.currentTimeMillis() - timecurrentTimeMillis) / 1000 < 60)
                //                    Snackbar.make(view, "您刚刚清理过内存，过会儿再来吧", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();
                //                else {
                //if () 未开启无障碍模式
                String result = MemoryClean.ClearMemory(Check_save.this);
                //else 使用无障碍模式
                //ClearMemoryActivityAdvanced(view);
                Snackbar.make(view, result, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //                }
                //                timecurrentTimeMillis = System.currentTimeMillis();
                getProcessesList();
            }
        });
        Toast.makeText(Check_save.this, "您可以 \n1. 点击APP名 -> 清理进程 \n2. 点击扫帚 -> 一键清理",Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        getProcessesList();
    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        //if ()
        String result = MemoryClean.CleanOneProcess(Check_save

                .this, mPackageList.get(position));
        Snackbar.make(view, result, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //else{
        //            if(MemoryClean.checkEnabledAccessibilityService(MemoryCleanActivity.this)){
        //                MemoryClean.showPackageDetail(MemoryCleanActivity.this, mPackageList.get(position));
        //            }
        //        }

        //mPackageAdapter.remove(position);
        getProcessesList();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (null != mCompositeSubscription) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void getProcessesList(){
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override public void call(Subscriber<? super List<String>> subscriber) {
                mPackageList.clear();
                mPackageList.addAll(MemoryClean.getBackgroundProcesses(Check_save.this));
                subscriber.onNext(mPackageList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPackageAdapter));
    }



    private static final String TAG_ClearMemoryActivityFundamental = "ClearMemoryActivity";
    //    public boolean ClearMemoryActivityFundamental(View view){
    //        //To change body of implemented methods use File | Settings | File Templates.
    //        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
    //        List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();
    //        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
    //
    //        long beforeMem = getAvailMemory(MemoryCleanActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
    //        int count = 0;
    //        if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
    //            ProcessManager.ProcessInfo appProcessInfo = infoList.get(i);
    //            Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.name);
    //
    //            String PackageName = appProcessInfo.getPackageName();
    //            if (!PackageName.equals("cn.edu.zju.powerpower")) {
    //                Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
    //                mActivityManager.killBackgroundProcesses(PackageName);
    //            }
    //            count++;
    //        }
    //
    //        long afterMem = getAvailMemory(MemoryCleanActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);
    //        Snackbar.make(view, "清理了 " + count + " 个进程\n空闲内存 " + afterMem + "M", Snackbar.LENGTH_LONG)
    //                .setAction("Action", null).show();
    //        return true;
    //    }

    //    public boolean ClearMemoryActivityAdvanced(View view){
    //        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
    //        List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();
    //        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
    //
    //        long beforeMem = getAvailMemory(MemoryCleanActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
    //        int count = 0;
    //        if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
    //            ProcessManager.ProcessInfo appProcessInfo = infoList.get(i);
    //            Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.name);
    //
    //            String PackageName = appProcessInfo.getPackageName();
    //            if (!PackageName.equals("cn.edu.zju.powerpower")) {
    //                Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
    //                //wuzhangai();
    //            }
    //            count++;
    //        }
    //
    //        long afterMem = getAvailMemory(MemoryCleanActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);
    //        Snackbar.make(view, "clear " + count + " process, " + (afterMem - beforeMem) + "M", Snackbar.LENGTH_LONG)
    //                .setAction("Action", null).show();
    //        return true;
    //    }

    //获取可用内存大小
    //    private long getAvailMemory(Context context) {
    //        // 获取android当前可用内存大小
    //        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    //        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    //        am.getMemoryInfo(mi);
    //        //mi.availMem; 当前系统的可用内存
    //        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    //        Log.d(TAG_ClearMemoryActivityFundamental, "可用内存---->>>" + mi.availMem / (1024 * 1024));
    //        return mi.availMem / (1024 * 1024);
    //    }
    //         安卓4可以使用
    //    public boolean ClearMemoryActivityFundamental(View view){
    //        //To change body of implemented methods use File | Settings | File Templates.
    //        //ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    //        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
    //        List<RunningAppProcessInfo> infoList = (RunningAppProcessInfo)ProcessManager.getRunningProcesses();
    //        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
    //
    //        long beforeMem = getAvailMemory(MainActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
    //        int count = 0;
    //        if (infoList != null) {
    //            for (int i = 0; i < infoList.size(); ++i) {
    //                RunningAppProcessInfo appProcessInfo = infoList.get(i);
    //                Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.processName);
    //                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
    //                Log.d(TAG_ClearMemoryActivityFundamental, "importance : " + appProcessInfo.importance);
    //
    //                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_FOREGROUND ) {
    //                    String[] pkgList = appProcessInfo.pkgList;
    //                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
    //                        Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + pkgList[j]);
    //                        am.killBackgroundProcesses(pkgList[j]);
    //                        count++;
    //                    }
    //                }
    //
    //            }
    //        }
    //
    //        long afterMem = getAvailMemory(MainActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);
    //        Snackbar.make(view, "clear " + count + " process, " + (afterMem - beforeMem) + "M", Snackbar.LENGTH_LONG)
    //                .setAction("Action", null).show();
    //        return true;
    //    }

    // forceStopPackage方法
    //    private static final String TAG_ClearMemoryActivityFundamental = "ClearMemoryActivity";
    //    public boolean ClearMemoryActivityFundamental(View view){
    //        //To change body of implemented methods use File | Settings | File Templates.
    //        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    //        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
    //        List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();
    //        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
    //
    //        long beforeMem = getAvailMemory(MemoryCleanActivity.this);
    //        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
    //        int count = 0;
    //        if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
    //            ProcessManager.ProcessInfo appProcessInfo = infoList.get(i);
    //            Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.name);
    //
    //            String PackageName = appProcessInfo.getPackageName();
    //            if (!PackageName.equals("cn.edu.zju.powerpower")) {
    //                Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
    //                try {
    //                    Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
    //                    forceStopPackage.setAccessible(true);
    //                    forceStopPackage.invoke(am, PackageName);
    //                } catch (NoSuchMethodException e) {
    //                    e.printStackTrace();
    //                } catch (IllegalAccessException e) {
    //                    e.printStackTrace();
    //                } catch (IllegalArgumentException e) {
    //                    e.printStackTrace();
    //                } catch (InvocationTargetException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            count++;
    //        }
}