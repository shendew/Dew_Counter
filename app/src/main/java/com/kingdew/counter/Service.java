package com.kingdew.counter;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class Service extends android.app.Service {

    Runnable runnable;
    Handler handler=new Handler();
    private String EVENT_DATE_TIME = "2022-02-07 00:00:00";
    private String EVENT_NAME = "Event";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String message;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        EVENT_DATE_TIME= Paper.book().read("date");
        EVENT_NAME =Paper.book().read("event");
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        countDownStart();
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "The new Service was Stopped", Toast.LENGTH_LONG).show();
    }
    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;

                        if (Minutes == 59 && Seconds ==59){
                            Notification(Days,Hours,Minutes);
                        }

                    } else {

                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void Notification( long days, long hours,long minutes){
        message="You Have "+days +" Days and "+hours+" Hours and "+minutes+" Minutes Left";
        String title="Hurry Up For Your "+EVENT_NAME+"!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Service.this,"My Notification");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setVibrate(new long[] { 1000 , 1000 , 1000 , 1000 , 1000});

        //LED
        builder.setLights(Color.RED, 3000, 3000);

        //Ton
        builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.ic_launcher_round);

        builder.setAutoCancel(false);



        Intent intent=new Intent(Service.this,FullscreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(Service.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(Service.this);
        managerCompat.notify(1,builder.build());

    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, FullscreenActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(1, new NotificationCompat.Builder(this,
                "Top") // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

}
