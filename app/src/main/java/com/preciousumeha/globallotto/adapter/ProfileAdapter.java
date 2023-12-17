package com.preciousumeha.globallotto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.classes.prf;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<prf> {

    public ProfileAdapter(@NonNull Context context, ArrayList<prf> prfArrayList) {
        super(context, 0,prfArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        prf prfItem = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_button,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.item_button_img);
        MaterialTextView materialTextView = convertView.findViewById(R.id.item_button_name);
        imageView.setImageResource(prfItem.getImg());
        materialTextView.setText(prfItem.getTitle());
        return convertView;
    }

}

