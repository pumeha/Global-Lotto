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
import com.preciousumeha.globallotto.adapter.NotificationAdapter;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Notification;
import com.preciousumeha.globallotto.fragments.UserIdChangeNotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    SharedPreferences preferences;
    ViewModel model;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    SwipeRefreshLayout refreshLayout;
    TextView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.notify_tool);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        preferences = getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        model = ViewModelProviders.of(this).get(ViewModel.class);
        emptyView = findViewById(R.id.item_empty);
        notificationAdapter = new NotificationAdapter(this);
        refreshLayout = findViewById(R.id.notify_refresh);
        recyclerView  = findViewById(R.id.notifyRecycle);
        emptyView.setText("No record found");
        recyclerView.setHasFixedSize(true);
        refreshLayout.setOnRefreshListener(NotificationActivity.this);

        model.getAllNotification().observe(this, notificationList -> {
            ArrayList<Notification> notifications = new ArrayList<>(notificationList);
            if (notifications.size()==0){
                refresh();

                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                notificationAdapter.clearList();
                notificationAdapter.setNotification(notificationList);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
    }



    @Override
    public void onBackPressed() {
       finish();
    }

    public void refresh(){
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Checking for notification...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_notify, response -> {
            model.deleteAllNotification();
            if (response.contains("{\"status\":\"0\"}")) {
                Constants.newToast("No record found", getApplicationContext());
                progressDialog.dismiss();
            }else if (response.contains("No User Account Found")) {
                progressDialog.dismiss();
                UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                userIdChangeNotify.show(getSupportFragmentManager(),"Dialog");
            }else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aaa");
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int t = 0; t < jsonArray.length(); t++) {
                        JSONObject object = jsonArray.getJSONObject(t);
                        Notification notification = new Notification();
                        Date date = dateFormat.parse(object.getString("Date"));
                        notification.setDate(DateFormat.getDateInstance().format(date.getTime()));
                        notification.setMessage(object.getString("Message"));
                        model.insert(notification);
                        Log.i(Constants.TAG, response);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
            notificationAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
        }, error -> {
            Constants.newToast("Connection failed.Try again",getApplicationContext());
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
