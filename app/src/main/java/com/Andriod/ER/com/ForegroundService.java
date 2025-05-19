package com.Andriod.ER.com;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.Andriod.ER.R;

/* loaded from: classes.dex */
public class ForegroundService extends Service {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "0xffffffff";

    private void startMyOwnForeground() {
        NotificationChannel notificationChannel = new NotificationChannel("com.example.simpleapp", "My Background Service", NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(-16776961);
        notificationChannel.setLockscreenVisibility(0);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        startForeground(2, new NotificationCompat.Builder(this, "com.example.simpleapp").setOngoing(true).setSmallIcon(R.drawable.logo_er_smal).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.i("ContentValues", "onBINDGround");
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        Log.i("ContentValues", "onCreatGround");
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        Log.i("ContentValues", "onDestroyGround");
        stopForeground(true);
    }
}
