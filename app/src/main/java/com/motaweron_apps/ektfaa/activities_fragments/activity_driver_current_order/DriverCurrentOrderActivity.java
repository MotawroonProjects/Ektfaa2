package com.motaweron_apps.ektfaa.activities_fragments.activity_driver_current_order;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.adapter.DriverCurrentOrderAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityDriverComingOrderBinding;
import com.motaweron_apps.ektfaa.databinding.ActivityDriverCurrentOrderBinding;
import com.motaweron_apps.ektfaa.model.FirebaseNotModel;
import com.motaweron_apps.ektfaa.model.OrderDataModel;
import com.motaweron_apps.ektfaa.model.OrderModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverCurrentOrderActivity extends BaseActivity {

    private ActivityDriverCurrentOrderBinding binding;
    private DriverCurrentOrderAdapter adapter;
    private List<OrderModel> list;
    private int req =1;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_current_order);
        binding.setLifecycleOwner(this);

        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.orders), R.color.white, R.color.black);
        list = new ArrayList<>();
        adapter = new DriverCurrentOrderAdapter(list, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.progBar.setVisibility(View.GONE);

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getOrder);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req ==1&&result.getResultCode()==RESULT_OK){
                getOrder();
            }
        });
        EventBus.getDefault().register(this);

        getOrder();
    }

    private void getOrder() {
        binding.tvNoOrders.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url).getDriverCurrentOrders("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId())
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0) {
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            } else {
                                binding.tvNoOrders.setVisibility(View.VISIBLE);
                            }

                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());

                            }

                        } catch (Exception e) {
                        }

                    }
                });

    }


    public void call(OrderModel orderModel) {
        String phone = "+"+orderModel.getUser().getPhone_code()+orderModel.getUser().getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+phone));
        startActivity(intent);
    }

    public void navigateToOrderDetails(OrderModel orderModel) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("data",orderModel.getId());
        launcher.launch(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusChanged(FirebaseNotModel model) {
        getOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}