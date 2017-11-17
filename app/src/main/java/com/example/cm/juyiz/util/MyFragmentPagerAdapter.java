package com.example.cm.juyiz.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.cm.juyiz.ui.fragment.ClassifyFragment;
import com.example.cm.juyiz.ui.fragment.HomeFragment;
import com.example.cm.juyiz.ui.fragment.MineFragment;
import com.example.cm.juyiz.ui.fragment.ShoppingFragment;

/*
HomeMainFragment --- 商城首页
ClassifyFragment --- 分类
ShoppingFragment --- 购物车
MineFragment --- 个人中心
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    private HomeFragment homeFragment = null;
    private ClassifyFragment classifyFragment = null;
    private ShoppingFragment shoppingFragment = null;
    private MineFragment mineFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        homeFragment = new HomeFragment();
        classifyFragment = new ClassifyFragment();
        shoppingFragment = new ShoppingFragment();
        mineFragment = new MineFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                fragment = homeFragment;
                break;
            case 1:
                fragment = classifyFragment;
                break;
            case 2:
                fragment = shoppingFragment;
                break;
            case 3:
                fragment = mineFragment;
                break;
        }
        return fragment;
    }
}
