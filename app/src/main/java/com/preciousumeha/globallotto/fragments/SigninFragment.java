package com.preciousumeha.globallotto.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.MainActivity;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.preciousumeha.globallotto.Constants.newToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends BottomSheetDialogFragment {
    TextInputEditText phone,password;
    MaterialButton btn_login,btn_forget;
    TextInputLayout phone_lay,password_lay;
    private SharedPreferences userstatus,userdetails;
    private ViewModel viewModel;
    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_signin, container, false);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        userstatus = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.preference_user_status, Context.MODE_PRIVATE);
        userdetails = getActivity().getSharedPreferences(Constants.preference_user_detail,Context.MODE_PRIVATE);

        phone = v.findViewById(R.id.fs_phone);
        password = v.findViewById(R.id.fs_password);
        btn_login = v.findViewById(R.id.fs_btn_login);
      //  btn_forget = v.findViewById(R.id.fs_btn_pass_forget);

        phone_lay = v.findViewById(R.id.fs_phone_layout);
        password_lay = v.findViewById(R.id.fs_password_layout);
        btn_login.setOnClickListener(view -> {


            phone_lay.setErrorEnabled(false);
            password_lay.setErrorEnabled(false);


            String phone_str,password_str;
            phone_str = String.valueOf(phone.getText());
            password_str = String.valueOf(password.getText());

            if (phone_str.isEmpty()) {
                phone_lay.setErrorEnabled(true);
                phone_lay.setError("Enter phone number or email");
                return;
            }if (password_str.isEmpty()) {
                password_lay.setErrorEnabled(true);
                password_lay.setError("Enter a password");
            }else if (password_str.length()<4) {
                password_lay.setErrorEnabled(true);
                password_lay.setError("Use 4 characters or more for your password");
                return;
            }else {
                phone_lay.setErrorEnabled(false);
                password_lay.setErrorEnabled(false);
                ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
                progressDialog.getWindow().setGravity(Gravity.BOTTOM);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, Constants.url_signin, response -> {
                    progressDialog.dismiss();
                    Log.i("onRespo",response);

                    try {
                     JSONObject  jsonObject = new JSONObject(response);
                        if (jsonObject.has("status")){
                            newToast("Invalid login details",getContext());
                        }else {
                            SharedPreferences.Editor editor = userdetails.edit();
                            editor.putString(Constants.ud_user_id,jsonObject.getString(Constants.ud_user_id));
                            editor.putString(Constants.ud_name,jsonObject.getString(Constants.ud_name));
                            editor.putString(Constants.ud_wallet,jsonObject.getString(Constants.ud_wallet));
                            editor.putString(Constants.ud_balance,jsonObject.getString(Constants.ud_balance));
                            editor.putString(Constants.ud_phone,jsonObject.getString(Constants.ud_phone));
                            editor.putString(Constants.ud_token,jsonObject.getString(Constants.ud_token));
                            editor.putString(Constants.ud_user,phone_str);
                            editor.putString(Constants.ud_agent,jsonObject.getString(Constants.ud_agent));
                            editor.putString(Constants.ud_password,password_str);
                            editor.apply();

                            User user = new User();
                            user.setAmt(jsonObject.getString(Constants.ud_balance));
                            user.setPhone(jsonObject.getString(Constants.ud_phone));
                            user.setWallet_id(jsonObject.getString(Constants.ud_wallet));
                            viewModel.insert(user);

                            SharedPreferences.Editor editor_1 = userstatus.edit();
                            editor_1.putInt("status",1);
                            editor_1.apply();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT",true);
                            startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                                Objects.requireNonNull(getActivity()).finishAffinity();
                            }else {
                                ActivityCompat.finishAffinity(Objects.requireNonNull(getActivity()));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    progressDialog.dismiss();
                    newToast("Connection Failed. Try Again",getContext());
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put(Constants.ud_user,phone_str);
                        params.put(Constants.ud_password,password_str);
                        return params;
                    }
                };
                queue.add(request);
            }
        });

    //    btn_forget.setOnClickListener(view -> new ForgetFragment().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"Dialog"));
        return v;
    }

}
