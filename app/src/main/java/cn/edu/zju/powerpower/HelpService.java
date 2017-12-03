package cn.edu.zju.powerpower;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class HelpService extends AccessibilityService {
    private static final String       TEXT_FORCE_STOP1 = "强行停止";
    private static final String       TEXT_FORCE_STOP2 = "结束运行";
    private static final String       TEXT_FORCE_STOP3 = "FORCE STOP";
    private static final String       TEXT_FORCE_STOP4 = "停止运行";
    private static final String       TEXT_DETERMINE1  = "确定";
    private static final String       TEXT_DETERMINE2  = "OK";
    private static final String       TEXT_DETERMINE3  = "好的";
    private static final String       TEXT_DETERMINE4  = "是";
    private static final CharSequence PACKAGE         = "com.android.settings";
    private static final CharSequence NAME_APP_DETAILS  = "com.android.settings.applications.InstalledAppDetailsTop";
    private static final CharSequence NAME_ALERT_DIALOG = "android.app.AlertDialog";

    private boolean isAppDetail;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        if(null == event || null == event.getSource()) { return; }
        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                event.getPackageName().equals(PACKAGE)){
            final CharSequence className = event.getClassName();
            if(className.equals(NAME_APP_DETAILS)){
                simulationClick(event, TEXT_FORCE_STOP1);
                simulationClick(event, TEXT_FORCE_STOP2);
                simulationClick(event, TEXT_FORCE_STOP3);
                simulationClick(event, TEXT_FORCE_STOP4);
                performGlobalAction(GLOBAL_ACTION_BACK);
                isAppDetail = true;
            }
            if(isAppDetail && className.equals(NAME_ALERT_DIALOG)){
                simulationClick(event, TEXT_DETERMINE1);
                simulationClick(event, TEXT_DETERMINE2);
                simulationClick(event, TEXT_DETERMINE3);
                simulationClick(event, TEXT_DETERMINE4);
                performGlobalAction(GLOBAL_ACTION_BACK);
                //performGlobalAction(GLOBAL_ACTION_BACK);
                isAppDetail = false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void simulationClick(AccessibilityEvent event, String text){
        List<AccessibilityNodeInfo> nodeInfoList = event.getSource().findAccessibilityNodeInfosByText(text);
        for (AccessibilityNodeInfo node : nodeInfoList) {
            if (node.isClickable() && node.isEnabled()) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() { }
}
