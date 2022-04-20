package com.motaweron_apps.ektfaa.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseFragment;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalet_my_ads.ChaletMyAdActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalets_signup.ChaletsSignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_client_new_order.ClientNewOrderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_delegate_signup.DelegateSignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_coming_order.DriverComingOrderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_current_order.DriverCurrentOrderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_driver_previous_order.DriverPreviousOrderActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_signup.FamilySignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_home.HomeActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_login.LoginActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_products.FamilyProductsActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_packages.PackagesActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_user_sign_up.UserSignUpActivity;
import com.motaweron_apps.ektfaa.adapter.SpinnerStatusAdapter;
import com.motaweron_apps.ektfaa.databinding.FragmentProfileBinding;
import com.motaweron_apps.ektfaa.model.StatusModel;
import com.motaweron_apps.ektfaa.model.StatusResponse;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.share.Common;
import com.motaweron_apps.ektfaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends BaseFragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int request;
    private SpinnerStatusAdapter adapter;
    private List<StatusModel> list;
    private boolean canChangeStatus = false;

    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (request == 1 && result.getResultCode() == Activity.RESULT_OK) {
                updateUserData();
                if (getUserModel() != null && getUserModel().getData().getUser_type().equals(BaseActivity.DRIVER)) {
                    activity.checkLocationPermission();

                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setLifecycleOwner(this);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.llLogout.setOnClickListener(view -> {
            logout();
        });


        binding.layoutUser.llEditProfile.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, UserSignUpActivity.class);
            intent.putExtra("phone", getUserModel().getData().getPhone());
            intent.putExtra("phone_code", getUserModel().getData().getPhone_code());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });

        binding.layoutDriver.llEditProfile.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, DelegateSignUpActivity.class);
            intent.putExtra("phone", getUserModel().getData().getPhone());
            intent.putExtra("phone_code", getUserModel().getData().getPhone_code());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });

        binding.layoutFamily.llEditProfile.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, FamilySignUpActivity.class);
            intent.putExtra("phone", getUserModel().getData().getPhone());
            intent.putExtra("phone_code", getUserModel().getData().getPhone_code());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });

        binding.layoutChalet.llEditProfile.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, ChaletsSignUpActivity.class);
            intent.putExtra("phone", getUserModel().getData().getPhone());
            intent.putExtra("phone_code", getUserModel().getData().getPhone_code());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });

        binding.layoutFamily.llMyProducts.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, FamilyProductsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);

        });

        binding.layoutChalet.llMyAds.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChaletMyAdActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.layoutNotSignIn.btnContinue.setOnClickListener(view -> {
            request = 1;
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            launcher.launch(intent);
        });

        binding.layoutFamily.llPackages.setOnClickListener(view -> {
            Intent intent = new Intent(activity, PackagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.layoutDriver.llPackages.setOnClickListener(view -> {
            Intent intent = new Intent(activity, PackagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.layoutChalet.llPackages.setOnClickListener(view -> {
            Intent intent = new Intent(activity, PackagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.layoutDriver.llMyOrder.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DriverComingOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.layoutDriver.llCurrentOrder.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DriverCurrentOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.layoutDriver.llPreviousOrder.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DriverPreviousOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        binding.layoutUser.llOrder.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ClientNewOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        });

        updateUserData();

    }

    private void updateUserData() {
        Log.e("ff","ff");
        if (getUserModel() == null) {
            binding.flNotSignIn.setVisibility(View.VISIBLE);
            binding.llLogout.setVisibility(View.GONE);
            binding.flUser.setVisibility(View.GONE);
            binding.flDriver.setVisibility(View.GONE);
            binding.flFamily.setVisibility(View.GONE);
            binding.flChalet.setVisibility(View.GONE);

        } else {
            binding.flNotSignIn.setVisibility(View.GONE);
            binding.llLogout.setVisibility(View.VISIBLE);
            list = new ArrayList<>();
            list.add(new StatusModel("open", getString(R.string.open)));
            list.add(new StatusModel("close", getString(R.string.close)));
            list.add(new StatusModel("busy", getString(R.string.busy)));
            adapter = new SpinnerStatusAdapter(list, activity);


            String user_type = getUserModel().getData().getUser_type();
            UserModel userModel = getUserModel();

            if (user_type.equals(BaseActivity.CLIENT)) {
                binding.flUser.setVisibility(View.VISIBLE);
                binding.layoutUser.setModel(userModel);
            } else if (user_type.equals(BaseActivity.DRIVER)) {
                binding.flDriver.setVisibility(View.VISIBLE);
                binding.layoutDriver.setModel(userModel);
                binding.layoutDriver.spinner.setAdapter(adapter);
                binding.layoutDriver.spinner.setSelection(getStatusPos());

                binding.layoutDriver.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String id = list.get(i).getId();
                        if (canChangeStatus) {
                            updateDriverStatus(id);


                        } else {
                            canChangeStatus = true;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } else if (user_type.equals(BaseActivity.FAMILY)) {
                binding.flFamily.setVisibility(View.VISIBLE);
                binding.layoutFamily.setModel(userModel);
                binding.layoutFamily.spinner.setAdapter(adapter);
                binding.layoutFamily.spinner.setSelection(getStatusPos());

                binding.layoutFamily.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String id = list.get(i).getId();

                        if (canChangeStatus) {
                            updateFamilyStatus(id);


                        } else {
                            canChangeStatus = true;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                canChangeStatus = true;

            } else if (user_type.equals(BaseActivity.CHALETS)) {
                binding.flChalet.setVisibility(View.VISIBLE);
                binding.layoutChalet.setModel(userModel);
            }
        }
    }

    private int getStatusPos() {
        int pos = 0;
        for (int index = 0; index < list.size(); index++) {
            if (getUserModel().getData().getStatus().equals(list.get(index).getId())) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    public void logout() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), getUserModel().getData().getFirebase_token())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            Log.e("code", response.body().getStatus() + "__");
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Log.e("loggedout", "logout");
                                canChangeStatus = false;
                                activity.stopUpdateLocation();
                                clearUserData();
                                updateUserData();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
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
                                Log.e("error", t.getMessage() + "__");


                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


    private void updateFamilyStatus(String status) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateFamilyStatus("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), status)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());


                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateDriverStatus(String status) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateDriverStatus("Bearer " + getUserModel().getData().getToken(), getUserModel().getData().getId(), status)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());


                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

}
