package com.yijian.them.ui.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class HomeAdapter extends FragmentPagerAdapter {
    private List<Fragment> _fragments;

    public HomeAdapter(FragmentManager fm, List<Fragment> _fragments) {
        super(fm);
        this._fragments = _fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return _fragments.get(i);
    }

    @Override
    public int getCount() {
        return _fragments.size();
    }
}
