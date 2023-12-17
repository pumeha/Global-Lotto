package com.preciousumeha.globallotto;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.preciousumeha.globallotto.adapter.DepositAdapter;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Deposit;
import com.preciousumeha.globallotto.fragments.UserIdChangeNotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepositActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ViewModel viewModel;
    RecyclerView recyclerView;
    TextView textView;
    DepositAdapter adapter;
    SharedPreferences preferences;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        Toolbar toolbar = findViewById(R.id.deposit_tool);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        refreshLayout = findViewById(R.id.deposit_refresh);
        refreshLayout.setOnRefreshListener(DepositActivity.this);
        recyclerView = findViewById(R.id.depositRecycle);
        textView = findViewById(R.id.item_empty);
        preferences = getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);

        textView.setText("No record found.");
        recyclerView.setHasFixedSize(true);
        adapter = new DepositAdapter(this);
        viewModel.getAllDeposit().observe(this, depositList -> {
            ArrayList<Deposit> depositArrayList = new ArrayList<>(depositList);
            if (depositArrayList.size()==0){
                refresh();
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.clearList();
                adapter.setDeposit(depositList);
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
        progressDialog.setMessage("Checking deposit history...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_fund_history, response -> {
            Log.i(Constants.TAG, response);
            String status = null;
            try {
                JSONObject object = new JSONObject(response);
                status = object.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert status != null;
            if (status.equals("No record found")) {
                Constants.newToast("No record found", getApplicationContext());
                progressDialog.dismiss();
                refreshLayout.setRefreshing(false);
            }else if (status.equals("No User Account Found")) {
                progressDialog.dismiss();
                refreshLayout.setRefreshing(false);
                UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                userIdChangeNotify.show(getSupportFragmentManager(),"Dialog");
            }else{
                viewModel.deleteAllDeposit();
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int t = 0; t < jsonArray.length(); t++) {
                        JSONObject object = jsonArray.getJSONObject(t);
                        Deposit deposit = new Deposit();
                        deposit.setDate(object.getString("Date"));
                        deposit.setTransaction_id(object.getString("Transaction_Id"));
                        deposit.setStatus(object.getString("status"));
                        deposit.setAmount(object.getString("Amount"));

                        viewModel.insert(deposit);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);

        }, error -> {
            Constants.newToast("Connection failed. Try again", this);
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
        }) {
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

    @Override
    public void onRefresh() {
        refresh();
    }
}
