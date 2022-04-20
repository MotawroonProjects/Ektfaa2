package com.motaweron_apps.ektfaa.activities_fragments.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_open_your_work.OpenYourWorkActivity;
import com.motaweron_apps.ektfaa.model.UserModel;
import com.motaweron_apps.ektfaa.remote.Api;
import com.motaweron_apps.ektfaa.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.databinding.ActivityVerificationCodeBinding;
import com.motaweron_apps.ektfaa.share.Common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends BaseActivity {
    private ActivityVerificationCodeBinding binding;
    private String phone_code;
    private String phone;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private boolean canSend = false;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        binding.setLifecycleOwner(this);

        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");

        }
    }

    private void initView() {

        setUpToolbar(binding.toolbar, getString(R.string.confirmation_code), R.color.white, R.color.black);

        mAuth = FirebaseAuth.getInstance();
        String phone = "+" + phone_code + this.phone;
        binding.setPhone(phone);
        binding.tvResend.setOnClickListener(view -> {
            if (canSend) {
                sendSmsCode();
            }
        });
        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
            if (!code.isEmpty()) {
                binding.edtCode.setError(null);
                Common.CloseKeyBoard(this, binding.edtCode);
                checkValidCode(code);
            } else {
                binding.edtCode.setError(getString(R.string.field_required));
            }

        });
        sendSmsCode();
        // login();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req == 1 && result.getResultCode() == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }

    private void sendSmsCode() {

        startTimer();

        mAuth.setLanguageCode(getLang());
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                binding.edtCode.setText(smsCode);
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                VerificationCodeActivity.this.verificationId = verification_id;
                VerificationCodeActivity.this.forceResendingToken = forceResendingToken;
                Log.e("verificationId", verificationId);
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("dkdkdk", e.toString());
                if (e.getMessage() != null) {
                    //   Common.CreateDialogAlert(VerificationCodeActivity.this, e.getMessage());
                } else {
                    // Common.CreateDialogAlert(VerificationCodeActivity.this, getString(R.string.failed));

                }
            }
        };
        String user_phone = "+" + phone_code + phone;
        Log.e("phone", user_phone);
        PhoneAuthOptions options;
        options = PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .setPhoneNumber(user_phone)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setForceResendingToken(forceResendingToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void startTimer() {
        canSend = false;
        binding.tvResend.setEnabled(false);
        timer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.tvCounter.setText(time);
            }

            @Override
            public void onFinish() {
                canSend = true;
                binding.tvCounter.setText("00:00");
                binding.tvResend.setEnabled(true);
                binding.tvResend.setVisibility(View.VISIBLE);
            }
        };
        timer.start();
    }


    private void checkValidCode(String code) {

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        login();
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                    // Common.CreateDialogAlert(this, e.getMessage());
                } else {
                    //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }

    private void login() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(phone_code, phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setUserModel(response.body());
                                setResult(RESULT_OK);
                                finish();

                            } else if (response.body().getStatus() == 401) {
                                navigateToChooseSignUpActivity();
                            } else if (response.body().getStatus() == 409) {
                                Toast.makeText(VerificationCodeActivity.this, R.string.user_block, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                //    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToChooseSignUpActivity() {
        req = 1;
        Intent intent = new Intent(this, OpenYourWorkActivity.class);
        intent.putExtra("phone_code", phone_code);
        intent.putExtra("phone", phone);
        launcher.launch(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
