package com.example.cm.juyiz.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.PagerSlidingTabStrip;
import com.example.cm.juyiz.ui.fragment.OrderMenuFragment;
import com.example.cm.juyiz.json.Types;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class OrderActivity extends FragmentActivity implements View.OnClickListener {
    private String[] titles = {"全部订单", "待付款", "待发货", "待收货", "交易完成","已关闭"};
    private ViewPagerAdapter pagerAdapter;
    ViewPager vp;
    PagerSlidingTabStrip psts;

    private ImageView order_break;

    private List<Types> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        String type = intent.getStringExtra("order");


        initUI();

        if (type.equals("all")) {
            vp.setCurrentItem(0);
        } else if (type.equals("payment")) {
            vp.setCurrentItem(1);
        } else if (type.equals("pending")) {
            vp.setCurrentItem(2);
        } else if (type.equals("tobereceived")) {
            vp.setCurrentItem(3);
        }
    }

    /*
    初始化控件
     */
    private void initUI() {
        order_break = (ImageView) findViewById(R.id.order_break);
        order_break.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.vp_order);
        psts = (PagerSlidingTabStrip) findViewById(R.id.psts_order);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        // 注意必须在viewpager设置适配器之后调用
        psts.setViewPager(vp);
        vp.setCurrentItem(1);
//        psts.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.order_break:
                finish();
                break;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
//            return list.get(position).getTypeName();
        }

        @Override
        public Fragment getItem(int position) {
            //初始化首页类型的产品片段,其余类型的产品共用一个片段

            OrderMenuFragment menuFragment = new OrderMenuFragment();
            Bundle bundle = new Bundle();
            bundle.putString("position", titles[position]);
//                bundle.putString("position", list.get(position).getTypeName());
            menuFragment.setArguments(bundle);
            return menuFragment;

        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
