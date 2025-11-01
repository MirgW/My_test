package com.moris.tavda;

import android.app.Application;
import android.content.Context;

public class ExampleApplication extends Application {
    private static Context appcontext;
    @Override public void onCreate() {
        super.onCreate();
        appcontext=getApplicationContext();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        // Normal app init code...
    }
    public static void setContext(Context cntxt) {
        appcontext = cntxt;
    }
    public static Context getContext() {
        return appcontext;}
}

