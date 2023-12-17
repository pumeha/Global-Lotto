package com.preciousumeha.globallotto.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.CustomerCareActivity;
import com.preciousumeha.globallotto.HowToActivity;
import com.preciousumeha.globallotto.NotificationActivity;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.adapter.ProfileAdapter;
import com.preciousumeha.globallotto.classes.prf;
import com.preciousumeha.globallotto.db_others.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ViewModel viewModel;
    TextView name,phone;
    SharedPreferences preferences;
    String yy;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        name = view.findViewById(R.id.p_name);
        phone = view.findViewById(R.id.p_phone);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        String s = "Dear "+getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE).getString(Constants.ud_name,null);
        name.setText(s);
        phone.setText(getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE).getString(Constants.ud_phone,null));

        Integer[] images = {R.drawable.ic_baseline_lock_open_24,R.drawable.ic_help_black_24dp,R.drawable.ic_phone_black_24dp,
                    R.drawable.ic_notifications_active_black_24dp,R.drawable.ic_baseline_person_24,R.drawable.ic_iconmonstr_log_out};
        yy = preferences.getString(Constants.ud_agent,null);
        String[] title;
          if (yy.equals("1")){
             title = new String[]{"Change Password", "How To Play", "Customer Care", "Notification", "Return To Normal User", "Log out"};
          }else{
              title = new String[]{"Change Password", "How To Play", "Customer Care", "Notification", "Become An Agent", "Log out"};

          }

        ArrayList<prf> arrayList = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            arrayList.add(new prf(images[i],title[i]));
        }

        ListView listView = view.findViewById(R.id.profile_listview);
        ProfileAdapter adapter = new ProfileAdapter(getContext(), arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            switch (i){

                case 0:
                    new ChangePasswordFragment().show(getActivity().getSupportFragmentManager(), "Dialog");
                    break;
                case 1:
                    startActivity(new Intent(getContext(), HowToActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(getContext(), CustomerCareActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getContext(), NotificationActivity.class));
                    break;
                case 4:
                    if (yy.equals("1")){
                        refresh();
                        yy = preferences.getString(Constants.ud_agent,null);
                        adapter.notifyDataSetChanged();
                    }else
                    new AgentFragment().show(getActivity().getSupportFragmentManager(),"Dialog");
                    yy = preferences.getString(Constants.ud_agent,null);
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    new logOut().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),"Dialog");
                    break;
            }
        });

        return view;
    }


    public void refresh(){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_agent, response -> {
            Log.i(Constants.TAG, response);

            try {
                String status;
                JSONObject object = new JSONObject(response);
                status = object.getString("status");

                if (status.equals("No User Account Found")) {
                    progressDialog.dismiss();
                    UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                    userIdChangeNotify.show(getActivity().getSupportFragmentManager(),"Dialog");
                }else {
                    progressDialog.dismiss();
                    Constants.newToast("Successfully \n You are no longer\n An Agent",getContext());
                    preferences.edit().putString(Constants.ud_agent,"0").apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Constants.newToast("Connection failed",getContext());
            progressDialog.dismiss();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_agent,"0");
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);

    }
    public static class logOut extends BottomSheetDialogFragment {
        MaterialButton btn_yes,btn_no;
        ViewModel viewModel;
        @SuppressLint("ObsoleteSdkInt")
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            setStyle(STYLE_NORMAL,R.style.ThemeOverlay_Demo_BottomSheetDialog);
            viewModel = ViewModelProviders.of(this).get(ViewModel.class);
            View v =  inflater.inflate(R.layout.sample, container, false);
            btn_yes = v.findViewById(R.id.sample_yes);
            btn_no = v.findViewById(R.id.sample_no);
            btn_yes.setOnClickListener(view -> {
                getActivity().getSharedPreferences(Constants.preference_user_detail, MODE_PRIVATE).edit().clear().apply();
                getActivity().getSharedPreferences(Constants.preference_user_status, MODE_PRIVATE).edit().clear().apply();
                getActivity().getSharedPreferences(Constants.preference_permission, MODE_PRIVATE).edit().clear().apply();
                viewModel.deleteAllDeposit();
                viewModel.deleteAllResults();
                viewModel.deleteAllShareFundHistory();
                viewModel.deleteAllNotification();
                viewModel.deleteAllWithdrawal();
                viewModel.deleteAllUser();
                dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    getActivity().finishAffinity();
                }else {
                    ActivityCompat.finishAffinity(getActivity());
                }
            });
            btn_no.setOnClickListener(view -> dismiss());

            return v;
        }
    }

}
