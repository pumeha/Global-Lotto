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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.adapter.ResultsAdapter;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.preciousumeha.globallotto.Constants.TAG;
import static com.preciousumeha.globallotto.Constants.url_check_game;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SharedPreferences preferences;
    private ViewModel viewModel;
    private RecyclerView mRecyclerView;
    private ResultsAdapter adapter;
    private  TextView emptyTextView;
    private SwipeRefreshLayout refreshLayout;

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
       View view = inflater.inflate(R.layout.fragment_results, container, false);
        mRecyclerView = view.findViewById(R.id.resultsFragmentRecycler);
        emptyTextView = view.findViewById(R.id.item_empty);
        mRecyclerView.setHasFixedSize(true);
        refreshLayout = view.findViewById(R.id.result_refresh);
        adapter = new ResultsAdapter(getContext());
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        refreshLayout.setOnRefreshListener(ResultsFragment.this);

        viewModel.getAllResults().observe((LifecycleOwner) getContext(), results -> {
            ArrayList<Results> results2 = new ArrayList<>(results);
            if (results2.size()==0) {
                checkPreviousGames();
                emptyTextView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);

            }else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
                adapter.clearList();
                adapter.setResults(results);
                Log.i(TAG, Arrays.toString(results2.toArray()));

            }
            //refreshLayout.setRefreshing(false);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
       mRecyclerView.setAdapter(adapter);
        return view;
    }
    public void checkPreviousGames(){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        refreshLayout.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, url_check_game, response -> {

            if (response.equals("0")){
                progressDialog.dismiss();
                Constants.newToast("No previous games found",getContext());
                refreshLayout.setRefreshing(false);
                Log.i(TAG, response);
                emptyTextView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                return;
            }else if (response.contains("No User Account Found")) {
                progressDialog.dismiss();
                UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                userIdChangeNotify.show(getFragmentManager(),"Dialog");
            }else{
                viewModel.deleteAllResults();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aaa");
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0;i<=array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        Results r = new Results();
                        //Date date = dateFormat.parse(object.getString("Date"));
                       // r.setGame_date(DateFormat.getDateInstance().format(date.getTime()));
                        r.setGame_date(object.getString("Date"));
                        r.setGame_id(object.getString("gsn"));
                        r.setGame_status(object.getString("Status"));
                        r.setGame_result(object.getString("Winning no"));
                        r.setGame_type(object.getString("Game type"));
                        r.setName(object.getString("Game cati"));
                        r.setGame_number(object.getString("Game number"));
                        r.setPlayed_no(object.getString("Played Games"));
                        r.setGame_stack(object.getString("Amount"));
                        viewModel.insert(r);
                    }
                    Log.i(TAG, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            progressDialog.dismiss();
            Log.i(TAG, response);
            refreshLayout.setRefreshing(false);
        }, error -> {
            Constants.newToast("Connection failed. Try again",getContext());
            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
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
        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onRefresh() {
        checkPreviousGames();

        refreshLayout.setRefreshing(true);
    }
}
