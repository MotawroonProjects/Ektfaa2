package com.motaweron_apps.ektfaa.firebase_broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.motaweron_apps.ektfaa.tags.Tags;

public class FirebaseBroadCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ddd","sssss");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager!=null){
            manager.cancel(Tags.not_tag,Tags.not_id);
        }
    }
}
