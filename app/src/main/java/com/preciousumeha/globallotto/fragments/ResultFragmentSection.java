package com.preciousumeha.globallotto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.adapter.ResultTabViewPagerAdapter;

import java.util.Objects;

public class ResultFragmentSection extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_section,container,false);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = v.findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Game Results"));
        tabLayout.addTab(tabLayout.newTab().setText("Check Results"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = v.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        ResultTabViewPagerAdapter adapter =  new ResultTabViewPagerAdapter(getContext(), Objects.requireNonNull(getActivity()).getSupportFragmentManager(),tabLayout.getTabCount());
//        if (viewPager==null){
//
//        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
        viewPager.invalidate();
        viewPager.setCurrentItem(viewPager.getCurrentItem());




        return v;
    }
}
