package com.motaweron_apps.ektfaa.activities_fragments.activity_open_your_work;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;

import com.motaweron_apps.ektfaa.R;
import com.motaweron_apps.ektfaa.activities_fragments.activity_base.BaseActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_chalets_signup.ChaletsSignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_delegate_signup.DelegateSignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_family_signup.FamilySignUpActivity;
import com.motaweron_apps.ektfaa.activities_fragments.activity_user_sign_up.UserSignUpActivity;
import com.motaweron_apps.ektfaa.databinding.ActivityOpenYourWorkBinding;

public class OpenYourWorkActivity extends BaseActivity {
    private ActivityOpenYourWorkBinding binding;
    private String phone_code="";
    private String phone="";
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_open_your_work);
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

        setUpToolbar(binding.toolbar,getString(R.string.open_your_work),R.color.white,R.color.black);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req==1&&result.getResultCode()==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        binding.cardUser.setOnClickListener(v -> {
            req = 1;

            Intent intent = new Intent(this, UserSignUpActivity.class);
            intent.putExtra("phone_code", phone_code);
            intent.putExtra("phone", phone);
            launcher.launch(intent);

            /*binding.llProductiveFamilies.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.llDelegates.setBackgroundResource(0);
            binding.llChalets.setBackgroundResource(0);*/

        });
        binding.cardProductiveFamilies.setOnClickListener(v -> {
            req = 1;

            Intent intent = new Intent(this, FamilySignUpActivity.class);
            intent.putExtra("phone_code", phone_code);
            intent.putExtra("phone", phone);
            launcher.launch(intent);

            /*binding.llProductiveFamilies.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.llDelegates.setBackgroundResource(0);
            binding.llChalets.setBackgroundResource(0);*/

        });

        binding.cardDelegates.setOnClickListener(v -> {
            req = 1;
            Intent intent = new Intent(this, DelegateSignUpActivity.class);
            intent.putExtra("phone_code", phone_code);
            intent.putExtra("phone", phone);
            launcher.launch(intent);
          /*  binding.llProductiveFamilies.setBackgroundResource(0);
            binding.llDelegates.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.llChalets.setBackgroundResource(0);*/

        });
        binding.cardChalets.setOnClickListener(v -> {
            req = 1;
            Intent intent = new Intent(this, ChaletsSignUpActivity.class);
            intent.putExtra("phone_code", phone_code);
            intent.putExtra("phone", phone);
            launcher.launch(intent);

           /* binding.llProductiveFamilies.setBackgroundResource(0);
            binding.llDelegates.setBackgroundResource(0);
            binding.llChalets.setBackgroundResource(R.drawable.small_stroke_primary);*/

        });

        /*binding.btnConfirm.setOnClickListener(v -> {
            Intent intent = null;
            if (type==1){

            }else if (type==2){
                intent = new Intent(this, DelegateSignUpActivity.class);

            }else if (type==3){
               intent = new Intent(this, ChaletsSignUpActivity.class);

            }


            startActivity(intent);

        });*/
    }

}