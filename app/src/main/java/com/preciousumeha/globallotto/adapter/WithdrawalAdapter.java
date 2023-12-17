package com.preciousumeha.globallotto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.classes.DateItem;
import com.preciousumeha.globallotto.classes.GeneralWithdrawalItem;
import com.preciousumeha.globallotto.classes.ListItem;
import com.preciousumeha.globallotto.entity.Withdrawal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WithdrawalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<ListItem> consolidatedList = new ArrayList<ListItem>();

    public WithdrawalAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        switch (viewType) {

            case ListItem.TYPE_GENERAL:
                v = inflater.inflate(R.layout.item_withdrawal, parent,
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ListItem.TYPE_GENERAL:
                GeneralWithdrawalItem withdrawalItem = (GeneralWithdrawalItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
                generalViewHolder.acc_num.setText(withdrawalItem.getWithdrawal().getAcc_no());
                generalViewHolder.bank.setText(withdrawalItem.getWithdrawal().getBank());
                generalViewHolder.name.setText(withdrawalItem.getWithdrawal().getAcc_name());
                String s = "₦"+withdrawalItem.getWithdrawal().getAmount();
                generalViewHolder.amount.setText(s);
                if (withdrawalItem.getWithdrawal().getStatus().equals("1")){
                    generalViewHolder.imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                }else {
                    generalViewHolder.imageView.setImageResource(R.drawable.ic_baseline_trending_up_24);
                }
                break;

            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.dateview.setText(dateItem.getDate());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }
    public void clearList(){
        consolidatedList.clear();
    }
    @Override
    public int getItemCount() {
        return consolidatedList.size();
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
        MaterialTextView name, amount, bank, acc_num;
        ImageView imageView;

        GeneralViewHolder(View v) {
            super(v);
            this.name = v.findViewById(R.id.item_with_acc_name);
            this.amount = v.findViewById(R.id.item_with_amt);
            this.bank = v.findViewById(R.id.item_with_bank);
            this.acc_num = v.findViewById(R.id.item_with_acc_number);
            this.imageView = v.findViewById(R.id.item_with_close);

        }
    }

    public void setWithdrawal(List<Withdrawal> fundHistoryList){
        HashMap<String, List<Withdrawal>> groupedHashMap = groupDataIntoHashMap(fundHistoryList);
        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);

            for (Withdrawal withdrawal : Objects.requireNonNull(groupedHashMap.get(date))) {
                GeneralWithdrawalItem generalItem = new GeneralWithdrawalItem();
                generalItem.setWithdrawal(withdrawal);
                consolidatedList.add(generalItem);
            }
        }
        notifyDataSetChanged();
    }
    private HashMap<String, List<Withdrawal>> groupDataIntoHashMap(List<Withdrawal> listOfPojosOfJsonArray) {

        HashMap<String, List<Withdrawal>> groupedHashMap = new HashMap<>();

        for (Withdrawal pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<Withdrawal> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        return groupedHashMap;
    }

}
