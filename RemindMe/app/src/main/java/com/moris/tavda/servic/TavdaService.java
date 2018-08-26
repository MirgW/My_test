package com.moris.tavda.servic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TavdaService extends Service {
    final String LOG_TAG = "myLog";
    Timer timer;
    TimerTask tTask;
    long interval = 1000*60 ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy: ");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(LOG_TAG, "Service: onTaskRemoved");
        if (Build.VERSION.SDK_INT == 19) {
            Intent restartIntent = new Intent(this, getClass());
            restartIntent.setPackage(getPackageName());
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(this, 1, restartIntent, PendingIntent.FLAG_ONE_SHOT);
//            restartIntent.putExtra("RESTART");
            am.setExact(AlarmManager.RTC, System.currentTimeMillis() + 3000, pi);
        }
    }

    // TODO: 8/26/2018  Проверить не на 19 версии востановление службы по свапу преложения;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask();
        Log.d(LOG_TAG, "onStartCommand: ");
//        return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    private void myTask() {
        new Thread(new Runnable() {
            public void run() {
                int i = 1;
                Log.d(LOG_TAG, "i = " + i);
                i = i + 1;

/*                for (int i = 1; i<=5; i++) {
                    Log.d(LOG_TAG, "i = " + i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
//                stopSelf();
            }
        }).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate: ");
        timer = new Timer();
        schedule();
    }

    private void schedule() {
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            tTask = new TimerTask() {
                public void run() {
                    Log.d(LOG_TAG, "run");


                }
            };
            timer.schedule(tTask, 1000, interval);
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, "MyService onRebind");
    }

    public TavdaService() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
        Log.d(LOG_TAG, "onUnbind: ");
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d(LOG_TAG, "onBind: ");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public TavdaService getService() {
            return TavdaService.this;
        }
    }
}
