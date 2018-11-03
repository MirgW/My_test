package com.moris.tavda.servic;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.moris.tavda.MainActivity;
import com.moris.tavda.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

public class NotificationUtils {
    public static final String LOG_TAG = NotificationUtils.class.getName();
    public static NotificationUtils instance;
    private Context context;
    private NotificationManagerCompat manager;
    private int lastId = 0;
//    private HashMap<Integer, Notification> notifications;

    private NotificationUtils(Context context) {
        this.context = context;
//        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager = NotificationManagerCompat.from(context);
//        notifications = new HashMap<Integer, Notification>();
    }

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtils(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    Transformation transformation = new Transformation() {

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = 200;

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

    };

    public int createInfoNotification(String title, String statbar, String message, String url_not, String url_DTO) {
        //Intent notificationIntent = new Intent(context, MainActivity.class); // по клику на уведомлении откроется ...
        Intent intent = new Intent(this.context, MainActivity.class);
        intent.putExtra("url_DTO", url_DTO);
        PendingIntent notificationIntent = PendingIntent.getActivity(context, lastId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
        String imageUri = "http://www.adm-tavda.ru/" + url_not.replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", "");

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context) //для версии Android > 3.0
                .setSmallIcon(R.drawable.ic_account) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                //.setTicker(statbar) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setContentIntent(notificationIntent)
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle(title) //заголовок уведомления
               // .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setSubText(title)
               //setColor(R.color.grey_300)
                .setDefaults(NotificationCompat.DEFAULT_ALL); // звук, вибро и диодный индикатор выставляются по умолчанию
        try {
            Bitmap bitmap = new Picasso.Builder(context)
                    .build()
                    .load(imageUri)
                    .transform(transformation)
                    .error(R.drawable.ic_action_name)
                    .placeholder(R.drawable.ic_action_name)
                    .get();
            if (bitmap != null) {
                nb.setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigLargeIcon(null) //
                                .bigPicture(bitmap)
                                .setSummaryText(message));
            }
//             nb.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
             Notification notification = nb.build(); //генерируем уведомление
            manager.notify(lastId, notification); // отображаем его пользователю.
//            notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastId++;
    }

}
