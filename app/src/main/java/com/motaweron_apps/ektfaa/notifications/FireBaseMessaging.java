package com.motaweron_apps.ektfaa.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;
import com.motaweron_apps.ektfaa.firebase_broadcast.FirebaseBroadCastReciever;
import com.motaweron_apps.ektfaa.model.FirebaseNotModel;
import com.motaweron_apps.ektfaa.model.OrderChatModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.preferences.Preferences;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.getInstance();
    private Map<String, String> map;
    private final String GROUP_KEY = "MyAppNotGroup1929Key";



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }

        manageNotification(map);

    }

    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        updateFirebaseToken(s);


    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNewNotificationVersion(Map<String, String> map) {
        Intent intentCancel = new Intent(this, FirebaseBroadCastReciever.class);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intentCancel, 0);

        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }


        String notification_type = map.get("noti_type");
        String order_id = map.get("order_id");

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setGroupSummary(true);
        builder.setGroup(GROUP_KEY);
        builder.setDeleteIntent(pendingCancelIntent);

        OrderChatModel.OrderChatMessage chatMessageModel = new Gson().fromJson(map.get("message_obj"), OrderChatModel.OrderChatMessage.class);
        UserModel.Data fromUser = new Gson().fromJson(map.get("from_user_data"), UserModel.Data.class);


        String title = "";
        String body = "";


        if (notification_type.equals("chat")) {

            title = fromUser.getName();
            if (chatMessageModel.getType().equals("message")) {
                body = chatMessageModel.getMessage();
            } else {
                body = getString(R.string.att_sent);
            }
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("to","chat");
            intent.putExtra("data", order_id);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);


            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(Uri.parse(fromUser.getLogo()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            builder.setLargeIcon(bitmap);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {

                                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();


                                if (current_class.equals("com.app.ektfaa.activities_fragments.activity_chat.ChatActivity") && order_id.equals(getRoomId())) {
                                    EventBus.getDefault().post(chatMessageModel);

                                } else {
                                    manager.createNotificationChannel(channel);
                                    manager.notify(Tags.not_tag, Tags.not_id, builder.build());
                                }


                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });


        } else if (notification_type.equals("order")) {

            builder.setContentTitle(map.get("title"));
            builder.setContentText(map.get("message"));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
            Intent intent = null;
            intent =  new Intent(this, HomeActivity.class);

           if (map.get("status").equals("client_accept_offer")||map.get("status").equals("driver_deliveried_order_to_client")||map.get("status").equals("client_rate_driver")){
               intent.putExtra("to","chat");


           }else {
               intent.putExtra("to","notification");

           }
            intent.putExtra("data",order_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(fromUser.getLogo()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            builder.setLargeIcon(bitmap);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {


                                manager.createNotificationChannel(channel);
                                manager.notify(Tags.not_tag, Tags.not_id, builder.build());
                                EventBus.getDefault().post(new FirebaseNotModel(order_id, map.get("status")));


                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });


        }


    }

    private void createOldNotificationVersion(Map<String, String> map) {
        Intent intentCancel = new Intent(this, FirebaseBroadCastReciever.class);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intentCancel, 0);

        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }


        String notification_type = map.get("noti_type");
        String order_id = map.get("order_id");

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setGroupSummary(true);
        builder.setGroup(GROUP_KEY);
        builder.setDeleteIntent(pendingCancelIntent);

        OrderChatModel.OrderChatMessage chatMessageModel = new Gson().fromJson(map.get("message_obj"), OrderChatModel.OrderChatMessage.class);
        UserModel.Data fromUser = new Gson().fromJson(map.get("from_user_data"), UserModel.Data.class);


        String title = "";
        String body = "";


        if (notification_type.equals("chat")) {

            title = fromUser.getName();
            if (chatMessageModel.getType().equals("message")) {
                body = chatMessageModel.getMessage();
            } else {
                body = getString(R.string.att_sent);
            }
            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));


            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("data", order_id);
            intent.putExtra("fromFirebase", true);
            intent.putExtra("to","chat");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);


            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(Uri.parse(fromUser.getLogo()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            builder.setLargeIcon(bitmap);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {

                                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                String current_class = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();


                                if (current_class.equals("com.app.ektfaa.activities_fragments.activity_chat.ChatActivity") && order_id.equals(getRoomId())) {
                                    EventBus.getDefault().post(chatMessageModel);

                                } else {
                                    manager.notify(Tags.not_tag, Tags.not_id, builder.build());
                                }


                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });


        } else if (notification_type.equals("order")) {
            builder.setContentTitle(map.get("title"));
            builder.setContentText(map.get("message"));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent =null;
            intent =  new Intent(this, HomeActivity.class);

            if (map.get("status").equals("client_accept_offer")||map.get("status").equals("driver_deliveried_order_to_client")||map.get("status").equals("client_rate_driver")){
                intent.putExtra("to","chat");


            }else {
                intent.putExtra("to","notification");

            }
            intent.putExtra("data", order_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(fromUser.getLogo()))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            builder.setLargeIcon(bitmap);
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {


                                manager.notify(Tags.not_tag, Tags.not_id, builder.build());
                                EventBus.getDefault().post(new FirebaseNotModel(order_id, map.get("status")));


                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });


        }

    }

    public void updateFirebaseToken(String token) {
        if (getUserData() != null) {
            try {
                Api.getService(Tags.base_url)
                        .updateFirebaseToken("Bearer " + getUserData().getData().getToken(), getUserData().getData().getId(), token, "android")
                        .enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    UserModel userModel = getUserData();
                                    if (userModel != null) {
                                        userModel.getData().setFirebase_token(token);
                                        preferences.createUpdateUserData(FireBaseMessaging.this, userModel);

                                    }

                                    Log.e("token", "updated successfully");
                                } else {
                                    try {

                                        Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                try {

                                    if (t.getMessage() != null) {
                                        Log.e("errorToken2", t.getMessage());

                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (Exception e) {

            }
        }
    }


    private UserModel getUserData() {
        return preferences.getUserData(this);

    }

    private String getRoomId() {
        return preferences.getRoom_Id(this);

    }

}
