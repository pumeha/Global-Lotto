package com.preciousumeha.globallotto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.preciousumeha.globallotto.R;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class PickerAdapter extends BaseAdapter {
    Context context;
    private final int[] numbers;
    LayoutInflater layoutInflater;
    LinearLayout layout ;
    private Set<Integer> integerSet = new LinkedHashSet<>();
    public PickerAdapter(Context context,int[] numbers) {
        this.numbers = numbers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numbers.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View v;
        String value = String.valueOf(numbers[i]+1);
        v =  layoutInflater.inflate(R.layout.item_picker,null,false);
        holder.button = v.findViewById(R.id.item_picker_btn);
        holder.button.setTextOff(value);
        holder.button.setTextOn(value);
        final boolean[] state = {false};
        holder.button.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                integerSet.add(Integer.valueOf(value));
                compoundButton.setChecked(true);
                state[0] = b;
            }else {
                integerSet.remove(Integer.valueOf(value));
                compoundButton.setChecked(false);
            }
            state[0] = b;
        });

        return v;
    }
    class Holder{
        ToggleButton button;
    }
    public String playerValue(){
        return Arrays.toString(integerSet.toArray());
    }

}
