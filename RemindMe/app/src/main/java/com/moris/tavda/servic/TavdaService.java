package com.moris.tavda.servic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.moris.tavda.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import dto.RemindDTO;
import dto.ServiceDTO;

public class TavdaService extends Service {
    final String LOG_TAG = "myLog";
    Timer timer;
    TimerTask tTask;
    Parse parse;
    long interval = 1000 * 60 * 30;//30 минут
    NotificationUtils notificationUtils;
    ServiceDTO serviceDTO = new ServiceDTO();
    private SharedPreferences preferences;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy: ");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(LOG_TAG, "Service: onTaskRemoved");
        if (preferences.getBoolean("notifications_new_message", true)) {
            if (Build.VERSION.SDK_INT == 19) {
                Intent restartIntent = new Intent(this, getClass());
                restartIntent.setPackage(getPackageName());
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getService(this, 1, restartIntent, PendingIntent.FLAG_ONE_SHOT);
            restartIntent.putExtra ("RESTART",true);
            am.setExact(AlarmManager.RTC, System.currentTimeMillis() + 3000, pi);
            }
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
        notificationUtils = NotificationUtils.getInstance(getApplication());
        preferences = getSharedPreferences(getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        getDTO();
        //serviceDTO.creatMockData(); // тестовый данный
        schedule();
    }

    public void getDTO() {
        Set<String> ret = preferences.getStringSet("DTOSetKey", new HashSet<String>());
        if (ret.isEmpty()) {
//            serviceDTO.creatMockData();
        }
        for (String r : ret) {
            serviceDTO.getData_old().add(new RemindDTO("", "", "", "", "", Integer.parseInt(r)));
//            Log.i("Share", ": " + r);
        }
    }

    public void setDTO() {
        Set<String> dto = new HashSet<String>();
        for (RemindDTO remindDTO : serviceDTO.getData_new()) {
            dto.add(remindDTO.getNode().toString());
        }
        SharedPreferences.Editor e = preferences.edit();
        e.putStringSet("DTOSetKey", dto);
        e.apply();
        e.commit();
    }


//    Set<String> ret = sp.getStringSet("strSetKey", new HashSet<String>());
//	for(String r : ret) {
//        Log.i("Share", "Имя кота: " + r);
//    }

    private void schedule() {
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            tTask = new TimerTask() {
                public void run() {
                    Long i;
                    Log.d(LOG_TAG, "run");
                    i = preferences.getLong("Interval", 1800000); //30 минут
                    if (!preferences.getBoolean("notifications_new_message", true)) stopSelf();
                    if (BuildConfig.DEBUG) i = 60000l;
                    if (i != interval) {
                        tTask.cancel();
                        interval = i;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("Interval", i);
                        editor.apply();
                        editor.commit();
                        schedule();
                    }
                    parse = new Parse();
                    parse.execute(0);
                }
            };
            if (BuildConfig.DEBUG) {
                timer.schedule(tTask, 1000, interval);
            } else timer.schedule(tTask, interval, interval);
        }
    }

    class  Parse extends AsyncTask<Integer, Void, List<RemindDTO>> {
        // TODO: 8/14/2018 Прогрес бар
        @Override
        protected List<RemindDTO> doInBackground(Integer... arg) {
            Document document;
            String nodeStr;
            Integer nodeInt;
            List<RemindDTO> nodeRes;
            try {
                document = Jsoup.connect("http://www.adm-tavda.ru/node?page=" + arg[0].toString()).get();
                Elements elements = document.select(".node.story.promote");
//                String dd;
                serviceDTO.getData_new().clear();
                for (Element element : elements) {
//                    =element.select("h2.art-postheader>a").attr("href");
                    nodeStr = element.select("div").attr("id");
                    nodeInt = Integer.parseInt(nodeStr.substring(5));
                    serviceDTO.getData_new().add(new RemindDTO(element.select("h2").text(), element.select("p").text(), element.select("img").attr("src"), element.select("span.art-postdateicon").text(), element.select("h2.art-postheader>a").attr("href"), nodeInt));
                }

            } catch (IOException e) {
                e.printStackTrace();
//                data = creatMockData();
            }
            nodeRes = serviceDTO.getNew();
            if (!nodeRes.isEmpty()) {
                setDTO();
                getDTO();
            }
            return nodeRes;
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTOS) {
            super.onPostExecute(remindDTOS);
            for (final RemindDTO remindDTO : remindDTOS) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOG_TAG, "onPostExecute: " + remindDTO.getTitle());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        notificationUtils.createInfoNotification(remindDTO.getTitle(), remindDTO.getTitle(), remindDTO.getDoc_DTO(), remindDTO.getImg_DTO(),remindDTO.getUrl_DTO());
                    }
                }).start();
            }
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
        Log.d(LOG_TAG, "onUnbind: TavdaService.onUnbind");
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
