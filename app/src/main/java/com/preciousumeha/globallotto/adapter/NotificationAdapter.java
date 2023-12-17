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
import com.preciousumeha.globallotto.classes.GeneralNotificationItem;
import com.preciousumeha.globallotto.classes.ListItem;
import com.preciousumeha.globallotto.entity.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    List<ListItem> itemList = new ArrayList<>();
    public NotificationAdapter(Context context){
       this.inflater =  LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType){
            case ListItem.TYPE_DATE:
                v = inflater.inflate(R.layout.item_date,parent,false);
                viewHolder = new DateViewHolder(v);
                break;
            case ListItem.TYPE_GENERAL:
                v = inflater.inflate(R.layout.item_sharefundhistory,parent,false);
                viewHolder = new GeneralViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case ListItem.TYPE_GENERAL:
                GeneralNotificationItem item = (GeneralNotificationItem) itemList.get(position);
                GeneralViewHolder holder1 = (GeneralViewHolder) holder;
                holder1.message.setText(item.getNotification().getMessage());
                break;
            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) itemList.get(position);
                DateViewHolder holder2 = (DateViewHolder) holder;
                holder2.dateView.setText(dateItem.getDate());
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
    class DateViewHolder extends RecyclerView.ViewHolder{
        private TextView dateView;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateView = itemView.findViewById(R.id.date);
        }
    }
    class GeneralViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView amount,wallet,title,message;
        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            this.amount = itemView.findViewById(R.id.item_share_fund_hist_amt);
            this.message = itemView.findViewById(R.id.item_share_fund_hist_name);
            this.wallet = itemView.findViewById(R.id.item_share_fund_hist_wallet);
            this.title = itemView.findViewById(R.id.item_share_fund_hist_title);
            this.title.setVisibility(View.GONE);
            this.wallet.setVisibility(View.GONE);
            this.amount.setVisibility(View.GONE);
        }
    }
    public void setNotification(List<Notification> notificationList){
        HashMap<String, List<Notification>> groupedHashMap = groupDataIntoHashMap(notificationList);
        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            itemList.add(dateItem);

            for (Notification notification: Objects.requireNonNull(groupedHashMap.get(date))) {
                GeneralNotificationItem notificationItem = new GeneralNotificationItem();
                notificationItem.setNotification(notification);
                itemList.add(notificationItem);
            }
        }
        notifyDataSetChanged();
    }
    private HashMap<String, List<Notification>> groupDataIntoHashMap(List<Notification> notificationList) {

        HashMap<String, List<Notification>> groupedHashMap = new HashMap<>();

        for (Notification notification : notificationList) {

            String hashMapKey = notification.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(notification);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<Notification> list = new ArrayList<>();
                list.add(notification);
                groupedHashMap.put(hashMapKey, list);
            }
        }

        return groupedHashMap;
    }

}
