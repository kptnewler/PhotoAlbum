package com.liule.photoalbum.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author 刺雒
 * @time  2017/10/28
 */
public class AlbumPagerAdapter extends FragmentPagerAdapter{
    private String[] mTitles = {"照片","相册"};
    private List<Fragment> mFragments;

    public AlbumPagerAdapter(FragmentManager fm, List<Fragment> fragments ) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
