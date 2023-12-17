package com.preciousumeha.globallotto.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.classes.DateItem;
import com.preciousumeha.globallotto.classes.GeneralResultItem;
import com.preciousumeha.globallotto.classes.ListItem;
import com.preciousumeha.globallotto.entity.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private Context context;

        List<ListItem> consolidatedList = new ArrayList<ListItem>();

        public ResultsAdapter(Context context){
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v;

            switch (viewType) {

                case ListItem.TYPE_GENERAL:
                    v = inflater.inflate(R.layout.item_game, parent,
                            false);
                    viewHolder = new GeneralViewHolder(v);
                    break;

                case ListItem.TYPE_DATE:
                    v = inflater.inflate(R.layout.item_date, parent, false);
                    viewHolder = new DateViewHolder(v);
                    break;


            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            switch (viewHolder.getItemViewType()) {

                case ListItem.TYPE_GENERAL:
                    GeneralResultItem generalResultItem = (GeneralResultItem) consolidatedList.get(position);
                    GeneralViewHolder generalViewHolder = (GeneralViewHolder) viewHolder;
                    generalViewHolder.game_id.setText(generalResultItem.getResults().getGame_id());
                    String v = generalResultItem.getResults().getGame_type()+ " "+
                            generalResultItem.getResults().getGame_number();
                    generalViewHolder.game_type.setText(v);
                    String ff = "₦"+generalResultItem.getResults().getGame_stack();
                    generalViewHolder.game_stack.setText(ff);
                    String t = generalResultItem.getResults().getName();
                    if (t.equals("5/11")){
                    String ss = "Quick Lotto("+t+")";
                    generalViewHolder.full_name.setText(ss);
                }else {
                    String ss = "Lucky Lotto("+t+")";
                    generalViewHolder.full_name.setText(ss);
                }

                    String status = generalResultItem.getResults().getGame_status();
                    if (status.equals("1")){
                        generalViewHolder.status_img_view.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                        String re = generalResultItem.getResults().getGame_result();
                        generalViewHolder.layout_win.setVisibility(View.VISIBLE);
                        generalViewHolder.win_label.setVisibility(View.VISIBLE);
                        generalViewHolder.layout_win.removeAllViews();
                        TextView[] textViews = new TextView[re.split(",").length];
                        generalViewHolder.layout_win.removeAllViews();
                        for(int i =0;i<re.split(",").length;i++) {
                            textViews[i] = new TextView(context);
                            textViews[i].setBackground(context.getResources().getDrawable(R.drawable.round_background_checked));
                            textViews[i].setGravity(Gravity.CENTER);
                            textViews[i].setTextColor(context.getResources().getColor(R.color.color_white));
                            textViews[i].setHeight(30);
                            textViews[i].setWidth(30);
                            textViews[i].setTypeface(Typeface.DEFAULT_BOLD);
                            textViews[i].setText(re.split(",")[i]);
                            generalViewHolder.layout_win.addView(textViews[i]);
                        }
                    }else if (status.equals("2")){
                        generalViewHolder.status_img_view.setImageResource(R.drawable.ic_baseline_cancel_24);
                        String re = generalResultItem.getResults().getGame_result();
                        generalViewHolder.layout_win.setVisibility(View.VISIBLE);
                        generalViewHolder.win_label.setVisibility(View.VISIBLE);
                        generalViewHolder.layout_win.removeAllViews();
                        TextView[] textViews = new TextView[re.split(",").length];
                        generalViewHolder.layout_win.removeAllViews();
                        for(int i =0;i<re.split(",").length;i++) {
                            textViews[i] = new TextView(context);
                            textViews[i].setBackground(context.getResources().getDrawable(R.drawable.round_background_checked));
                            textViews[i].setGravity(Gravity.CENTER);
                            textViews[i].setTextColor(context.getResources().getColor(R.color.color_white));
                            textViews[i].setHeight(30);
                            textViews[i].setWidth(30);
                            textViews[i].setTypeface(Typeface.DEFAULT_BOLD);
                            textViews[i].setText(re.split(",")[i]);
                            generalViewHolder.layout_win.addView(textViews[i]);
                        }
                    }else {
                        generalViewHolder.status_img_view.setImageResource(R.drawable.ic_baseline_trending_up_24);
                        generalViewHolder.layout_win.setVisibility(View.GONE);
                        generalViewHolder.win_label.setVisibility(View.GONE);
                    }
                    String tt= generalResultItem.getResults().getPlayed_no();
                    TextView[] textViews1 = new TextView[tt.split(",").length];
                    generalViewHolder.layout_played.removeAllViews();
                    for(int i =0;i<tt.split(",").length;i++){
                        textViews1[i] = new TextView(context);
                        textViews1[i].setBackground(context.getResources().getDrawable(R.drawable.round_background));
                        textViews1[i].setGravity(Gravity.CENTER);
                        textViews1[i].setTextColor(context.getResources().getColor(R.color.color_white));
                        textViews1[i].setHeight(30);
                        textViews1[i].setWidth(30);
                        textViews1[i].setTypeface(Typeface.DEFAULT_BOLD);
                        textViews1[i].setText(tt.split(",")[i]);
                        generalViewHolder.layout_played.addView(textViews1[i]);
                    }
                    break;
                case ListItem.TYPE_DATE:
                    DateItem dateItem = (DateItem) consolidatedList.get(position);
                    DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                    dateViewHolder.txtTitle.setText(dateItem.getDate());
                    break;
            }
        }
        // ViewHolder for date row item
        class DateViewHolder extends RecyclerView.ViewHolder {
            MaterialTextView txtTitle;

            DateViewHolder(View v) {
                super(v);
                this.txtTitle = v.findViewById(R.id.date);

            }
        }

        // View holder for general row item
        class GeneralViewHolder extends RecyclerView.ViewHolder {
            MaterialTextView game_id,game_type,game_stack,full_name,win_label;
            ImageView status_img_view;
            LinearLayout layout_played,layout_win;
            GeneralViewHolder(View v) {
                super(v);
                this.game_id = v.findViewById(R.id.item_game_id);
                this.game_type = v.findViewById(R.id.item_game_name);
                this.game_stack = v.findViewById(R.id.item_game_stack);
                this.layout_played = v.findViewById(R.id.item_game_played);
                this.layout_win = v.findViewById(R.id.item_game_winner);
                this.status_img_view = v.findViewById(R.id.item_game_status);
                this.full_name = v.findViewById(R.id.game_full_name);
                this.win_label = v.findViewById(R.id.item_game_label);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return consolidatedList.get(position).getType();

        }

        @Override
        public int getItemCount() {
            return consolidatedList.size();
        }
        public void clearList(){
            consolidatedList.clear();
        }
        public void setResults(List<Results> results) {


                HashMap<String, List<Results>> groupedHashMap = groupDataIntoHashMap(results);

                for (String date : groupedHashMap.keySet()) {
                    DateItem dateItem = new DateItem();
                    dateItem.setDate(date);
                    consolidatedList.add(dateItem);


                    for (Results pojoOfJsonArray : groupedHashMap.get(date)) {
                        GeneralResultItem generalItem = new GeneralResultItem();
                        generalItem.setResults(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
                        consolidatedList.add(generalItem);
                    }
                }
                notifyDataSetChanged();

        }


        private HashMap<String, List<Results>> groupDataIntoHashMap(List<Results> listOfPojosOfJsonArray) {

            HashMap<String, List<Results>> groupedHashMap = new HashMap<>();

            for (Results pojoOfJsonArray : listOfPojosOfJsonArray) {

                String hashMapKey = pojoOfJsonArray.getGame_date();

                if (groupedHashMap.containsKey(hashMapKey)) {
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
                } else {
                    // The key is not there in the HashMap; create a new key-value pair
                    List<Results> list = new ArrayList<>();
                    list.add(pojoOfJsonArray);
                    groupedHashMap.put(hashMapKey, list);
                }
            }

            return groupedHashMap;
        }
}