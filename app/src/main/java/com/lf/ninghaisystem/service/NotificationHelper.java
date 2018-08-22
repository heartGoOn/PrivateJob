package com.lf.ninghaisystem.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.lf.ninghaisystem.R;
import com.lf.ninghaisystem.activities.MyApplication;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by admin on 2017/12/14.
 */

public class NotificationHelper {

    // 声明Notification(通知)的管理者
    private static NotificationManager mNotifyMgr;
    // 声明Notification（通知）对象
    private static Notification notification;
    // 消息的唯一标示id
    public static final int mNotificationId = 001;

    @SuppressLint("NewApi")
    public static void createNotification(Class ac, String content, String type) {

        Intent resultIntent = new Intent(MyApplication.getmContext(),
                ac);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                MyApplication.getmContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // 建立所要创建的Notification的配置信息，并有notifyBuilder来保存。
        notification = new Notification.Builder(MyApplication.getmContext())
                // 触摸之后，通知立即消失
                .setAutoCancel(true)
                // 显示的时间
                .setWhen(System.currentTimeMillis())
                // 设置通知的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                // 设置状态栏显示的文本
                .setTicker("状态栏提示消息")
                // 设置通知的标题
                .setContentTitle(type)
                // 设置通知的内容
                .setContentText(content)
                // 设置声音（系统默认的）
                 .setDefaults(Notification.DEFAULT_SOUND)
                // 设置声音（自定义）
//                .setSound(
//                        Uri.parse("android.resource://org.crazyit.ui/"
//                                + R.raw.msg))
                // 设置跳转的activity
                .setContentIntent(resultPendingIntent).build();

        // 创建NotificationManager对象，并发布和管理所要创建的Notification
        mNotifyMgr = (NotificationManager) MyApplication.getmContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, notification);

    }

}
