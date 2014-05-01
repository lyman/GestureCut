package org.vliux.android.gesturecut;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

import org.vliux.android.gesturecut.biz.PhoneStateMonitor;
import org.vliux.android.gesturecut.biz.db.DbManager;
import org.vliux.android.gesturecut.service.GestureKeyGuardService;
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
        initKeyguardRelated();
        controlFloatWindow(true);
        GestureUtil.init(getApplicationContext());
        initDb();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        controlFloatWindow(false);
    }

    private void initDb(){
        DbManager.init(getApplicationContext());
    }

    private void initKeyguardRelated(){
        PhoneStateMonitor.init(this);
        GestureKeyGuardService.startKeyGuard(this);
    }

    private void controlFloatWindow(final boolean isToShow) {
        if (isToShow) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //FloatWindowManager.registerLockerStatusReceiver(GestureCutApplication.this);
                    FloatWindowManager.showWindow(GestureCutApplication.this);
                }
            }, 300L);
        }else{
            //FloatWindowManager.unregisterLockerStatusReceiver(GestureCutApplication.this);
            //FloatWindowManager.closeWindow(this);
        }
    }

    private void checkDebuggable() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (null != applicationInfo) {
            boolean debug = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            AppLog.setLoggingEnabled(debug);
        }
    }



    /*public static void startTargetActivity(Context context) {
        if (null != sTargetComponentName) {
            Intent intent = new Intent();
            intent.setComponent(sTargetComponentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }*/

    private Handler mHandler = new Handler();
}
