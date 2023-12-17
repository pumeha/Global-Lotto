package com.preciousumeha.globallotto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class HowToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
        Toolbar toolbar = findViewById(R.id.how_to_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        ArrayList<info> arrayList = new ArrayList<info>(9);
        int[] titles = new int[]{R.string.direct_1,R.string.direct_2,R.string.direct_3,R.string.direct_4,
                R.string.direct_5,R.string.perm_2,R.string.perm_3,R.string.perm_4,R.string.perm_5};
        int[] details = new int[]{R.string.direct_1_note,R.string.direct_2_note,R.string.direct_3_note,R.string.direct_4_note,
                R.string.direct_5_note,R.string.perm_2_note,R.string.perm_3_note,R.string.perm_4_note,R.string.perm_5_note};
        //  arrayList.add(new info(getString(titles[0]),getString(details[0])));
        for (int i = 0;i<9;i++){
            arrayList.add(new info(getString(titles[i]),getString(details[i])));
        }

        HowtoAdapter adapter = new HowtoAdapter(this,arrayList);
        ListView listView = findViewById(R.id.how_to_listview);
        listView.setAdapter(adapter);


    }
    class HowtoAdapter extends ArrayAdapter<info> {

        HowtoAdapter(@NonNull Context context, ArrayList<info> infos) {
            super(context, 0,infos);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // getting data item for the position
            info info = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.how_to_item,parent,false);
            }
            MaterialTextView titleView =  convertView.findViewById(R.id.how_to_item_title);
            MaterialTextView detailView =  convertView.findViewById(R.id.how_to_item_detail);
            titleView.setText(info.title);
            detailView.setText(info.detials);

            return convertView;
        }
    }
    class info{
        private String title,detials;
        info(String title,String detials){
            this.title = title;
            this.detials = detials;
        }

    }
}