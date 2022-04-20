package com.motaweron_apps.ektfaa.activities_fragments.activity_home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.motaweron_apps.ektfaa.model.AllAreaModel;
import com.motaweron_apps.ektfaa.model.SingleAreaModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralHomeMvvm extends AndroidViewModel {
    private MutableLiveData<List<SingleAreaModel>> onAreaSuccess;

    public GeneralHomeMvvm(@NonNull Application application) {
        super(application);
        getArea();
    }

    public MutableLiveData<List<SingleAreaModel>> getOnAreaSuccess() {
        if (onAreaSuccess == null) {
            onAreaSuccess = new MutableLiveData<>();
        }

        return onAreaSuccess;
    }

    public void getArea() {
        Api.getService(Tags.base_url).getArea().enqueue(new Callback<AllAreaModel>() {
            @Override
            public void onResponse(Call<AllAreaModel> call, Response<AllAreaModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    getOnAreaSuccess().setValue(response.body().getData());

                } else {
                    try {

                        Log.e("error", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onFailure(Call<AllAreaModel> call, Throwable t) {
                try {
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());

                    }

                } catch (Exception e) {
                }

            }
        });

    }
}
