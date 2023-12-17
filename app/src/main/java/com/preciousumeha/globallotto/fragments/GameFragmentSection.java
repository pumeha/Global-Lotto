package com.preciousumeha.globallotto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.preciousumeha.globallotto.R;
import com.preciousumeha.globallotto.adapter.GameTabViewPagerAdapter;

public class GameFragmentSection extends Fragment {


    public GameFragmentSection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v =  inflater.inflate(R.layout.fragment_game_section, container, false);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = v.findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Quick Lotto(5/11)"));
        tabLayout.addTab(tabLayout.newTab().setText("Lucky Lotto(5/90)"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = v.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        GameTabViewPagerAdapter adapter =  new GameTabViewPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);






        return v;
    }
}