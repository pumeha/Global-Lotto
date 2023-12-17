package com.preciousumeha.globallotto.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.preciousumeha.globallotto.fragments.GamesFragmeent_11;
import com.preciousumeha.globallotto.fragments.GamesFragment_90;

public class GameTabViewPagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Quick Lotto(5/11)", "Lucky Lotto(5/90)"};
    Context context;
    int totalTabs;
    public GameTabViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            return new GamesFragmeent_11();
            case 1:
              return new GamesFragment_90();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
