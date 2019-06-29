package com.moris.tavda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import dto.RemindDTO;
import dto.ServiceDTO;


public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    Data output;
    Parse parse;
    long interval = 1000 * 60 * 30;//30 минут
    NotificationUtils notificationUtils;
    ServiceDTO serviceDTO = new ServiceDTO();
    final String LOG_TAG = "myLog";
    private SharedPreferences preferences;
    Timer timer;

    @NonNull
    @Override
    public Worker.Result doWork() {
        output = new Data.Builder()
               .putString("keyA", "value1")
               .putInt("keyB", 1)
               .build();

        Log.d("myLog", "doWork: start-5");
        try {
            TimeUnit.SECONDS.sleep(10);

            Log.d(LOG_TAG, "onCreate: ");
            timer = new Timer();
            notificationUtils = NotificationUtils.getInstance(getApplicationContext());
            preferences = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
            getDTO();

            parse = new Parse();
            parse.execute(0);
/////////////////////////////////
//
//            String manasia_notification_channel = "Manasia Event Reminder";
//            //get latest event details
//            String notificationTitle = "Manasia event" ;
//            String notificationText = " at Stelea Spatarul 13, Bucuresti";
//
//            //build the notification
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(getApplicationContext(), manasia_notification_channel)
//                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
//                            .setContentTitle(notificationTitle)
//                            .setContentText(notificationText)
////                                        .setContentIntent(pendingIntent)
//                            .setAutoCancel(true)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//            //trigger the notification
//            NotificationManagerCompat notificationManager =
//                    NotificationManagerCompat.from(getApplicationContext());
//
//            //we give each notification the ID of the event it's describing,
//            //to ensure they all show up and there are no duplicates
//            notificationManager.notify(1, notificationBuilder.build());
//
//
/////////////////////////////////


        } catch (InterruptedException e) {
            e.printStackTrace();
            Result.failure(output);
        }
        Log.d("myLog", "doWork: end-5");

        return Result.success(output);
    }

///////////////////////////////
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
///////////////////////////////

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

////    private void schedule() {
//        if (tTask != null) tTask.cancel();
//        if (interval > 0) {
//            tTask = new TimerTask() {
//                public void run() {
//                    Long i;
//                    Log.d(LOG_TAG, "run");
//                    i = preferences.getLong("Interval", 1800000); //30 минут
//                    if (!preferences.getBoolean("notifications_new_message", true)) stopSelf();
//                    if (BuildConfig.DEBUG) i = 60000l;
//                    if (i != interval) {
//                        tTask.cancel();
//                        interval = i;
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putLong("Interval", i);
//                        editor.apply();
//                        editor.commit();
//                        schedule();
//                    }
////                    parse = new Parse();
////                    parse.execute(0);
//                }
//            };
//            if (BuildConfig.DEBUG) {
//                timer.schedule(tTask, 1000, interval);
//            } else timer.schedule(tTask, interval, interval);
//        }
////    }


}
