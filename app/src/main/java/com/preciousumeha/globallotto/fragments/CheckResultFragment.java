package com.preciousumeha.globallotto.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.Constants;
import com.preciousumeha.globallotto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckResultFragment extends Fragment {
    SharedPreferences preferences;
    MaterialTextView v_game_id,date,name,game_type,stack,win_label;
    ImageView imageView;
    LinearLayout layout_played,layout_win;
    MaterialCardView cardView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_check_result, container, false);
        TextInputEditText t_no = v.findViewById(R.id.check_ticket_no);
        TextInputLayout layout = v.findViewById(R.id.check_ticket_no_layout);
        preferences = getActivity().getSharedPreferences(Constants.preference_user_detail, Context.MODE_PRIVATE);
        v_game_id = v.findViewById(R.id.item_game_id);
        date = v.findViewById(R.id.item_game_date);
        name = v.findViewById(R.id.game_full_name);
        game_type = v.findViewById(R.id.item_game_name);
        imageView = v.findViewById(R.id.item_game_status);
        layout_played = v.findViewById(R.id.item_game_played);
        layout_win = v.findViewById(R.id.item_game_winner);
        cardView = v.findViewById(R.id.check_card);
       stack =  v.findViewById(R.id.item_game_stack);
        win_label = v.findViewById(R.id.item_game_label);
        v.findViewById(R.id.check_btn).setOnClickListener(view -> {
            layout.setErrorEnabled(false);
            String game_id = t_no.getText().toString().trim();
            if (game_id.isEmpty()){
                layout.setErrorEnabled(true);
                layout.setError("Please enter ticket number");
            }else if(game_id.length()!=17){
                layout.setErrorEnabled(true);
                layout.setError("Invalid ticket number");
                return;
            }else {
                cardView.setVisibility(View.GONE);
                    refresh(game_id);
            }
        });

        return v;
    }

    public void refresh(String game_id){
        ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyDialogStyle);
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.setMessage("Checking ticket...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_game_check, response -> {
            Log.i(Constants.TAG, response);



                if (response.contains("No record Found")) {
                    progressDialog.dismiss();
                    Constants.newToast("No Record Found ",getContext());
                }else {

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int iii = 0; iii <= array.length(); iii++) {
                            JSONObject o = array.getJSONObject(iii);


                            date.setText(o.getString("Date"));
                            v_game_id.setText(o.getString("gsn"));
                            String nam = o.getString("Game cati");
                            if (nam.equals("5/11")) {
                                String ss = "Quick Lotto(" + nam + ")";
                                name.setText(ss);
                            } else {
                                String ss = "Lucky Lotto(" + nam + ")";
                                name.setText(ss);
                            }
                            String stat = o.getString("Status");
                            if (stat.equals("1")) {
                                imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                                String re = o.getString("Winning no");
                                layout_win.setVisibility(View.VISIBLE);
                                win_label.setVisibility(View.VISIBLE);
                                layout_win.removeAllViews();
                                TextView[] textViews = new TextView[re.split(",").length];
                                layout_win.removeAllViews();
                                for (int i = 0; i < re.split(",").length; i++) {
                                    textViews[i] = new TextView(getContext());
                                    textViews[i].setBackground(getContext().getResources().getDrawable(R.drawable.round_background_checked));
                                    textViews[i].setGravity(Gravity.CENTER);
                                    textViews[i].setTextColor(getContext().getResources().getColor(R.color.color_white));
                                    textViews[i].setHeight(30);
                                    textViews[i].setWidth(30);
                                    textViews[i].setTypeface(Typeface.DEFAULT_BOLD);
                                    textViews[i].setText(re.split(",")[i]);
                                    layout_win.addView(textViews[i]);
                                }
                            } else if (stat.equals("2")) {
                                imageView.setImageResource(R.drawable.ic_baseline_cancel_24);
                                String re = o.getString("Winning no");
                                layout_win.setVisibility(View.VISIBLE);
                                win_label.setVisibility(View.VISIBLE);
                                layout_win.removeAllViews();
                                TextView[] textViews = new TextView[re.split(",").length];
                                layout_win.removeAllViews();
                                for (int i = 0; i < re.split(",").length; i++) {
                                    textViews[i] = new TextView(getContext());
                                    textViews[i].setBackground(getContext().getResources().getDrawable(R.drawable.round_background_checked));
                                    textViews[i].setGravity(Gravity.CENTER);
                                    textViews[i].setTextColor(getContext().getResources().getColor(R.color.color_white));
                                    textViews[i].setHeight(30);
                                    textViews[i].setWidth(30);
                                    textViews[i].setTypeface(Typeface.DEFAULT_BOLD);
                                    textViews[i].setText(re.split(",")[i]);
                                    layout_win.addView(textViews[i]);
                                }
                            } else {
                                imageView.setImageResource(R.drawable.ic_baseline_trending_up_24);
                                layout_win.setVisibility(View.GONE);
                                win_label.setVisibility(View.GONE);

                            }
                            String tt = o.getString("Played Games");
                            TextView[] textViews1 = new TextView[tt.split(",").length];
                            layout_played.removeAllViews();
                            for (int i = 0; i < tt.split(",").length; i++) {
                                textViews1[i] = new TextView(getContext());
                                textViews1[i].setBackground(getContext().getResources().getDrawable(R.drawable.round_background));
                                textViews1[i].setGravity(Gravity.CENTER);
                                textViews1[i].setTextColor(getContext().getResources().getColor(R.color.color_white));
                                textViews1[i].setHeight(30);
                                textViews1[i].setWidth(30);
                                textViews1[i].setTypeface(Typeface.DEFAULT_BOLD);
                                textViews1[i].setText(tt.split(",")[i]);
                                layout_played.addView(textViews1[i]);
                            }
                            String s = o.getString("Game type") + " " + o.getString("Game number");
                            game_type.setText(s);
                            stack.setText(o.getString("Amount"));
                        }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        cardView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                }



            progressDialog.dismiss();

            Constants.newToast("Successfully ",getContext());
        }, error -> {
            Constants.newToast("Connection failed",getContext());
            progressDialog.dismiss();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("gid",game_id);

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);

    }
}
