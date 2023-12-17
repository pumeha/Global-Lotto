package com.preciousumeha.globallotto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.classes.DateItem;
import com.preciousumeha.globallotto.classes.GeneralHistoryItem;
import com.preciousumeha.globallotto.classes.ListItem;
import com.preciousumeha.globallotto.entity.ShareFundHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShareFundHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<ListItem> consolidatedList = new ArrayList<ListItem>();

    public ShareFundHistoryAdapter(Context context){
        this.inflater =  LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        switch (viewType) {

            case ListItem.TYPE_GENERAL:
                v = inflater.inflate(R.layout.item_sharefundhistory, parent,
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case ListItem.TYPE_GENERAL:
                GeneralHistoryItem generalHistoryItem = (GeneralHistoryItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
                generalViewHolder.wallet.setText(generalHistoryItem.getShareFundHistory().getWallet());
                String s = "₦ "+generalHistoryItem.getShareFundHistory().getAmount();
                generalViewHolder.amount.setText(s);
                generalViewHolder.name.setText(generalHistoryItem.getShareFundHistory().getName());
                generalViewHolder.time.setText(generalHistoryItem.getShareFundHistory().getTime());
                break;
            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.dateview.setText(dateItem.getDate());
                break;
        }
    }

    public void setShareHistory(List<ShareFundHistory> fundHistoryList){
        HashMap<String, List<ShareFundHistory>> groupedHashMap = groupDataIntoHashMap(fundHistoryList);
        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);

            for (ShareFundHistory history : Objects.requireNonNull(groupedHashMap.get(date))) {
                GeneralHistoryItem generalItem = new GeneralHistoryItem();
                generalItem.setShareFundHistory(history);
                consolidatedList.add(generalItem);
            }
        }
        notifyDataSetChanged();
    }
    public void clearList(){
        consolidatedList.clear();
    }
    @Override
    public int getItemCount() {
        return consolidatedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateview;

        DateViewHolder(View v) {
            super(v);
            this.dateview = v.findViewById(R.id.date);

        }
    }
    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView name,amount,wallet,time;

        GeneralViewHolder(View v) {
            super(v);
            this.name = v.findViewById(R.id.item_share_fund_hist_name);
            this.amount = v.findViewById(R.id.item_share_fund_hist_amt);
            this.wallet = v.findViewById(R.id.item_share_fund_hist_wallet);
            this.time = v.findViewById(R.id.item_share_fund_hist_time);
            this.time.setVisibility(View.VISIBLE);

        }
    }

    private HashMap<String, List<ShareFundHistory>> groupDataIntoHashMap(List<ShareFundHistory> listOfPojosOfJsonArray) {

        HashMap<String, List<ShareFundHistory>> groupedHashMap = new HashMap<>();

        for (ShareFundHistory pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<ShareFundHistory> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        return groupedHashMap;
    }
}
