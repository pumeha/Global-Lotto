package com.preciousumeha.globallotto.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.OtpView;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.MainActivity;
import com.preciousumeha.globallotto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static com.preciousumeha.globallotto.Constants.newToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyFragment extends Fragment {
    private static String verification_code,phonenumber;
    private OtpView otpView;
    private SharedPreferences userstatus,userdetail_preference;
    //todo how to add a toolbar in a fragment.
    //todo how to add slide in and out in a fragment transition
    public VerifyFragment() {
        // Required empty public constructor
    }

    static VerifyFragment newInstance(String value, String phone){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.verifycode,value);
        bundle.putString(Constants.phone,phone);
        VerifyFragment fragment = new  VerifyFragment();
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            verification_code = bundle.getString(Constants.verifycode);
            phonenumber = bundle.getString(Constants.phone);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_verify, container, false);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        userstatus = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.preference_user_status, Context.MODE_PRIVATE);
        userdetail_preference = getActivity().getSharedPreferences(Constants.preference_user_detail,Context.MODE_PRIVATE);
        TextView detail = v.findViewById(R.id.detail_fv);
      String info = "We have sent an SMS with a verification code to your phone " + phonenumber;
        detail.setText(info);
        try {
            JSONObject jsonObject = new JSONObject(verification_code);
            verification_code = jsonObject.getString("code");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Constants.newToast(verification_code,getContext());
        otpView = v.findViewById(R.id.otp_view);
        otpView.requestFocus();
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//SHOW Keyboard
        otpView.setOtpCompletionListener(otp -> {

            if (otp.equals(verification_code)){
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Verifying...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, Constants.url_verify, new Response.Listener<String>() {
                    @SuppressLint("ObsoleteSdkInt")
                    @Override
                    public void onResponse(String response) {
                        newToast(response,getContext());
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = userstatus.edit();
                        editor.putInt("status",1);
                        editor.apply();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            SharedPreferences.Editor editor1 = userdetail_preference.edit();
                            editor1.putString(Constants.ud_user_id,jsonObject.getString(Constants.ud_user_id));
                            editor1.putString(Constants.ud_name,jsonObject.getString(Constants.ud_name));
                            editor1.putString(Constants.ud_wallet,jsonObject.getString(Constants.ud_wallet));
                            editor1.putString(Constants.ud_balance,jsonObject.getString(Constants.ud_balance));
                            editor1.putString(Constants.ud_phone,jsonObject.getString(Constants.ud_phone));
                            editor1.putString(Constants.ud_token,jsonObject.getString(Constants.ud_token));
                            editor1.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        startActivity(new Intent(getContext(), MainActivity.class));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                            Objects.requireNonNull(getActivity()).finishAffinity();
                        }else {
                            ActivityCompat.finishAffinity(Objects.requireNonNull(getActivity()));
                        }
                    }
                }, error -> {
                    progressDialog.dismiss();
                    newToast("Connection Failed. Try Again",getContext());
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put(Constants.phone,phonenumber);
                        params.put(Constants.verifycode,otp);
                        return params;
                    }
                };
                queue.add(request);
            }else {
                otpView.setText(null);
                otpView.requestFocus();
                newToast("Invalid Verification Code",getContext());
            }

        });

        return v;
    }

}
