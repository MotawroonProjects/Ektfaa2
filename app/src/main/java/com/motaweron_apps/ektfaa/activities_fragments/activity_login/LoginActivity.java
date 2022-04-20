package com.motaweron_apps.ektfaa.activities_fragments.activity_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_verification_code.VerificationCodeActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityLoginBinding;
import com.motaweron_apps.ektfaa.model.LoginModel;
import com.motaweron_apps.ektfaa.preferences.Preferences;
import com.motaweron_apps.ektfaa.share.Common;

import io.paperdb.Paper;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private String lang;
    private LoginModel loginModel;
    private Preferences preferences;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.login), R.color.white, R.color.black);

        preferences = Preferences.getInstance();
        binding.setLang(getLang());
        loginModel = new LoginModel();
        binding.setModel(loginModel);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req==1&&result.getResultCode()==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        binding.btnLogin.setOnClickListener(view -> {
            if (loginModel.isDataValid(this)) {
                req =1;
                Common.CloseKeyBoard(this, binding.edtPhone);
                navigateToConfirmCode();
            }
        });




    }








    private void navigateToConfirmCode() {
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("phone_code",loginModel.getPhone_code());
        intent.putExtra("phone", loginModel.getPhone());
        launcher.launch(intent);
    }


}