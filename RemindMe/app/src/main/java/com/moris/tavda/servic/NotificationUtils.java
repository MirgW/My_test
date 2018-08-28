package com.moris.tavda.servic;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.moris.tavda.MainActivity;
import com.moris.tavda.R;

import java.util.HashMap;

public class NotificationUtils {
    public static final String LOG_TAG = NotificationUtils.class.getName();
    public static NotificationUtils instance;
    private static Context context;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, Notification> notifications;

    private NotificationUtils(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
    }

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtils(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    public int createInfoNotification(String message) {
        Intent notificationIntent = new Intent(context, MainActivity.class); // по клику на уведомлении откроется ...
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
        Notification.Builder nb = new Notification.Builder(context) //для версии Android > 3.0
                .setSmallIcon(R.drawable.ic_account) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle("№ "+lastId) //заголовок уведомления
                .setDefaults(Notification.DEFAULT_ALL); // звук, вибро и диодный индикатор выставляются по умолчанию

        Notification notification = nb.getNotification(); //генерируем уведомление
        manager.notify(lastId, notification); // отображаем его пользователю.
        notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        return lastId++;
    }

}
