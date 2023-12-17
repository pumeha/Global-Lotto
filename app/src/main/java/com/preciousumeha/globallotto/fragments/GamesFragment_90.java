package com.preciousumeha.globallotto.fragments;

import android.app.ProgressDialog;
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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.db_others.ViewModel;
import com.preciousumeha.globallotto.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.preciousumeha.globallotto.Constants.TAG;
import static com.preciousumeha.globallotto.Constants.newToast;
import static com.preciousumeha.globallotto.Constants.ud_agent;
import static com.preciousumeha.globallotto.Constants.url_agent_play;
import static com.preciousumeha.globallotto.Constants.url_play_game;

public class GamesFragment_90 extends Fragment {
    GridLayout gridView;
    LinearLayout layout;
    MaterialButton btn_play;
    int count = 0;int inputsize = 0;
    private SharedPreferences preferences;
    private ViewModel viewModel;
    int[] value = new int[91];
    private TextView[] textViews = new TextView[91];
    private ToggleButton[] toggleButtons = new ToggleButton[91];
    private Set<Integer> integerSet = new LinkedHashSet<>();
    MaterialTextView selectedtextView;
    AppCompatSpinner spinner_90;
    final String[] balance = new String[1];
    private TextInputEditText amt_stack,phone;
    private RadioGroup radioGroup;

    TextInputLayout layout_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games_90, container, false);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllUser().observe(this, users -> {
            ArrayList<User> arrayList = new ArrayList<>(users);
            balance[0] = arrayList.get(0).getAmt();
        });
        final int[] idd = new int[1];
            phone = v.findViewById(R.id.games_90_agent_phone);
        MaterialCardView cardView = v.findViewById(R.id.games_90_play_as);
        if (preferences.getString(ud_agent,null).equals("1")){
            cardView.setVisibility(View.VISIBLE);
            layout_phone = v.findViewById(R.id.games_90_agent_phone_lay);
            radioGroup = v.findViewById(R.id.game_90_radio);
            radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                switch (i){
                    case R.id.game_90_personal:
                        idd[0] = i;
                        layout_phone.setVisibility(View.GONE);break;
                    case R.id.game_90_agent:
                        idd[0] = i;
                        layout_phone.setVisibility(View.VISIBLE);break;
                }
            });
        }else {
            cardView.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        selectedtextView = v.findViewById(R.id.picker_selected_number);
        layout = v.findViewById(R.id.game_selected_linear);
        gridView = v.findViewById(R.id.picker_gridview);
        spinner_90 = v.findViewById(R.id.games_spinner_90);
        btn_play = v.findViewById(R.id.games_play);
        amt_stack = v.findViewById(R.id.games_amt);
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

                        inputsize = i +1;
                        layout.removeAllViews();
                        for (int ii = 1; ii < 91; ii++) {
                            if ( toggleButtons[ii].isChecked())
                                toggleButtons[ii].setChecked(false);
                        }
                        amt_stack.setText(null);
                        count = 0;
                        String num = "Selected numbers are: " + count;
                        selectedtextView.setText(num);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        for (int i = 1; i < 91; i++) {
            toggleButtons[i] = new ToggleButton(getContext());
            toggleButtons[i].setBackground(getResources().getDrawable(R.drawable.game_btn_checker));
            toggleButtons[i].setTextOff(String.valueOf(i));
            toggleButtons[i].setTextOn(String.valueOf(i));
            toggleButtons[i].setChecked(false);
            toggleButtons[i].setLayoutParams(params);
            toggleButtons[i].setMinHeight(0);
            toggleButtons[i].setTextColor(getResources().getColor(R.color.color_white));
            toggleButtons[i].setMinWidth(0);
            toggleButtons[i].setMinimumHeight(0);
            toggleButtons[i].setMinimumWidth(0);
            toggleButtons[i].setPadding(12, 12, 12, 12);
            gridView.addView(toggleButtons[i]);
            int ii = i;
            toggleButtons[i].setOnCheckedChangeListener((compoundButton, b) -> tt(b, toggleButtons[ii], ii));
        }

        btn_play.setOnClickListener(view -> {
            String mainBalance = balance[0];
            String amt = amt_stack.getText().toString();
            if (preferences.getString(ud_agent,null).equals("1")) {


                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.game_90_personal:
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
                                String cati,gameId,gameType;
                                String tt = value.substring(value.indexOf("[")+1, value.indexOf("]"));

                                cati = "5/90";
                                gameType = "direct";
                                gameId = String.valueOf(spinner_90.getSelectedItemId()+1);

                                placeGame(tt,cati,gameType,gameId,amt);

                            }
                            break;
                        case R.id.game_90_agent:
                            if (inputsize != count){
                                String message = "you are met to select "+ inputsize;
                                Constants.newToast(message,getContext());
                                return;
                            }if (phone.getText().toString().isEmpty()){
                            layout_phone.setErrorEnabled(true);
                            layout_phone.setError("Please enter phone number");
                        }else if (phone.getText().toString().trim().length()!=11){
                            layout_phone.setErrorEnabled(true);
                            layout_phone.setError("invalid phone number");return;
                        }else if (amt.isEmpty()){
                                newToast("Enter stack amount",getContext());
                                return;
                            }else if (Integer.parseInt(amt)>Integer.parseInt(mainBalance)){
                                newToast("Insufficent fund",getContext());
                                return;
                            }else {
                                String value = Arrays.toString(integerSet.toArray());
                                String cati,gameId,gameType;
                                String tt = value.substring(value.indexOf("[")+1, value.indexOf("]"));

                                cati = "5/90";
                                gameType = "direct";
                                gameId = String.valueOf(spinner_90.getSelectedItemId()+1);

                                placeAgentGame(tt,cati,gameType,gameId,amt,phone.getText().toString());

                            }
                            break;
                    }

            }else {
                if (inputsize != count) {
                    String message = "you are met to select " + inputsize;
                    Constants.newToast(message, getContext());
                    return;
                } else if (amt.isEmpty()) {
                    newToast("Enter stack amount", getContext());
                    return;
                } else if (Integer.parseInt(amt) > Integer.parseInt(mainBalance)) {
                    newToast("Insufficent fund", getContext());
                    return;
                } else {
                    String value = Arrays.toString(integerSet.toArray());
                    String cati, gameId, gameType;
                    String tt = value.substring(value.indexOf("[") + 1, value.indexOf("]"));

                    cati = "5/90";
                    gameType = "direct";
                    gameId = String.valueOf(spinner_90.getSelectedItemId() + 1);

                    placeGame(tt, cati, gameType, gameId, amt);

                }
            }


        });






        return v;

    }
    private void tt(boolean b, ToggleButton v, int pos){
        String value = v.getTextOn().toString();
        if (b){
            count = count +1;

            textViews[pos] = new TextView(getContext());
            textViews[pos].setBackground(getResources().getDrawable(R.drawable.round_background_checked));
            textViews[pos].setGravity(Gravity.CENTER);
            textViews[pos].setTextColor(getResources().getColor(R.color.color_white));
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
        selectedtextView.setText(num);
    }
    private void placeAgentGame(String placeNumber,String cati,String gtype,String gameId,String amt,String phone_str){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Placing game...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_agent_play, response -> {
            Log.i(TAG, response);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    // Results results = new Results();
                    // Constants.newToast("you played "+ Arrays.toString(integerSet.toArray()),getContext());
                    integerSet.clear();

                    for (int i = 1; i < 91; i++) {
                        if ( toggleButtons[i].isChecked())
                            toggleButtons[i].setChecked(false);
                    }
                    count = 0;
                    layout.removeAllViews();
                    amt_stack.setText(null);

                    newToast("Agent Successfully stacked",getContext());
                    //results.
                }else if (object.getString("status").equals("No User Account Found")) {
                    progressDialog.dismiss();
                    UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                    userIdChangeNotify.show(getFragmentManager(),"Dialog");
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
                params.put("phone",phone_str);
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
    private void placeGame(String placeNumber,String cati,String gtype,String gameId,String amt){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Placing game...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_play_game, response -> {
            Log.i(TAG, response);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                   // Results results = new Results();
                   // Constants.newToast("you played "+ Arrays.toString(integerSet.toArray()),getContext());
                    integerSet.clear();

                    for (int i = 1; i < 91; i++) {
                        if ( toggleButtons[i].isChecked())
                            toggleButtons[i].setChecked(false);
                    }
                    count = 0;
                    layout.removeAllViews();
                    amt_stack.setText(null);

                    newToast("Successfully stacked",getContext());
                    //results.
                }else if (object.getString("status").equals("No User Account Found")) {
                    progressDialog.dismiss();
                    UserIdChangeNotify userIdChangeNotify = new UserIdChangeNotify();
                    userIdChangeNotify.show(getFragmentManager(),"Dialog");
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
}
