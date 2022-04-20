package com.motaweron_apps.ektfaa.activities_fragments.activity_client_new_order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chat.ChatActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_client_offer_details.ClientOfferDetialsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_delegets.DelegetsActivity;
import com.motaweron_apps.ektfaa.adapter.ClientNewOrderAdapter;
import com.motaweron_apps.ektfaa.databinding.ActivityDriverComingOrderBinding;
import com.motaweron_apps.ektfaa.model.FirebaseNotModel;
import com.motaweron_apps.ektfaa.model.OrderDataModel;
import com.motaweron_apps.ektfaa.model.OrderModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
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

public class ClientNewOrderActivity extends BaseActivity {
    private ActivityDriverComingOrderBinding binding;
    private ClientNewOrderAdapter adapter;
    private List<OrderModel> list;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private Call<OrderDataModel> call;
    private OrderModel selectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_coming_order);

        binding.setLifecycleOwner(this);

        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.orders), R.color.white, R.color.black);
        list = new ArrayList<>();
        adapter = new ClientNewOrderAdapter(list, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.progBar.setVisibility(View.GONE);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK&&result.getData()!=null) {
                UserModel.Data driverModel = (UserModel.Data) result.getData().getSerializableExtra("data");
                sendAnotherDriver(driverModel);
            }else if (req == 2 && result.getResultCode() == RESULT_OK&&result.getData()!=null) {
                getOrder();

                String order_id = result.getData().getStringExtra("order_id");
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("data",order_id);
                startActivity(intent);


            }else if (req == 3 && result.getResultCode() == RESULT_OK) {
                getOrder();



            }
        });
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getOrder);
        EventBus.getDefault().register(this);

        getOrder();
    }



    private void getOrder() {
        binding.tvNoOrders.setVisibility(View.GONE);
        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        if (call != null) {
            call.cancel();
        }

        call = Api.getService(Tags.base_url).getClientNewOrder("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId());
        call.enqueue(new Callback<OrderDataModel>() {
            @Override
            public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().size() > 0) {
                        list.clear();
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

    private void sendAnotherDriver(UserModel.Data driverModel) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .chooseAnotherDriver("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), driverModel.getId(),selectedOrder.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getOrder();
                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("err", t.toString() + "__");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setItemOrder(OrderModel orderModel) {
        this.selectedOrder = orderModel;
        req = 1;
        Intent intent = new Intent(this, DelegetsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("from", "NewOrderActivity");
        launcher.launch(intent);
    }

    public void refuseOrder(OrderModel model) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOffer("Bearer " + getUserModel().getData().getToken(), model.getUser_id(), model.getDriver_id(), model.getId(), "client_refuse_order_after_offer",null)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getOrder();

                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("err", t.toString() + "__");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setOfferDetails(OrderModel orderModel) {
        req = 3;
        Intent intent = new Intent(this, ClientOfferDetialsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("data", orderModel);
        launcher.launch(intent);
    }

    public void cancelOrder(OrderModel model) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .cancelOrder("Bearer " + getUserModel().getData().getToken(), model.getUser_id(), model.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getOrder();

                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("err", t.toString() + "__");
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
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