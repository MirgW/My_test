package com.moris.tavda;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

public class NotificationUtils {
    public static final String LOG_TAG = NotificationUtils.class.getName();
    public static NotificationUtils instance;
    private static Context context;
    private static NotificationManagerCompat manager;
    private static int lastId = 0;
    String CHANNEL_ID = "Tavda";
//    private HashMap<Integer, Notification> notifications;

    private NotificationUtils(Context context) {
        this.context = context;
//        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                CharSequence name = "Тавда";
                String description = "О Тавде";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(description);
                NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }
        }
//        notifications = new HashMap<Integer, Notification>();
    }

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            Log.d("myLog", "doWork: N-1");
            instance = new NotificationUtils(context);
            Log.d("myLog", "doWork: N-1.1");
            // TODO: 7/4/2019   падение при зппушеном приложении, при приходе уведомления.
            //instance.context = context;
        } else {
            //instance.context = context;
            Log.d("myLog", "doWork: N-2");
            instance = new NotificationUtils(context);
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
        Uri data1 = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data1);
        PendingIntent notificationIntent = PendingIntent.getActivity(context, lastId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
        String imageUri = "http://www.adm-tavda.ru/" + url_not.replaceAll("http://www.adm-tavda.ru/", "").replaceAll("http://adm-tavda.ru/", "");

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, CHANNEL_ID) //для версии Android > 3.0
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
