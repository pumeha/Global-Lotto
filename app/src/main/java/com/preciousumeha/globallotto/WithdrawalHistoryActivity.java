package com.preciousumeha.globallotto;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.preciousumeha.globallotto.adapter.WithdrawalAdapter;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Withdrawal;
import com.preciousumeha.globallotto.fragments.UserIdChangeNotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WithdrawalHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ViewModel viewModel;
    private WithdrawalAdapter adapter;
    private SharedPreferences preferences;
    RecyclerView recyclerView;
    TextView textView;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_history);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        Toolbar toolbar = findViewById(R.id.withdraw_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        adapter = new WithdrawalAdapter(this);
        recyclerView = findViewById(R.id.withdraw_recycler);
        refreshLayout = findViewById(R.id.withdraw_refresh);
        refreshLayout.setOnRefreshListener(WithdrawalHistoryActivity.this);
        recyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.withdraw_empty);
        preferences = getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        viewModel.getAllWithdrawal().observe(this, withdrawals -> {
            ArrayList<Withdrawal> arrayList = new ArrayList<>(withdrawals);
            if (arrayList.size()==0){
                refresh();
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.clearList();
                adapter.setWithdrawal(withdrawals);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void refresh(){
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Checking for withdrawal history...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_withdraw_history, response -> {
            Log.i(Constants.TAG, response);



                if (response.contains("No record found")) {
                    Constants.newToast("No record found", getApplicationContext());
                    progressDialog.dismiss();

                }else if (response.contains("No User Account Found")) {
                    progressDialog.dismiss();
                    UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                    userIdChangeNotify.show(getSupportFragmentManager(),"Dialog");
                }else {
                    viewModel.deleteAllWithdrawal();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aaa");
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int t = 0; t < jsonArray.length(); t++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(t);
                            Withdrawal withdrawal = new Withdrawal();
                            Date date = dateFormat.parse(jsonObject.getString("Date"));
                            withdrawal.setDate(String.valueOf(date));
                            withdrawal.setAcc_name(jsonObject.getString("Account Name"));
                            withdrawal.setAcc_no(jsonObject.getString("Account no"));
                            withdrawal.setStatus(jsonObject.getString("Status"));
                            withdrawal.setBank(jsonObject.getString("Bank"));
                            withdrawal.setAmount(jsonObject.getString("Amount"));
                            viewModel.insert(withdrawal);
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }

                }



            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
            Constants.newToast("Successfully",getApplicationContext());
        }, error -> {
            Constants.newToast("Connection failed",getApplicationContext());
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }

    @Override
    public void onRefresh() {
        refresh();
    }
}