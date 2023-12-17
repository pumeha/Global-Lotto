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

public class GamesFragmeent_11 extends Fragment  {
   private AppCompatSpinner spinner_game_type, spinner_game_number_direct,spinner_game_number_perm, spinner_game_number_perm_by;
    private SharedPreferences preferences;
    private ViewModel viewModel;
    private LinearLayout layout,layout_perm;
    public static final String cati = "5/11";
    private TextView txt_by;
    MaterialButton btn_play;
    private TextInputEditText amt_stack,phone;
    private TextView[] textViews = new TextView[13];
    MaterialTextView showSelectednumbers;
    private ToggleButton btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_10,btn_11;
    final String[] balance = new String[1];
    int count = 0;int inputsize = 0;
    private Set<Integer> integerSet = new LinkedHashSet<>();
    private RadioGroup radioGroup;
    private TextInputLayout layout_phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_games_11, container, false);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail,MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        phone = v.findViewById(R.id.games_11_agent_phone);
        viewModel.getAllUser().observe(this, users -> {
            ArrayList<User> arrayList = new ArrayList<>(users);
            balance[0] = arrayList.get(0).getAmt();

        });


        MaterialCardView cardView = v.findViewById(R.id.games_11_play_as);
        if (preferences.getString(ud_agent,null).equals("1")){
            cardView.setVisibility(View.VISIBLE);
            layout_phone = v.findViewById(R.id.games_11_agent_phone_lay);
            radioGroup = v.findViewById(R.id.game_11_radio);

            radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                switch (i){
                    case R.id.game_11_personal:
                        layout_phone.setVisibility(View.GONE);break;
                    case R.id.game_11_agent:
                        layout_phone.setVisibility(View.VISIBLE);break;
                }
            });

        }else {
            cardView.setVisibility(View.GONE);
        }
        txt_by = v.findViewById(R.id.txt_by);
        spinner_game_type = v.findViewById(R.id.games_spinner_type);
        spinner_game_number_direct = v.findViewById(R.id.games_number_direct);
        spinner_game_number_perm = v.findViewById(R.id.games_number_perm);
        spinner_game_number_perm_by =v.findViewById(R.id.games_number_perm_by);
        showSelectednumbers = v.findViewById(R.id.game_selected_number);
        layout_perm = v.findViewById(R.id.games_number_perm_layout);
        amt_stack = v.findViewById(R.id.games_amt);
        btn_play = v.findViewById(R.id.games_play);
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
        {
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
        });}
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.g_cati,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter arrayAdapter_d = ArrayAdapter.createFromResource(getContext(),R.array.g_cati_direct,android.R.layout.simple_spinner_item);
        arrayAdapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        spinner_game_number_perm.setAdapter(arrayAdapter_p);
        spinner_game_type.setAdapter(arrayAdapter);
        spinner_game_number_direct.setAdapter(arrayAdapter_d);
        spinner_game_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputsize = 0;
                switch (i){
                    case 0:
                      //  spinner_game_number_direct.setSelection(0);
                     //   spinner_game_number_direct.setVisibility(View.VISIBLE);
                        spinner_game_number_perm.setVisibility(View.GONE);
                        spinner_game_number_perm_by.setVisibility(View.GONE);
                        layout_perm.setVisibility(View.GONE);
                        txt_by.setVisibility(View.GONE);
                        uncheckbutton();
                        break;
                    case 1:
                        spinner_game_number_perm.setSelection(0);
                        spinner_game_number_perm_by.setSelection(0);
                        spinner_game_number_perm.setVisibility(View.VISIBLE);
                        spinner_game_number_perm_by.setVisibility(View.VISIBLE);
                        txt_by.setVisibility(View.VISIBLE);
                        layout_perm.setVisibility(View.VISIBLE);
                        spinner_game_number_direct.setSelection(0);
                        spinner_game_number_direct.setVisibility(View.GONE);
                        
                        uncheckbutton();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_game_number_direct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uncheckbutton();
                inputsize = 0;
                inputsize = i+1;
              //  newToast(spinner_game_number_direct.getSelectedItem().toString(),getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_game_number_perm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uncheckbutton();
                integerSet.clear();
                inputsize = 0;
                switch (i){
                case 0:

                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_2);
                break;
                case 1:
                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_3);
                break;
                case 2:
                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_4);
                break;
                case 3:
                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_5);
                break;
                case 4:
                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_6);
                break;
                case 5:
                spinner_game_number_perm_by.setAdapter(arrayAdapter_p_7);
                break;
            }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_game_number_perm_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uncheckbutton();
                inputsize = 0;
                inputsize = Integer.parseInt(spinner_game_number_perm_by.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_play.setOnClickListener(view -> {
            String mainBalance = balance[0];
            String amt = amt_stack.getText().toString();
            if (preferences.getString(ud_agent,null).equals("1")) {

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.game_11_personal: {
                            if (inputsize != count) {
                                String message = "you are met to select " + inputsize;
                                newToast(message, getContext());
                                return;
                            } else if (amt.isEmpty()) {
                                newToast("Enter stack amount", getContext());
                                return;
                            } else if (Integer.parseInt(amt) > Integer.parseInt(mainBalance)) {
                                newToast("Insufficent fund", getContext());
                                return;
                            } else {
                                String value = Arrays.toString(integerSet.toArray());
                                String ttt = value.substring(value.indexOf("[") + 1, value.indexOf("]"));
                                String cati = "5/11";
                                String gtype = String.valueOf(spinner_game_type.getSelectedItemPosition());
                                String gameType = null;
                                String gameId;
                                if (gtype.equals("0")) {
                                    gameType = "direct";
                                    gameId = spinner_game_number_direct.getSelectedItem().toString();
                                } else {
                                    gameType = "perm";
                                    gameId = spinner_game_number_perm.getSelectedItem().toString() + "/" + spinner_game_number_perm_by.getSelectedItem().toString();
                                }

                                placeGame(ttt, cati, gameType, gameId, amt);
                            }
                        }
                        break;
                        case R.id.game_11_agent:{
                            layout_phone.setErrorEnabled(false);
                            if (inputsize != count){
                                String message = "you are met to select "+ inputsize;
                                newToast(message,getContext());
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
                                String ttt = value.substring(value.indexOf("[") + 1, value.indexOf("]"));
                                String cati = "5/11";
                                String gtype = String.valueOf(spinner_game_type.getSelectedItemPosition());
                                String gameType = null;
                                String gameId;
                                if (gtype.equals("0")) {
                                    gameType = "direct";
                                    gameId = spinner_game_number_direct.getSelectedItem().toString();
                                } else {
                                    gameType = "perm";
                                    gameId = spinner_game_number_perm.getSelectedItem().toString() + "/" + spinner_game_number_perm_by.getSelectedItem().toString();
                                }

                                placeAgentGame(ttt, cati, gameType, gameId, amt,phone.getText().toString());
                            }



                    }
                        break;
                    }

            } else {
                if (inputsize != count){
                    String message = "you are met to select "+ inputsize;
                    newToast(message,getContext());
                    return;
                }else if (amt.isEmpty()){
                    newToast("Enter stack amount",getContext());
                    return;
                }else if (Integer.parseInt(amt)>Integer.parseInt(mainBalance)){
                    newToast("Insufficent fund",getContext());
                    return;
                }else {
                    String value = Arrays.toString(integerSet.toArray());
                    String ttt = value.substring(value.indexOf("[") + 1, value.indexOf("]"));
                    String cati = "5/11";
                    String gtype = String.valueOf(spinner_game_type.getSelectedItemPosition());
                    String gameType = null;
                    String gameId;
                    if (gtype.equals("0")) {
                        gameType = "direct";
                        gameId = spinner_game_number_direct.getSelectedItem().toString();
                    } else {
                        gameType = "perm";
                        gameId = spinner_game_number_perm.getSelectedItem().toString() + "/" + spinner_game_number_perm_by.getSelectedItem().toString();
                    }

                    placeGame(ttt, cati, gameType, gameId, amt);
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
        amt_stack.setText(null);
        layout.removeAllViews();
    }
    private void placeAgentGame(String placeNumber,String cati,String gtype,String gameId,String amt,String phone){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.setMessage("Placing game...");
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_agent_play, response -> {
            Log.i(TAG, response);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    newToast("Agent Successfully stacked",getContext());
                    //newToast("you played "+ Arrays.toString(integerSet.toArray()),getContext());
                    integerSet.clear();
                    uncheckbutton();

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
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ud_user_id,preferences.getString(Constants.ud_user_id,null));
                params.put(Constants.ud_token,preferences.getString(Constants.ud_token,null));
                params.put(Constants.ud_wallet,preferences.getString(Constants.ud_wallet,null));
                params.put("phone",phone);
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
        progressDialog.setMessage("Placing game...");
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url_play_game, response -> {
            Log.i(TAG, response);
            progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    newToast(" Successfully stacked",getContext());
                    newToast("you played "+ Arrays.toString(integerSet.toArray()),getContext());
                    integerSet.clear();
                    uncheckbutton();

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
