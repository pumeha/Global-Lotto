package com.preciousumeha.globallotto.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccFragment extends BottomSheetDialogFragment {
    TextInputEditText name,phoneNumber,password,password_confirm;
   TextInputLayout phone_layout,name_layout,password_layout,password_confirm_layout;
    public CreateAccFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ObsoleteSdkInt")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_create_acc, container, false);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        name = v.findViewById(R.id.name_fca);
        phoneNumber = v.findViewById(R.id.phone_fca);
        password = v.findViewById(R.id.password_fca);
        password_confirm = v.findViewById(R.id.confirm_password_fca);

        name_layout = v.findViewById(R.id.name_layout_fca);
        phone_layout = v.findViewById(R.id.phone_layout_fca);
        password_layout = v.findViewById(R.id.password_layout_fca);
        password_confirm_layout = v.findViewById(R.id.password_confirm_layout_fca);

        //TODO edittext for show casing password capacity
        //todo how to remove white space in input fields
        v.findViewById(R.id.btn_register_fca).setOnClickListener(view -> {

            name_layout.setErrorEnabled(false);
            phone_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(false);
            password_confirm_layout.setErrorEnabled(false);
            String name_str = name.getText().toString().trim();
            String phoneNumber_str = String.valueOf(phoneNumber.getText());
            String password_str = String.valueOf(password.getText());
            String password_confirm_str = String.valueOf(password_confirm.getText());

            if (TextUtils.isEmpty(name_str)){
                name_layout.setError("Enter your full name");
                name_layout.setErrorEnabled(true);
                return;
            }else if (name_str.length()<4){
                name_layout.setError("Use 4 characters or more for your full name");
                name_layout.setErrorEnabled(true);
                return;
            }if (TextUtils.isEmpty(phoneNumber_str)){
                phone_layout.setError("Enter your phone number");
                phone_layout.setErrorEnabled(true);
                return;
            }else if (phoneNumber_str.length() != 11){
                phone_layout.setError("Invalid phone number");
                phone_layout.setErrorEnabled(true);
                return;
            }if (TextUtils.isEmpty(password_str)){
                password_layout.setError("Enter password");
                password_layout.setErrorEnabled(true);
                password_confirm.setText(null);
                return;
            }else if (password_str.length()<4){
                password_layout.setErrorEnabled(true);
                password_layout.setError("Use 4 characters or more for your password");
                password_confirm.setText(null);
                return;
            }else if (TextUtils.isEmpty(password_confirm_str)) {
                password_confirm_layout.setError("Confirm your password");
                password_confirm_layout.setErrorEnabled(true);
                return;
            }else if (!TextUtils.equals(password_str,password_confirm_str)){
                password_confirm.setText(null);
                password_confirm_layout.setError("Those passwords didn't match. Try again");
                password_confirm_layout.setErrorEnabled(true);
                return;
            }else {
                name_layout.setErrorEnabled(false);
                phone_layout.setErrorEnabled(false);
                password_layout.setErrorEnabled(false);
                password_confirm_layout.setErrorEnabled(false);
                ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
                progressDialog.getWindow().setGravity(Gravity.BOTTOM);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, Constants.url_create_account, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i( "onResponse: ",response);
                        progressDialog.dismiss();
                        loadFragment(response, String.valueOf(phoneNumber_str));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        newToast("Connection Failed. Try Again");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put("email","is");
                        params.put("fullname",name_str);
                        params.put("pass",password_confirm_str);
                        params.put("phone",phoneNumber_str);
                        return params;
                    }
                };

                queue.add(request);
            }

        });




        return v;
    }
    private void loadFragment(String v_code,String p_number){
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        VerifyFragment verifyFragment = VerifyFragment.newInstance(v_code, p_number);
        fragmentTransaction.replace(R.id.sign_container,verifyFragment);
        fragmentTransaction.commit();
    }
    private void newToast(String text){
        LinearLayout layout=new LinearLayout(getContext());
        layout.setBackgroundResource(R.drawable.toast);
        layout.setPadding(10,10,10,10);
        TextView tv=new TextView(getContext());
        tv.setTextColor(getResources().getColor(R.color.color_white));
        tv.setTypeface(Typeface.SANS_SERIF);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        layout.addView(tv);
        Toast toast=new Toast(getContext());
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.show();
    }
}
