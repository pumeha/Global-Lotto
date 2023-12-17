package com.preciousumeha.globallotto.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFundFragment extends BottomSheetDialogFragment {
    private static int BARCODE_READER_ACTIVITY_REQUEST = 12;
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus,sharefund_preference;
    MaterialTextView balance_textview;
    MaterialButton scan_btn,share_btn;
    TextInputEditText inputWallet,inputAmount;
    TextInputLayout wallet_layout,amount_layout;
    final String[] balance = new String[1];
    private ViewModel viewModel;
    public ShareFundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL,R.style.ThemeOverlay_Demo_BottomSheetDialog);
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_share_fund, container, false);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        balance_textview = v.findViewById(R.id.fsf_balance);
        sharefund_preference = getActivity().getSharedPreferences(Constants.preference_user_detail,Context.MODE_PRIVATE);
        String user_id_str = sharefund_preference.getString(Constants.ud_user_id,null);
        String user_wallet = sharefund_preference.getString(Constants.ud_wallet,null);
        viewModel.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                ArrayList<User> arrayList = new ArrayList<>(users);
                balance[0] =  arrayList.get(0).getAmt();
               String bal_str = "Balance : ₦ " + balance[0];
                balance_textview.setText(bal_str);
            }
        });
        RequestQueue queue =  Volley.newRequestQueue(getContext());
        permissionStatus = getActivity().getSharedPreferences(Constants.preference_permission, Context.MODE_PRIVATE);
        balance_textview = v.findViewById(R.id.fsf_balance);

        String user_token = sharefund_preference.getString(Constants.ud_token,null);
        scan_btn = v.findViewById(R.id.fsf_scan_btn);
        share_btn = v.findViewById(R.id.fsf_send_btn);
        inputAmount = v.findViewById(R.id.fsf_amount);
        inputWallet = v.findViewById(R.id.fsf_wallet_id);
        wallet_layout = v.findViewById(R.id.fsf_wallet_id_layout);
        amount_layout = v.findViewById(R.id.fsf_amount_layout);

        inputWallet.setCustomSelectionActionModeCallback(new Constants.stopCopy());
        inputAmount.setCustomSelectionActionModeCallback(new Constants.stopCopy());

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPermission();
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wallet_layout.setErrorEnabled(false);
                amount_layout.setErrorEnabled(false);
                String wallet_str = inputWallet.getText().toString().trim();
            String amount_str = inputAmount.getText().toString().trim();
                int bal_int = Integer.parseInt(balance[0]);

           if (TextUtils.isEmpty(wallet_str)){
                wallet_layout.setErrorEnabled(true);
                wallet_layout.setError("Enter wallet id");
                return;
            }else if (wallet_str.length() != 9){
               wallet_layout.setErrorEnabled(true);
               wallet_layout.setError("Invalid wallet id");
               return;
            }else if (wallet_str.equals(user_wallet)){
               wallet_layout.setErrorEnabled(true);
               wallet_layout.setError("that's your wallet");
           }
           if (TextUtils.isEmpty(amount_str)){
                    amount_layout.setErrorEnabled(true);
                    amount_layout.setError("Enter amount");
                    return;
                }else if (Integer.parseInt(amount_str)>bal_int){
                    amount_layout.setErrorEnabled(true);
                    amount_layout.setError("Insufficient balance");
                    return;
                }
           else {
               int amt_int = Integer.parseInt(amount_str);
               wallet_layout.setErrorEnabled(false);
               amount_layout.setErrorEnabled(false);
               ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
               progressDialog.getWindow().setGravity(Gravity.BOTTOM);
               progressDialog.setCancelable(false);
               progressDialog.setMessage("Processing...");
               progressDialog.show();
               StringRequest request = new StringRequest(Request.Method.POST, Constants.url_share_funds, response -> {
                   Log.i("onRespo",response);

                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       String status = jsonObject.getString(Constants.status);
                       if (status.equals("0")){
                           progressDialog.dismiss();
                           Constants.newToast("Invalid input",getContext());
                       }else if (status.equals("2")){
                           progressDialog.dismiss();
                           Constants.newToast("Insufficient Balance",getContext());
                       }else if (status.equals("3")){
                           progressDialog.dismiss();
                           Constants.newToast("No wallet found",getContext());
                       }else if (status.equals("No User Account Found")) {
                           progressDialog.dismiss();
                           UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                           userIdChangeNotify.show(getActivity().getSupportFragmentManager(),"Dialog");
                       }else {
                           String new_bal = jsonObject.getString("new_bal");
                           Constants.newToast("Share Fund Sucessfully",getContext());
                          // int new_bl = bal_int-amt_int;
                           SharedPreferences.Editor preferences =  sharefund_preference.edit();
                           preferences.putString(Constants.ud_balance,new_bal);
                           preferences.apply();
                           progressDialog.dismiss();
                           ShareFundFragment.super.dismiss();
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       progressDialog.dismiss();
                       Constants.newToast("Connection Failed. Try Again",getContext());
                   }
               }){
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put(Constants.amount,amount_str);
                        params.put(Constants.ud_wallet,wallet_str);
                        params.put(Constants.ud_user_id,user_id_str);
                        params.put(Constants.ud_token,user_token);

                        return params;
                   }
               };
               queue.add(request);
           }
            }
        });
        return v;
    }
    private void startBarcode(){
       Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getContext(), true,false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }
    private void MyPermission(){
        //getActivity();
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        // to check whether the permission is granted if not ** is carried out
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // ** is when the user check deny let my dialog info it why it needs it
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(com.notbytes.barcode_reader.R.string.grant_permission));
                builder.setMessage(getString(com.notbytes.barcode_reader.R.string.permission_camera_rationale));
                builder.setPositiveButton(com.notbytes.barcode_reader.R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                builder.show();
            }else if (permissionStatus.getBoolean(Manifest.permission.CAMERA, false)){
                //it carry this out when user check don't ask again and deny

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(com.notbytes.barcode_reader.R.string.grant_permission));
                builder.setMessage(getString(com.notbytes.barcode_reader.R.string.no_camera_permission));
                builder.setPositiveButton(com.notbytes.barcode_reader.R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                });
                builder.setNegativeButton(com.notbytes.barcode_reader.R.string.cancel, (dialog, which) -> dialog.cancel());
                builder.show();
            }else {
                // it pop the permission dialog at first
                requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CAMERA, true);
            editor.apply();
        }else {
            // since permission is granted call the barcode reader.
            startBarcode();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
           Toast.makeText(getContext(), "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {

            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            inputWallet.setText(barcode.rawValue);
         //  Constants.newToast(barcode.rawValue,getContext());

        }
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                startBarcode();
            }
        }   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted){
                startBarcode();
            }else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(com.notbytes.barcode_reader.R.string.grant_permission));
                builder.setMessage(getString(com.notbytes.barcode_reader.R.string.permission_camera_rationale));
                builder.setPositiveButton(com.notbytes.barcode_reader.R.string.grant, (dialog, which) -> {
                    dialog.cancel();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                });
                builder.setNegativeButton(com.notbytes.barcode_reader.R.string.cancel, (dialog, which) -> dialog.cancel());
                builder.show();
            }else {
                Constants.newToast("Permission Denied",getContext());
            }
        }
    }


}
