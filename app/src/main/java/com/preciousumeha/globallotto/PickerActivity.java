package com.preciousumeha.globallotto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class PickerActivity extends AppCompatActivity {
    GridLayout gridView;
    LinearLayout layout;
    int count = 0;
    int[] value = new int[91];
    private TextView[] textViews = new TextView[91];
    private ToggleButton[] toggleButtons = new ToggleButton[91];
    private Set<Integer> integerSet = new LinkedHashSet<>();
    MaterialTextView selectedtextView;
    private int r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        Intent intent = getIntent();
        r = intent.getIntExtra("t",0);
        Toolbar toolbar = findViewById(R.id.picker_tool);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        selectedtextView = findViewById(R.id.picker_selected_number);
        layout = findViewById(R.id.game_selected_linear);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8,8,8,8);

        gridView = findViewById(R.id.picker_gridview);
        for (int i =1; i <91; i++) {
            toggleButtons[i] = new ToggleButton(this);
            toggleButtons[i].setBackground(getResources().getDrawable(R.drawable.game_btn_checker));
            toggleButtons[i].setTextOff(String.valueOf(i));
            toggleButtons[i].setTextOn(String.valueOf(i));
            toggleButtons[i].setChecked(false);
            toggleButtons[i].setLayoutParams(params);
            toggleButtons[i].setMinHeight(0);
            toggleButtons[i].setTextColor(getResources().getColor(R.color.color_white));
            toggleButtons[i].setMinWidth(0);
            toggleButtons[i].setMinimumHeight(0);
            toggleButtons[i].setMinimumWidth(0);
            toggleButtons[i].setPadding(12,12,12,12);
            gridView.addView(toggleButtons[i]);
            int ii = i;
            toggleButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    tt(b,toggleButtons[ii], ii);
                }
            });
        }

    }
    private void tt(boolean b, ToggleButton v, int pos){
        String value = v.getTextOn().toString();
        if (b){
            count = count +1;

            textViews[pos] = new TextView(this);
            textViews[pos].setBackground(getResources().getDrawable(R.drawable.round_background_checked));
            textViews[pos].setGravity(Gravity.CENTER);
            textViews[pos].setTextColor(getResources().getColor(R.color.color_white));
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
        selectedtextView.setText(num);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.menu_check){

            if (integerSet.toArray().length != r){
                String string = "you have not selected any number " + r;
                Constants.newToast(string,this);
            }else {
                String value = Arrays.toString(integerSet.toArray());

                Intent returnIntent = new Intent();
               returnIntent.putExtra("result",value);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
