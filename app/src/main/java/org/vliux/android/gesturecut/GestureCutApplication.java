package org.vliux.android.gesturecut;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

import org.vliux.android.gesturecut.service.GuestKeyGuardService;
import org.vliux.android.gesturecut.util.AppLog;
import org.vliux.android.gesturecut.ui.floatwindow.FloatWindowManager;
import org.vliux.android.gesturecut.util.GestureUtil;

/**
 * Created by vliux on 4/3/14.
 */
public class GestureCutApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        checkDebuggable();
        startKeyGuard();
        controlFloatWindow(true);
        GestureUtil.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        controlFloatWindow(false);
    }

    private void controlFloatWindow(final boolean isToShow) {
        if (isToShow) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FloatWindowManager.showWindow(GestureCutApplication.this);
                }
            }, 300L);
        }else{
            FloatWindowManager.closeWindow(this);
        }
    }

    private void checkDebuggable() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (null != applicationInfo) {
            boolean debug = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            AppLog.setLoggingEnabled(debug);
        }
    }

    private void startKeyGuard() {
        Intent intent = new Intent(getApplicationContext(), GuestKeyGuardService.class);
        startService(intent);
    }

    public static ComponentName sTargetComponentName;

    public static void startTargetActivity(Context context) {
        if (null != sTargetComponentName) {
            Intent intent = new Intent();
            intent.setComponent(sTargetComponentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private Handler mHandler = new Handler();
}