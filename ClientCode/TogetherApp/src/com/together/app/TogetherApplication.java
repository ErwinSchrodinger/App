package com.together.app;

import android.app.Application;

import com.together.app.util.AccessTokenKeeper;
import com.together.app.util.CrashHandler;

public class TogetherApplication extends Application {

    private static final String TAG = "UnionApp";

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        AccessTokenKeeper.init(this);
    }
}
