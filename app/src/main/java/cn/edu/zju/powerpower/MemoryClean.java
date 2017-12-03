package cn.edu.zju.powerpower;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.app.ActivityManager;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static cn.edu.zju.powerpower.ProcessManager.getApplicationNameByPackageName;
import static cn.edu.zju.powerpower.utils.AppUtil.getAvailMemory;

/**
 * Created by Austin on 2017/11/20.
 */

public class MemoryClean {
    private static final String SERVICE_NAME    = "cn.edu.zju.powerpower/.HelpService";
    private static final String TAG_ClearMemoryActivityFundamental = "ClearMemoryActivity";
    private static final String PACKAGE         = "package";
    private static CompositeSubscription mCompositeSubscription;

    public static String ClearMemory(Context mAppContext){
        // if ()
            return QuickClearMemoryActivityFundamental(mAppContext);
        // else
//            return QuickClearMemoryActivityAdvanced(mAppContext);
    }
    public static String CleanOneProcess(Context mContext, String PackageNameToKill) {
        ActivityManager mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        long beforeMem = getAvailMemory(mContext) / (1024*1024);
        //if ()
            mActivityManager.killBackgroundProcesses(PackageNameToKill);
        // else{
//            if(checkEnabledAccessibilityService(mContext)) {
//                if (!PackageNameToKill.equals("cn.edu.zju.powerpower")) {
//                    Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageNameToKill);
//                    showPackageDetail(mContext, PackageNameToKill);
//                }
//                return null;
//            }
        //}
        long afterMem = getAvailMemory(mContext) / (1024*1024);
        List<String> infoList2 = getBackgroundProcesses(mContext);
        for (String pname: infoList2){
            if (pname.equals(PackageNameToKill))
                return new String("清理失败，建议您打开强力模式试试。\n高级模式 -> 强力模式");
        }
        return new String("清理了 " + getApplicationNameByPackageName(mContext, PackageNameToKill) + " \n空闲内存 " + afterMem + "M");
    }
    public static String QuickClearMemoryActivityFundamental(Context mContext){
        ActivityManager mActivityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        //To change body of implemented methods use File | Settings | File Templates.
        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        //List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();

        List<String> infoList = getBackgroundProcesses(mContext);

        long beforeMem = getAvailMemory(mContext) / (1024*1024);
        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
        if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
            //ProcessManager.ProcessInfo appProcessInfo = infoList.get(i);
            //Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.name);

            //String PackageName = appProcessInfo.getPackageName();
            String PackageName = infoList.get(i);
            Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + PackageName);
            if (!PackageName.equals("cn.edu.zju.powerpower")) {
                Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
                mActivityManager.killBackgroundProcesses(PackageName);
            }
        }

        long afterMem = getAvailMemory(mContext) / (1024*1024);
        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);

        List<String> infoList2 = getBackgroundProcesses(mContext);
        int count = 0;
        for(String pname :infoList){
            if (!infoList2.contains(pname)) {
                count++;
                Toast.makeText(mContext, getApplicationNameByPackageName(mContext, pname)+ "被成功清理了",Toast.LENGTH_SHORT).show();
            }
        }
        return new String("共有 "+infoList.size()+" 个进程，"+"成功清理了 " + count + " 个进程\n剩余空闲内存 " + afterMem + "M");
//        Snackbar.make(view, "清理了 " + count + " 个进程\n空闲内存 " + afterMem + "M", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }
    public static boolean checkEnabledAccessibilityService(Context mContext) {
        AccessibilityManager mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(SERVICE_NAME)) {
                return true;
            }
        }
        Toast.makeText(mContext, "PowerPower需要无障碍服务的权限",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mContext.startActivity(intent);
        return false;
    }
    public static void showPackageDetail(Context mContext, String packageName){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(PACKAGE, packageName, null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }
    public static String QuickClearMemoryActivityAdvanced(Context mContext) {
        int count = 0;
        List<String> infoList = getBackgroundProcesses(mContext);
        long beforeMem = getAvailMemory(mContext) / (1024*1024);
        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);

        if(checkEnabledAccessibilityService(mContext)){
            //showPackageDetail(mContext, "com.tencent.tim");
            if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
                String PackageName = infoList.get(i);
                Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + PackageName);
                if (!PackageName.equals("cn.edu.zju.powerpower")) {
                    Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
                    showPackageDetail(mContext, infoList.get(i));
                }
                count++;
            }
        }
        long afterMem = getAvailMemory(mContext) / (1024*1024);
        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);
        return null;
    }


    public static List<String> getBackgroundProcesses(Context mContext){
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<String> packageList = new ArrayList<>();
        List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();
        for (ProcessManager.ProcessInfo info : infoList) {
            //if (verifyPackageName(info.processName)) continue;
            final String packageName = info.getPackageName();
            if(!packageList.contains(packageName)){
                packageList.add(packageName);
            }
        }
        List<ActivityManager.RunningServiceInfo> allServices = mActivityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo info : allServices) {
            //if (verifyPackageName(info.process)) continue;
            final String packageName = packageNameFilter(info.process);
            if(!packageList.contains(packageName)){
                packageList.add(packageName);
            }
        }
        return packageList;
    }

    private static String packageNameFilter(String processName){
        return processName.contains(":") ? processName.split(":")[0] : processName;
    }
}



//    public static String QuickClearMemoryActivityAdvanced(Context mContext) {
//        ActivityManager mActivityManager = (ActivityManager) mContext
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        //To change body of implemented methods use File | Settings | File Templates.
//        //List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
//        //List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
//
//        //List<ProcessManager.ProcessInfo> infoList = ProcessManager.getRunningProcesses();
//        Process sh = null;
//        DataOutputStream os = null;
//        List<String> infoList = getBackgroundProcesses(mContext);
//        long beforeMem = getAvailMemory(mContext) / (1024*1024);
//        Log.d(TAG_ClearMemoryActivityFundamental, "-----------before memory info : " + beforeMem);
//
//        int count = 0;
//        if (infoList != null) for (int i = 0; i < infoList.size(); ++i) {
//            //ProcessManager.ProcessInfo appProcessInfo = infoList.get(i);
//            //Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + appProcessInfo.name);
//
//            //String PackageName = appProcessInfo.getPackageName();
//            String PackageName = infoList.get(i);
//            PackageName = "com.taobao.taobao";
//            Log.d(TAG_ClearMemoryActivityFundamental, "process name : " + PackageName);
//            if (!PackageName.equals("cn.edu.zju.powerpower")) {
//                Log.d(TAG_ClearMemoryActivityFundamental, "It will be killed, package name : " + PackageName);
//                try {
//                    //sh = Runtime.getRuntime().exec("su");
//                    os = new DataOutputStream(sh.getOutputStream());
//                    final String Command = "am force-stop "+PackageName+ "\n";
//                    os.writeBytes(Command);
//                    os.flush();
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                try {
//                    sh.waitFor();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            count++;
//        }
//
//        long afterMem = getAvailMemory(mContext) / (1024*1024);
//        Log.d(TAG_ClearMemoryActivityFundamental, "----------- after memory info : " + afterMem);
//        return new String("清理了 " + count + " 个进程\n空闲内存 " + afterMem + "M");
////        Snackbar.make(view, "清理了 " + count + " 个进程\n空闲内存 " + afterMem + "M", Snackbar.LENGTH_LONG)
////                .setAction("Action", null).show();
//    }