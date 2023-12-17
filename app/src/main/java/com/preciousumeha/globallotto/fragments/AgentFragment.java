package com.preciousumeha.globallotto.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgentFragment extends BottomSheetDialogFragment {
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.fragment_agent, container, false);
        TextView textView = v.findViewById(R.id.agent_name);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        String tt = "Dear "+preferences.getString(Constants.ud_name,null);
        textView.setText(tt);
        MaterialButton button = v.findViewById(R.id.agent_send_btn);
        button.setOnClickListener(view -> {
            refresh();
        });

       return v;
    }
    public void refresh(){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_agent, response -> {
            Log.i(Constants.TAG, response);

            try {
                String status;
                JSONObject object = new JSONObject(response);
                status = object.getString("status");

                if (status.equals("No User Account Found")) {
                    progressDialog.dismiss();
                    UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                    userIdChangeNotify.show(getActivity().getSupportFragmentManager(),"Dialog");

                }else {
                    progressDialog.dismiss();
                    Constants.newToast("Successfully \n You are now\n An Agent",getContext());
                    preferences.edit().putString(Constants.ud_agent,"1").apply();
                    AgentFragment.super.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Constants.newToast("Connection failed",getContext());
            progressDialog.dismiss();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_agent,"1");
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);

    }
}
