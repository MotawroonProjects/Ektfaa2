package com.motaweron_apps.ektfaa.activities_fragments.activity_chat;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


import com.motaweron_apps.ektfaa.model.OrderChatModel;
import com.motaweron_apps.ektfaa.model.SingleMessageModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceUploadAttachment extends Service {
    private String file_uri;
    private String user_id;
    private String to_user_id;
    private String user_token;
    private String room_id;
    private String attachment_type;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        file_uri = intent.getStringExtra("file_uri");
        user_token = intent.getStringExtra("user_token");
        user_id = intent.getStringExtra("user_id");
        to_user_id = intent.getStringExtra("to_user_id");
        room_id = intent.getStringExtra("room_id");
        attachment_type = intent.getStringExtra("attachment_type");
        Log.e("file_uri", file_uri+"__");
        Log.e("user_token", user_token);
        Log.e("user_id", user_id + "_");
        Log.e("to_user_id", to_user_id + "_");
        Log.e("room_id", room_id + "__");
        Log.e("attachment_type", attachment_type);

        uploadAttachment(attachment_type);

        return START_STICKY;
    }

    private void uploadAttachment(String attachment_type) {

        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody to_user_id_part = Common.getRequestBodyText(String.valueOf(to_user_id));
        RequestBody room_id_part = Common.getRequestBodyText(String.valueOf(room_id));
        RequestBody type_part = Common.getRequestBodyText(attachment_type);

        MultipartBody.Part file_part = null;
        if (attachment_type.equals("image")) {
            file_part = Common.getMultiPartImage(this, Uri.parse(file_uri), "image");

        }
        Api.getService(Tags.base_url).sendChatImage("Bearer "+ user_token, room_id_part, user_id_part, to_user_id_part, type_part, file_part)
                .enqueue(new Callback<SingleMessageModel>() {
                    @Override
                    public void onResponse(Call<SingleMessageModel> call, Response<SingleMessageModel> response) {
                        stopSelf();
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200) {
                            OrderChatModel.OrderChatMessage model = response.body().getData();
                            EventBus.getDefault().post(model);
                        } else {


                        }
                    }

                    @Override
                    public void onFailure(Call<SingleMessageModel> call, Throwable t) {

                        try {

                            stopSelf();

                            if (t.getMessage() != null) {
                                Log.e("msg_chat_error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {

                        }
                    }
                });

        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
