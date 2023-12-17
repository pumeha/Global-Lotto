package com.preciousumeha.globallotto.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.preciousumeha.globallotto.fragments.CheckResultFragment;
import com.preciousumeha.globallotto.fragments.ResultsFragment;

public class ResultTabViewPagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Game Results", "Check Results"};
    Context context;
    int totalTabs;
    public ResultTabViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ResultsFragment();
            case 1:
                return new CheckResultFragment();
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
