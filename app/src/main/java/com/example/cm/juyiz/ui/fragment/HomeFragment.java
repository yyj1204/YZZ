package com.example.cm.juyiz.ui.fragment;
/*
商城首页片段
*/

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cm.juyiz.ui.activity.HomeMessageActivity;
import com.example.cm.juyiz.ui.activity.LoginActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.ui.activity.SearchActivity;
import com.example.cm.juyiz.bean.HomeTypeBean;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.customwidget.PagerSlidingTabStrip;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private List<HomeTypeBean> list = new ArrayList<>();
    private View view;

    private PagerSlidingTabStrip psts;
    private ViewPager vp;

    private ViewPagerAdapter pagerAdapter;

    private Member user;

    public HomeFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            //user = MemberUtils.getUser(APP.getContext());
            user = LoginUtil.getinit().getMember();
            initUI();
            initData();
        }
        return view;
    }

    /*
    初始化控件
     */
    private void initUI() {
        //字体加粗
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_home_title);
//        TextPaint tp = tv_title.getPaint();
//        tp.setFakeBoldText(true);

        view.findViewById(R.id.rela_home_message).setOnClickListener(this);
        view.findViewById(R.id.rela_home_search).setOnClickListener(this);
        vp = (ViewPager) view.findViewById(R.id.vp_home);
        psts = (PagerSlidingTabStrip) view.findViewById(R.id.psts_home);

        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        vp.setAdapter(pagerAdapter);
    }

    /*
    初始化数据
     */
    private void initData() {
        new OkHttpUtil().get(DataUrl.URL_HomeList, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<List<HomeTypeBean>>() {
                }.getType();
                list.clear();
                list = jsonUtil.analysis2List(data, listType);
                pagerAdapter.notifyDataSetChanged();
                // 注意必须在viewpager设置适配器之后调用
                psts.setViewPager(vp);
            }
        });
    }

    /*
   点击事件
    */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rela_home_message:
                if (user != null) {
                    startActivity(new Intent(getActivity(), HomeMessageActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rela_home_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getList_name();
        }

        @Override
        public Fragment getItem(int position) {
            //初始化首页类型的产品片段,其余类型的产品共用一个片段
            if (position == 0) {
                return new HomeMainFragment();
            } else {
                HomeMenuFragment menuFragment = new HomeMenuFragment();
                //传递给子类型片段的id
                Bundle bundle = new Bundle();
                HomeTypeBean homeTypeBean = list.get(position);
                bundle.putSerializable(MyConstant.KEY_Homemenu, homeTypeBean);
                menuFragment.setArguments(bundle);
                return menuFragment;
            }
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
    }
}
