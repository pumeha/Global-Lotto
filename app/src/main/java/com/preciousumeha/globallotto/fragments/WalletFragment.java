package com.preciousumeha.globallotto.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.DepositActivity;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.ShareFundHistoryActivity;
import com.preciousumeha.globallotto.WithdrawActivity;
import com.preciousumeha.globallotto.WithdrawalHistoryActivity;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class WalletFragment extends Fragment {
    MaterialTextView textView_balance;
    MaterialButton recieve_btn,share_btn,share_history_btn,deposit_btn;
    private ViewModel viewModel;
    private SharedPreferences preferences;
    public WalletFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_wallet, container, false);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        textView_balance = v.findViewById(R.id.wl_balance);
        ArrayList<String> values = new ArrayList<>(7);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                ArrayList<User> arrayList = new ArrayList<>(users);
                 String balance = "₦"+ arrayList.get(0).getAmt();
                  textView_balance.setText(balance);
            }
        });
        values.add("Fund Account");
        values.add("Withdraw Fund");
        values.add("Share Fund");
        values.add("Receive Fund");
        values.add("Deposit History");
        values.add("Share Fund History");
        values.add("Withdraw Fund History");
        AppCompatImageButton btn_refresh = v.findViewById(R.id.wallet_refresh);
        btn_refresh.setOnClickListener(view -> {
            //textView_balance.setText("");
            refresh();
        });
        walletAdapter walletAdapter = new walletAdapter(getContext(),values);
        ListView listView = v.findViewById(R.id.wallet_listview);
        listView.setAdapter(walletAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        startActivity(new Intent(getActivity(), WithdrawActivity.class));
                        break;
                    case 2:
                        ShareFundFragment shareFundFragment = new ShareFundFragment();
            shareFundFragment.show(getActivity().getSupportFragmentManager(),"Dialog");
                        break;
                    case 3:
                        new RecieveFragment().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"Dialog");
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), DepositActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), ShareFundHistoryActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), WithdrawalHistoryActivity.class));
                        break;
                }
            }
        });




        return v;
    }

    class walletAdapter extends ArrayAdapter<String>{
        public walletAdapter(@NonNull Context context, ArrayList<String> list) {
            super(context,0,list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String s = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_button,parent,false);
            }
            MaterialTextView textView = convertView.findViewById(R.id.item_button_name);
            textView.setText(s);
            return convertView;
        }
    }
    public void refresh() {

        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking balance ...");
        progressDialog.show();
        StringRequest request = new StringRequest(StringRequest.Method.POST, Constants.url_my_bal, response -> {
            if (response.contains("status")){
                Constants.newToast("No record found",getContext());
                progressDialog.dismiss();
                return;
            }else {


                try {
                    JSONObject object = new JSONObject(response);
                    String bal = object.getString(Constants.ud_balance);
                    viewModel.updateUserAcc(bal,preferences.getString(Constants.ud_wallet,null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Constants.newToast("Balance Updated",getContext());
            }

            Log.i(Constants.TAG,response);
            progressDialog.dismiss();
            //myActivityRestart();
            //                    getActivity().finish();
//                    startActivity(SignActivity.getIntentLanuch(getContext(),3));
            //code for restarting activity or fragment
        }, error -> {
            progressDialog.dismiss();
            Constants.newToast("Connection Failed. Try Again",getContext());
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
        Volley.newRequestQueue(getContext()).add(request);


    }
}
