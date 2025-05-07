package com.Andriod.ER.com;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.Andriod.ER.R;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ForegroundService extends Service {

    private static final String CHANNEL_DEFAULT_IMPORTANCE = "0xffffffff" ;
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.logo_er_smal)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBINDGround");
        return null;
    }
    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreatGround");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }



   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommandGround");
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo_er_smal);
       // Intent notificationIntent = new Intent(this, DeviceScanActivity.class);

        Toast.makeText(this,"Creating Notification",Toast.LENGTH_SHORT).show();
       // PendingIntent pendingIntent =
           //     PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                        .setContentTitle("Energy Research")
                        .setTicker("Bluetooth")
                        .setContentText("Using Bluetooth..")
                        .setSmallIcon(R.drawable.logo_er_smal)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setOngoing(true)
                        .build();

        startForeground(1001,notification);
        return START_REDELIVER_INTENT;
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroyGround");
//        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        stopForeground(true);
    }
}
