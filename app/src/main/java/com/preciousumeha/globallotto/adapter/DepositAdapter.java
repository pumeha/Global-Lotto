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
import com.preciousumeha.globallotto.classes.GeneralDepositItem;
import com.preciousumeha.globallotto.classes.ListItem;
import com.preciousumeha.globallotto.entity.Deposit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DepositAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<ListItem> itemList = new ArrayList<>();
    
    public DepositAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        switch (viewType){
            case ListItem.TYPE_GENERAL:
                v = inflater.inflate(R.layout.item_sharefundhistory,parent,false);
                holder = new GeneralViewHolder(v);

                break;
            case ListItem.TYPE_DATE:
                v = inflater.inflate(R.layout.item_date,parent,false);
                holder = new DateViewHolder(v);

                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()){
                case ListItem.TYPE_GENERAL:
                    GeneralDepositItem depositItem = (GeneralDepositItem) itemList.get(position);
                    GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
                    String s = "₦ "+depositItem.getDeposit().getAmount();
                    generalViewHolder.amount.setText(s);
                    generalViewHolder.name.setText(depositItem.getDeposit().getTransaction_id());
                    generalViewHolder.wallet.setText(depositItem.getDeposit().getStatus());
                    break;
                case ListItem.TYPE_DATE:
                    DateItem dateItem = (DateItem) itemList.get(position);
                    DateViewHolder dateViewHolder = (DateViewHolder) holder;
                    dateViewHolder.dateview.setText(dateItem.getDate());
                    break;


            }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void clearList(){
        itemList.clear();
    }
    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
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
        MaterialTextView name,amount,wallet,title;
        
        GeneralViewHolder(View v) {
            super(v);
            this.title = v.findViewById(R.id.item_share_fund_hist_title);
            this.name = v.findViewById(R.id.item_share_fund_hist_name);
            this.amount = v.findViewById(R.id.item_share_fund_hist_amt);
            this.wallet = v.findViewById(R.id.item_share_fund_hist_wallet);
            this.title.setText("Transaction Information");
        }
    }

    public void setDeposit(List<Deposit> depositList){
        HashMap<String, List<Deposit>> groupedHashMap = groupDataIntoHashMap(depositList);
        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            itemList.add(dateItem);

            for (Deposit deposit: Objects.requireNonNull(groupedHashMap.get(date))) {
                GeneralDepositItem depositItem = new GeneralDepositItem();
                depositItem.setDeposit(deposit);
                itemList.add(depositItem);
            }
        }
        notifyDataSetChanged();
    }
    
    private HashMap<String, List<Deposit>> groupDataIntoHashMap(List<Deposit> depositList) {

        HashMap<String, List<Deposit>> groupedHashMap = new HashMap<>();

        for (Deposit deposit : depositList) {

            String hashMapKey = deposit.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(deposit);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<Deposit> list = new ArrayList<>();
                list.add(deposit);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        return groupedHashMap;
    }
}
