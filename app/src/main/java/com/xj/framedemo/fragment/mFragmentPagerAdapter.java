package com.xj.framedemo.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by xj on 2018/10/29.
 */

public class mFragmentPagerAdapter extends FragmentPagerAdapter {
    private TestFragment myFragment = null;

    public mFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        myFragment = new TestFragment();
        //这里可以根据位置返回相应的Fragment，例如娱乐，体育，新闻等等单独弄个Fragment m
        myFragment.setTitle("This is " + position);
        return myFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //返回Tab名字
        String name = "Tag" + position;
        return name;
    }

    @Override
    public int getCount() {
        return 6; //这里就简单返回个固定数字，可以根据需要返回Tab的数量
    }
}