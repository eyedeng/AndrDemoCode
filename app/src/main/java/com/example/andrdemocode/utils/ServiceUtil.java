package com.example.andrdemocode.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.andrdemocode.R;

/**
 * @author dengyan
 * @date 2024/7/20
 * @desc
 */
public class ServiceUtil {
    private static final String CHANNEL_ID = "my_serv";
    private static boolean mIsChannelCreated = false;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !mIsChannelCreated) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "前台服务", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mIsChannelCreated = true;
        }
    }

    public static Notification getForegroundNotification(Context context, String contentTitle, String contentText) {
        return getForegroundNotification(context, contentTitle, contentText, null);
    }

    public static Notification getForegroundNotification(Context context, String contentTitle, String contentText, Class<?> cls) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.btn_call_normal);
        if (cls != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, cls), PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);
        }
        return builder.build();
    }
}

