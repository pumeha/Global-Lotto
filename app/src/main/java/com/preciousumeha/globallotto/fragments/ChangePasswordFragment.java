package com.preciousumeha.globallotto.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.preciousumeha.globallotto.Constants.newToast;
import static com.preciousumeha.globallotto.Constants.ud_password;
import static com.preciousumeha.globallotto.Constants.url_change_password;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends BottomSheetDialogFragment {
    MaterialButton btn_submit;
    TextInputEditText text_old,text_new;
    TextInputLayout layout_old,layout_new;
    SharedPreferences preferences;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      // getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(STYLE_NORMAL,R.style.ThemeOverlay_Demo_BottomSheetDialog);
        View v =  inflater.inflate(R.layout.fragment_change_password, container, false);
        btn_submit = v.findViewById(R.id.fcp_submit);
        text_new = v.findViewById(R.id.fcp_new);
        text_old = v.findViewById(R.id.fcp_old);
        layout_new = v.findViewById(R.id.fcp_new_layout);
        layout_old  = v.findViewById(R.id.fcp_old_layout);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        btn_submit.setOnClickListener(view -> {
            layout_old.setErrorEnabled(false);
            layout_new.setErrorEnabled(false);
            String old_password = text_old.getText().toString();
            String new_password = text_new.getText().toString();

            if (old_password.isEmpty()){
                layout_old.setError("Enter old password");
                layout_old.setErrorEnabled(true);

            }else if (!TextUtils.equals(old_password,preferences.getString(Constants.ud_password,null))){
                layout_old.setError("Incorrect old password");
                layout_old.setErrorEnabled(true);
                return;
            }if(new_password.isEmpty()){
                layout_new.setErrorEnabled(true);
                layout_new.setError("Enter new password");
            }else if (new_password.length()<4){
                layout_new.setErrorEnabled(true);
                layout_new.setError("New password must be > 4");
                return;
            }else if (new_password.equals(old_password)){
                layout_new.setErrorEnabled(true);
                layout_new.setError("no different with the old password");
                return;
            }else{
                changeAction(new_password);
            }
        });

        return v;

    }
        public void changeAction(String new_pass){
            ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
            progressDialog.getWindow().setGravity(Gravity.BOTTOM);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, url_change_password, response -> {
            progressDialog.dismiss();
            ChangePasswordFragment.super.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("Invalid Old Password")){
                        newToast("Invalid Old Password",getContext());
                        return;
                    }else {
                        newToast("Password changed Successfully",getContext());
                        preferences.edit().putString(ud_password,new_pass).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.i(TAG, response);
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    newToast("Check Connection status and try again",getContext());
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Constants.ud_wallet, preferences.getString(Constants.ud_wallet, null));
                    params.put(Constants.ud_user_id, preferences.getString(Constants.ud_user_id, null));
                    params.put(Constants.ud_token, preferences.getString(Constants.ud_token, null));
                    params.put("new_pass",new_pass);
                    params.put("old_pass",preferences.getString(Constants.ud_password,null));
                    return params;
                }
            };
            Volley.newRequestQueue(getContext()).add(request);
        }
}
