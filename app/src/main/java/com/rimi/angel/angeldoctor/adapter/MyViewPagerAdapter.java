package com.rimi.angel.angeldoctor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Android on 2016/6/1.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList;

    public MyViewPagerAdapter(FragmentManager fm ,List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
