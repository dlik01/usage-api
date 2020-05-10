package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Broadcast extends BroadcastReceiver {
    static public final long ALARM_TRIGGER_AT = 30000;
    static public final long ALARM_INTERVAL_UPDATE = AlarmManager.INTERVAL_HOUR;

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarmsBanner(context);
        startNotification(context);
    }

    public void startNotification (Context context){
        context.startService(new Intent(context,ServiceNotification.class));
    }

    private void setAlarmsBanner(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Broadcast.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 1, i, 0);
        alarmManager.cancel(pending);
        long t = System.currentTimeMillis() + ALARM_TRIGGER_AT;
        alarmManager.setInexactRepeating(AlarmManager.RTC, t, ALARM_INTERVAL_UPDATE, pending);
    }
}
