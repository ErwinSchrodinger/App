package com.together.app.util;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.together.app.R;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final int TIME_WAIT = 3000;
    private static CrashHandler sInstance = new CrashHandler();

    private Context mContext;

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (AppConfig.CRASH_LOG_TO_FILE) {
            AppLog.writeCrashFile(ex);
        }

        NotificationManager manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, R.string.error_crash,
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(TIME_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
