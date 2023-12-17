package com.preciousumeha.globallotto;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.preciousumeha.globallotto.workmanager.myWorker;

import java.util.concurrent.TimeUnit;

public class FlashActivity extends AppCompatActivity {
    MaterialButton btn_sign,btn_register;
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        btn_sign = findViewById(R.id.flash_signinbtn);
        btn_register = findViewById(R.id.flash_signupbtn);
        new Handler().postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences(Constants.preference_user_status, Context.MODE_PRIVATE);
            int status = preferences.getInt("status",0);
            if (status==1){
                Intent intent = new Intent(this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("EXIT",true);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    finishAffinity();
                }else {
                    ActivityCompat.finishAffinity(this);
                }
            }else {
                btn_sign.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.VISIBLE);
                btn_sign.setOnClickListener(view -> {
                    startActivity(SignActivity.getIntentLanuch(this,1));
                });
                btn_register.setOnClickListener(view -> {
                    startActivity(SignActivity.getIntentLanuch(this,2));

                });

            }
        },5000);


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(findViewById(R.id.logo),"rotation",360);
        objectAnimator.setDuration(5000);
        objectAnimator.start();

        //sending data to our workRequest(which is input), we can create put as many variables
        Data data = new Data.Builder().putString(Constants.TAG,"My Flash Activity").build();

        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();

        //subclass of WorkRequest;
//        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(myWorker.class).setInputData(data).setConstraints(constraints).build();
//        WorkManager.getInstance().enqueue(workRequest);
        //use this to stop workRequest
        //WorkManager.getInstance().cancelWorkById(workRequest.getId());,1,TimeUnit.MINUTES
        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(myWorker.class,1, TimeUnit.MINUTES).build();
       // WorkManager.getInstance().enqueue(periodicWorkRequest);

        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        String v = workInfo.getState().name() + "\n" + workInfo.getOutputData().getString(Constants.TAG);
                        Constants.newToast(v,getApplicationContext());

                    }
                });
    }
}
