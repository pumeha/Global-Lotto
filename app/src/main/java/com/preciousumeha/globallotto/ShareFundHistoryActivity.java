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

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.preciousumeha.globallotto.adapter.ShareFundHistoryAdapter;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.ShareFundHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShareFundHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ViewModel viewModel;
    private RecyclerView mRecyclerView;
    private ShareFundHistoryAdapter fundHistoryAdapter;
    private TextView emptyTextView;
    private SharedPreferences preferences;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_fund_history);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        preferences = getSharedPreferences(Constants.preference_user_detail, MODE_PRIVATE);
        refreshLayout = findViewById(R.id.share_hist_refresh);
        refreshLayout.setOnRefreshListener(ShareFundHistoryActivity.this);
        Toolbar toolbar = findViewById(R.id.share_hist_tool);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mRecyclerView = findViewById(R.id.shareHistoryRecycle);
        emptyTextView = findViewById(R.id.shareHistory_item_empty);
        emptyTextView.setText("No record found");
        fundHistoryAdapter = new ShareFundHistoryAdapter(this);
        viewModel.getAllShareFundHistory().observe(this, shareFundHistories -> {
            ArrayList<ShareFundHistory> histories = new ArrayList<>(shareFundHistories);
            if (histories.size() == 0) {
                emptyTextView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                refresh();
            } else {
                emptyTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                fundHistoryAdapter.clearList();
                fundHistoryAdapter.setShareHistory(histories);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(fundHistoryAdapter);
    }


    public void refresh() {

        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking for share fund history ...");
        progressDialog.show();
        refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(StringRequest.Method.POST, Constants.url_share_fund_history, response -> {
            viewModel.deleteAllShareFundHistory();
            if (response.contains("status")){
                Constants.newToast("No record found",getApplicationContext());
            }else {
                // mRecyclerView.removeAllViews();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i(Constants.TAG, String.valueOf(jsonArray.length()));
                    for (int t = 0; t < jsonArray.length(); t++) {
                        JSONObject object = jsonArray.getJSONObject(t);
                        ShareFundHistory history = new ShareFundHistory();
                        history.setWallet(object.getString("Wallet ID"));
                        history.setName(object.getString("Receiver"));
                        String[] datee = object.getString("Date").split("_");
                        history.setDate(datee[0]);
                        history.setTime(datee[1]);
                        history.setAmount(object.getString("Amount"));
                        viewModel.insert(history);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.i(Constants.TAG,response);
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
            //myActivityRestart();
            //                    getActivity().finish();
//                    startActivity(SignActivity.getIntentLanuch(getContext(),3));
            //code for restarting activity or fragment
        }, error -> {
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
            Constants.newToast("Connection Failed. Try Again",getApplicationContext());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.mdate, "0");
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
