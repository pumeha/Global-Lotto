package com.preciousumeha.globallotto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.preciousumeha.globallotto.fragments.CreateAccFragment;
import com.preciousumeha.globallotto.fragments.SigninFragment;

public class SignActivity extends AppCompatActivity {
    public static final String Count = "count";
     int value ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
//        Toolbar toolbar = findViewById(R.id.toolbar_sign);
//        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        if (intent != null){
                value = intent.getIntExtra(Count,0);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (value == 1){
            SigninFragment signinFragment  = new SigninFragment();
            fragmentTransaction.replace(R.id.sign_container,signinFragment);
            fragmentTransaction.commit();
        }else if(value == 2){
            CreateAccFragment createAccFragment = new CreateAccFragment();
            fragmentTransaction.replace(R.id.sign_container,createAccFragment);
            fragmentTransaction.commit();
        }
    }
    public static Intent getIntentLanuch(Context context,int value){
        Intent intent = new Intent(context,SignActivity.class);
        intent.putExtra(Count,value);
        return intent;
    }


}
