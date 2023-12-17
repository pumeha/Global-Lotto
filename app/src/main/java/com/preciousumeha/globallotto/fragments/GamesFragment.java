package com.preciousumeha.globallotto.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.PickerActivity;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.Results;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.preciousumeha.globallotto.Constants.TAG;
import static com.preciousumeha.globallotto.Constants.newToast;
import static com.preciousumeha.globallotto.Constants.url_play_game;


public class GamesFragment extends Fragment {
    private String placeNumber;
    private TextView[] textViews = new TextView[13];
    private ToggleButton btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_10,btn_11;
    int count = 0;int inputsize = 0;
    LinearLayout layout,layout_picker_11,layout_num;
    MaterialButton btn_play,select_number;
    int LAUCH_PICKER_ACTIVITY = 34;
    MaterialTextView showSelectednumbers;
    private Set<Integer> integerSet = new LinkedHashSet<>();
    AppCompatSpinner spinner,spinnerType,spinner_90,spinner_num,spinner_num_by;
    private SharedPreferences preferences;
    private TextView textView_by;
    private TextInputEditText amt_stack;
    private ViewModel viewModel;
    final String[] balance = new String[1];
    public GamesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_games, container, false);
        btn_play = v.findViewById(R.id.games_play);
        select_number = v.findViewById(R.id.select_number_90);
        showSelectednumbers = v.findViewById(R.id.game_selected_number);
        layout_picker_11 = v.findViewById(R.id.picker_11);
        layout_num = v.findViewById(R.id.games_lay_num);
        textView_by = v.findViewById(R.id.txt_by);
        amt_stack = v.findViewById(R.id.games_amt);
        btn_1 = v.findViewById(R.id.game_1);
        btn_2 = v.findViewById(R.id.game_2);
        btn_3 = v.findViewById(R.id.game_3);
        btn_4 = v.findViewById(R.id.game_4);
        btn_5 = v.findViewById(R.id.game_5);
        btn_6 = v.findViewById(R.id.game_6);
        btn_7 = v.findViewById(R.id.game_7);
        btn_8 = v.findViewById(R.id.game_8);
        btn_9 = v.findViewById(R.id.game_9);
        btn_10 = v.findViewById(R.id.game_10);
        btn_11 = v.findViewById(R.id.game_11);
        layout = v.findViewById(R.id.game_selected_linear);
        spinner = v.findViewById(R.id.games_spinner);
        spinnerType =  v.findViewById(R.id.games_select);
        spinner_90 = v.findViewById(R.id.games_spinner_90);
        spinner_num = v.findViewById(R.id.games_number);
        spinner_num_by = v.findViewById(R.id.games_number_by);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        viewModel.getAllUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                ArrayList<User> arrayList = new ArrayList<>(users);
                balance[0] = arrayList.get(0).getAmt();

            }
        });

        btn_play.setOnClickListener(view -> {
           String mainBalance = balance[0];
            String amt = amt_stack.getText().toString();

            if (inputsize != count){
                String message = "you are met to select "+ inputsize;
                Constants.newToast(message,getContext());
                return;
            }else if (amt.isEmpty()){
               newToast("Enter stack amount",getContext());
               return;
            }else if (Integer.parseInt(amt)>Integer.parseInt(mainBalance)){
                newToast("Insufficent fund",getContext());
                return;
            }else {
                String value = Arrays.toString(integerSet.toArray());
                String cati,gameId;

                String tt = value.substring(value.indexOf("[")+1, value.indexOf("]"));
                String gameCati = String.valueOf(spinnerType.getSelectedItemPosition());
                String gtype = String.valueOf(spinner.getSelectedItemPosition());
                String gameType = null;
                if (gameCati.equals("0")){
                    cati = "5/11";

                    if (gtype.equals("0")){
                        gameType = "direct";
                        gameId = spinner_num.getSelectedItem().toString();
                    }else {
                        gameType = "perm";
                        gameId = spinner_num.getSelectedItem().toString() +"/"+spinner_num_by.getSelectedItem().toString();
                    }
                }else {
                    cati = "5/90";
                    gameType = "direct";
                    gameId = String.valueOf(spinner_90.getSelectedItemId()+1);
                }
                placeGame(tt,cati,gameType,gameId,amt);

            }

            Constants.newToast("you played "+ Arrays.toString(integerSet.toArray()),getContext());
            integerSet.clear();
            uncheckbutton();
          clearSpinnerSelection();
        });

        ArrayAdapter adapter90 = ArrayAdapter.createFromResource(getContext(),R.array.games_90,android.R.layout.simple_spinner_item);
        adapter90.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_90.setAdapter(adapter90);
        spinner_90.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                    case 1:
                    case 2:
                        inputsize = 0;
                        count = 0;
                        inputsize = i +1;
                        layout.removeAllViews();
                        select_number.setText("Select Number");
                        String num = "Selected numbers are: " + count;
                        showSelectednumbers.setText(num);
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter adapterType = ArrayAdapter.createFromResource(getContext(),R.array.game_type,android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              // clearSpinnerSelection();
                switch (i){
                    case 0:
                        uncheckbutton();
                        integerSet.clear();
                        layout.removeAllViews();
                        spinner_90.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);
                        layout_picker_11.setVisibility(View.VISIBLE);
                        select_number.setVisibility(View.GONE);
                        select_number.setText("Select Number");
                        layout_num.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        integerSet.clear();
                        spinner.setVisibility(View.GONE);
                        layout_picker_11.setVisibility(View.GONE);
                        spinner_90.setVisibility(View.VISIBLE);
                        select_number.setVisibility(View.VISIBLE);
                        layout.removeAllViews();
                        layout_num.setVisibility(View.GONE);
                        uncheckbutton();
                        select_number.setText("Select Number");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        select_number.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PickerActivity.class);
            intent.putExtra("t",inputsize);
            startActivityForResult(intent,LAUCH_PICKER_ACTIVITY);
        });
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.g_cati,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_d = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_direct,android.R.layout.simple_spinner_item);
        arrayAdapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_num.setAdapter(arrayAdapter_d);
        ArrayAdapter arrayAdapter_p = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm,android.R.layout.simple_spinner_item);
        arrayAdapter_p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_2 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_2,android.R.layout.simple_spinner_item);
        arrayAdapter_p_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_3 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_3,android.R.layout.simple_spinner_item);
        arrayAdapter_p_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_4 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_4,android.R.layout.simple_spinner_item);
        arrayAdapter_p_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_5 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_5,android.R.layout.simple_spinner_item);
        arrayAdapter_p_5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_6 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_6,android.R.layout.simple_spinner_item);
        arrayAdapter_p_6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_p_7 = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_perm_7,android.R.layout.simple_spinner_item);
        arrayAdapter_p_7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_num_by.setAdapter(arrayAdapter_p_2);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        textView_by.setVisibility(View.GONE);
                        spinner_num_by.setVisibility(View.GONE);
                        spinner_num.setAdapter(arrayAdapter_d);
                        uncheckbutton();
                        //spinner_num.seth
                        break;
                    case 1:
                        textView_by.setVisibility(View.VISIBLE);
                        spinner_num_by.setVisibility(View.VISIBLE);
                        spinner_num.setAdapter(arrayAdapter_p);
                        uncheckbutton();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputsize = 0;
                if (spinner.getSelectedItemPosition()==0){
                    switch (i){
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        inputsize = i +1;
                        uncheckbutton();
                        break;
                    }
                }
                switch (i){
                    case 0:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_2);
                        break;
                    case 1:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_3);
                        break;
                    case 2:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_4);
                        break;
                    case 3:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_5);
                        break;
                    case 4:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_6);
                        break;
                    case 5:
                        uncheckbutton();
                        spinner_num_by.setAdapter(arrayAdapter_p_7);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_num_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputsize = 0;
                inputsize = Integer.parseInt(spinner_num_by.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_1.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 1;
            tt(b,btn_1,pos);
        });
        btn_2.setOnCheckedChangeListener((compoundButton, b) -> {
            int pos = 2;
            tt(b,btn_2,pos);
        });
        btn_3.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 3;
            tt(b,btn_3,pos);
        });
        btn_4.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 4;
            tt(b,btn_4,pos);
        });
        btn_5.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 5;
            tt(b,btn_5,pos);
        });
        btn_6.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 6;
            tt(b,btn_6,pos);
        });
        btn_7.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 7;
            tt(b,btn_7,pos);
        });
        btn_8.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 8;
            tt(b,btn_8,pos);
        });
        btn_9.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 9;
            tt(b,btn_9,pos);
        });
        btn_10.setOnCheckedChangeListener((compoundButton, b) -> {

            int pos = 10;
            tt(b,btn_10,pos);
        });
        btn_11.setOnCheckedChangeListener((compoundButton, b) -> {
            int pos = 11;
            tt(b,btn_11,pos);
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==LAUCH_PICKER_ACTIVITY){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                select_number.setText("Change Selected Numbers");
                layout.removeAllViews();
             //   Constants.newToast(data.getStringExtra("result"),getContext());

                String ss = data.getStringExtra("result");
                Log.i(TAG, ss);
                String tt = ss.substring(ss.indexOf("[")+1, ss.indexOf("]"));
                tt = tt.replace(" ","");
                int size = tt.split(",").length;
                TextView[] textViews1 = new TextView[size];
                for(int i =0;i<size;i++){
                    textViews1[i] = new TextView(getContext());
                    textViews1[i].setBackground(getContext().getResources().getDrawable(R.drawable.round_background_checked));
                    textViews1[i].setGravity(Gravity.CENTER);
                    textViews1[i].setTextColor(getContext().getResources().getColor(R.color.color_white));
                    textViews1[i].setHeight(40);
                    textViews1[i].setWidth(40);
                    textViews1[i].setTypeface(Typeface.DEFAULT_BOLD);
                    textViews1[i].setText(tt.split(",")[i]);
                    layout.addView(textViews1[i]);
                    integerSet.add(Integer.valueOf(tt.split(",")[i]));
                }
            count = size;
                String num = "Selected numbers are: " + count;
                showSelectednumbers.setText(num);
            }
        }
    }

    private void tt(boolean b, ToggleButton v, int pos){
        String value = v.getTextOn().toString();
        if (b){
            count = count +1;

            textViews[pos] = new TextView(getContext());
            textViews[pos].setBackground(getContext().getResources().getDrawable(R.drawable.round_background_checked));
            textViews[pos].setGravity(Gravity.CENTER);
            textViews[pos].setTextColor(getContext().getResources().getColor(R.color.color_white));
            textViews[pos].setHeight(40);
            textViews[pos].setWidth(40);
            textViews[pos].setTypeface(Typeface.DEFAULT_BOLD);
            textViews[pos].setText(v.getTextOn().toString());
            layout.addView(textViews[pos]);
                integerSet.add(Integer.valueOf(value));
        }else {

            layout.removeView(textViews[pos]);
            count = count - 1;
            integerSet.remove(Integer.valueOf(value));
        }
        String num = "Selected numbers are: " + count;
        showSelectednumbers.setText(num);
    }

    private void uncheckbutton(){
        btn_1.setChecked(false);
        btn_2.setChecked(false);
        btn_3.setChecked(false);
        btn_4.setChecked(false);
        btn_5.setChecked(false);
        btn_6.setChecked(false);
        btn_7.setChecked(false);
        btn_8.setChecked(false);
        btn_9.setChecked(false);
        btn_10.setChecked(false);
        btn_11.setChecked(false);
        layout.removeAllViews();
    }
    private void placeGame(String placeNumber,String cati,String gtype,String gameId,String amt){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Placing game...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_play_game, response -> {
            Log.i(TAG, response);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    Results results = new Results();
                    newToast("Successfully stacked",getContext());
                    //results.
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String newBalance = String.valueOf(Integer.parseInt(balance[0])-Integer.parseInt(amt));
            viewModel.updateUserAcc(newBalance,preferences.getString(Constants.ud_wallet,null));

        }, error -> {
            newToast(error.getMessage(),getContext());
            progressDialog.dismiss();

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                params.put("pno",placeNumber);
                params.put("cati",cati);
                params.put("gtype",gtype);
                params.put("gid",gameId);
                params.put("amt",amt);
                Log.i(TAG, String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }
    public void clearSpinnerSelection(){
        count = inputsize = 0;
        spinner.setSelection(0);
        spinner_num.setSelection(0);
        spinner_num_by.setSelection(0);
        spinnerType.setSelection(0);
        spinner_90.setSelection(0);
        amt_stack.setText(null);
        showSelectednumbers.setText("Selected numbers are:");
        select_number.setText("Select Number");
    }
}
