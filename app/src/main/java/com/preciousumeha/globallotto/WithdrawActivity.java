package com.preciousumeha.globallotto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Bank;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.preciousumeha.globallotto.Constants.newToast;
import static com.preciousumeha.globallotto.Constants.ud_balance;
import static com.preciousumeha.globallotto.Constants.url_my_bank;

public class WithdrawActivity extends AppCompatActivity {
    MaterialTextView balance_view;
    AppCompatImageButton btn_refresh;
    ViewModel viewModel;
    AppCompatSpinner spinner;
    ArrayList<String> mybank;
    ArrayList<Bank> bankArrayList;
    MaterialButton btn_cash_out;
    TextInputEditText view_name,view_number,view_amount;
    SharedPreferences preferences;
    String str_amt,str_bank_id;
    TextInputLayout layout_amt;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        mybank = new ArrayList<>();
        bankArrayList = new ArrayList<>();
        mybank.add("Select Bank");
        btn_cash_out = findViewById(R.id.act_wdraw_cash_out);
        view_name = findViewById(R.id.act_wdraw_acc_name);
        view_amount = findViewById(R.id.act_wdraw_acc_amount);
        view_number = findViewById(R.id.act_wdraw_acc_number);
        layout_amt = findViewById(R.id.act_wdraw_layout_amount);
        preferences = getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        viewModel.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                ArrayList<User> arrayList = new ArrayList<>(users);
                String balance = "₦"+ arrayList.get(0).getAmt();
                balance_view.setText(balance);
            }
        });
        viewModel.getAllBank().observe(this, new Observer<List<Bank>>() {
            @Override
            public void onChanged(List<Bank> banks) {
                ArrayList<Bank> arrayList = new ArrayList<>(banks);
                if (arrayList.size()==0){
                    checkPreviousBank();
                }else {
                        bankArrayList.clear();
                        mybank.clear();
                    mybank.add("Select Bank");
                    for (Bank b:banks) {
                        mybank.add(b.getName());
                        bankArrayList.add(b);
                    }

                    setMybank(mybank);
                    setBankArrayList(bankArrayList);

                }

            }
        });
        spinner = findViewById(R.id.wdraw_bank);
         progressDialog = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_withdraw);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        balance_view = findViewById(R.id.wdraw_balance);
        btn_refresh =findViewById(R.id.wdraw_refresh);
        btn_refresh.setOnClickListener(view -> {
            refresh();
        });

        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getMybank());
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    ArrayList<Bank> banks = new ArrayList<>(getBankArrayList());
                Log.i(Constants.TAG, String.valueOf(banks.size()));

                  if (i!=0){
                      if (banks.size()==0){

                      }else {
                          int index = i-1;
                          view_name.setText(banks.get(index).getAcc_name());
                          view_number.setText( banks.get(index).getNumber());
                          str_bank_id = banks.get(index).getBank_id();


                      }
                  }else {
                      view_name.setText("");
                      view_number.setText("");
                  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_cash_out.setOnClickListener(view -> {
            str_amt = view_amount.getText().toString();
            int bal_int = Integer.parseInt(preferences.getString(Constants.ud_balance,null));
            if (spinner.getSelectedItemPosition() == 0){
                Constants.newToast("Select bank of choice or add bank",getApplicationContext());
              return;
            }if (str_amt.isEmpty()){
                layout_amt.setErrorEnabled(true);
                layout_amt.setError("Enter amount");
              return;
            }else if (Integer.parseInt(str_amt)>bal_int){
                layout_amt.setErrorEnabled(true);
                layout_amt.setError("Insufficient balance");
              return;
            }else {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, Constants.url_withdraw, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Constants.newToast("Successfully",getApplicationContext());
                        try {
                            JSONObject object = new JSONObject(response);
                    SharedPreferences userdetails = getSharedPreferences(Constants.preference_user_detail,Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = userdetails.edit();
                            editor.putString(ud_balance,object.getString(object.getString(ud_balance)));
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                }, error -> {
                    progressDialog.dismiss();
                    Constants.newToast("Connection failed.Try again",getApplicationContext());
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                        params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                        params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                        params.put("bank_id",str_bank_id);
                        params.put("amt",str_amt);
                        return params;
                    }
                };
                Volley.newRequestQueue(getApplicationContext()).add(request);
            }
        });
    }

    public ArrayList<Bank> getBankArrayList() {
        return bankArrayList;
    }

    public void setBankArrayList(ArrayList<Bank> bankArrayList) {
        this.bankArrayList = bankArrayList;
    }

    public ArrayList<String> getMybank() {
        return mybank;
    }

    public void setMybank(ArrayList<String> mybank) {
        this.mybank = mybank;
    }

    //TODO vitamin C white can make fish to eat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.withdraw_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_bank) {
            new AddBankClass().show(getSupportFragmentManager(), "Dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AddBankClass extends BottomSheetDialogFragment{
        MaterialButton btn;
        TextInputEditText acc_name,acc_number;
        AppCompatSpinner select_bank;
        ViewModel viewModel;
        String str_acc_name,str_acc_num,str_bank_id,bank_name;
        TextInputLayout layout_acc_name,layout_acc_no;
        public AddBankClass(){

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            setStyle(STYLE_NORMAL,R.style.ThemeOverlay_Demo_BottomSheetDialog);
            View v = inflater.inflate(R.layout.addbank,container,false);
            viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
                btn = v.findViewById(R.id.wdraw_btn);
                acc_name = v.findViewById(R.id.wdraw_acc_name);
                acc_number = v.findViewById(R.id.wdraw_acc_number);
                select_bank = v.findViewById(R.id.wdraw_select_bank);
                layout_acc_name = v.findViewById(R.id.wdraw_acc_name_layout);
                layout_acc_no = v.findViewById(R.id.wdraw_acc_number_layout);


            SharedPreferences preferences = getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
                btn.setOnClickListener(view -> {
                    str_acc_name = Objects.requireNonNull(acc_name.getText()).toString();
                    str_acc_num = Objects.requireNonNull(acc_number.getText()).toString();
                    str_bank_id = String.valueOf(select_bank.getId());
                    bank_name = select_bank.getSelectedItem().toString();
                    layout_acc_no.setErrorEnabled(false);
                    layout_acc_name.setErrorEnabled(false);
//                    if (select_bank.getSelectedItemPosition()==0){
//                        Constants.newToast("Select Bank",getContext());
//                    }
                    if (str_acc_name.isEmpty()){
                        layout_acc_name.setEnabled(true);
                        layout_acc_name.setError("Enter account name");
                    }else if (str_acc_name.length()<5){
                        layout_acc_name.setEnabled(true);
                        layout_acc_name.setError("Enter a valid account name");
                        return;
                    }if (str_acc_num.isEmpty()){
                        layout_acc_no.setErrorEnabled(true);
                        layout_acc_no.setError("Enter account number");
                    }else if (str_acc_num.length() != 10){
                        layout_acc_no.setErrorEnabled(true);
                        layout_acc_no.setError("Enter a valid account number");
                        return;
                    }else {
                        layout_acc_no.setErrorEnabled(false);
                        layout_acc_name.setErrorEnabled(false);
                        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
                        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Processing...");
                        progressDialog.show();
                        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_add_bank, response -> {
                            progressDialog.dismiss();
                            try {
                                JSONObject  jsonObject = new JSONObject(response);
                                Bank bank = new Bank();
                                bank.setAcc_name(str_acc_name);
                                bank.setNumber(str_acc_num);
                                bank.setBank_id(str_bank_id);
                                bank.setName(bank_name);
                                bank.setBank_id(jsonObject.getString("id"));
                                viewModel.insert(bank);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            AddBankClass.super.dismiss();
                            Constants.newToast("Successfully",getContext());
                        }, error -> {
                            progressDialog.dismiss();
                            Constants.newToast("Connection Failed. Try Again",getContext());
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                                params.put("ac_name",str_acc_name);
                                params.put("bank",bank_name);
                                params.put("acts",str_acc_num);
                                return params;
                            }
                        };
                        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(request);
                    }


                });

            return v;
        }

    }

    public void checkPreviousBank(){
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Checking for previous bank...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_my_bank, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.i(Constants.TAG, response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int t = 0; t < jsonArray.length(); t++) {
                        JSONObject object = jsonArray.getJSONObject(t);
                        Bank b = new Bank();
                        b.setName(object.getString("Bank Name"));
                        b.setAcc_name(object.getString("Account name"));
                        b.setNumber(object.getString("Account no"));
                        b.setBank_id(object.getString("id"));
                        viewModel.insert(b);

                    }
                } catch (JSONException  e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                newToast("Connection failed. Try again ",getApplicationContext());
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.ud_wallet, preferences.getString(Constants.ud_wallet, null));
                params.put(Constants.ud_user_id, preferences.getString(Constants.ud_user_id, null));
                params.put(Constants.ud_token, preferences.getString(Constants.ud_token, null));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    public void refresh() {

        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking balance ...");
        progressDialog.show();
        StringRequest request = new StringRequest(StringRequest.Method.POST, Constants.url_my_bal, response -> {
            if (response.contains("No record found")){
                Constants.newToast("No record found",getApplicationContext());
                return;
            }else {


                try {
                    JSONObject object = new JSONObject(response);
                    String bal = object.getString(Constants.ud_balance);
                        viewModel.updateUserAcc(bal,preferences.getString(Constants.ud_wallet,null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Constants.newToast("Balance Updated",getApplicationContext());
            }

            Log.i(Constants.TAG,response);
            progressDialog.dismiss();
            //myActivityRestart();
            //                    getActivity().finish();
//                    startActivity(SignActivity.getIntentLanuch(getContext(),3));
            //code for restarting activity or fragment
        }, error -> {
            progressDialog.dismiss();
            Constants.newToast("Connection Failed. Try Again",getApplicationContext());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);


    }
}