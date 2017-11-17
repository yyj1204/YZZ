package com.example.cm.juyiz.ui.activity;
/*
自定义界面
*/

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.HomeMainAdsBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomActivity extends BaseActivity {
    private List<HomeMainAdsBean> adsBean = new ArrayList<>();
    @Bind(R.id.tv_custom_title)
    TextView tv_title;
    @Bind(R.id.list_custom)
    ListView list;
    private CustomAdapter customAdapter;

    private int customId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);//初始化bind库
        //TODO 接收首页轮播图，热点信息，广告图 传递过来的自定义类型
        customId = getIntent().getIntExtra("details", 0);

        customAdapter = new CustomAdapter(this, adsBean, R.layout.item_custom_list);
        list.setAdapter(customAdapter);
    }

    /*
       ListView自定义适配器
        */
    class CustomAdapter extends SuperAdapter<HomeMainAdsBean> {
        public CustomAdapter(Context context, List<HomeMainAdsBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }
        @Override
        public void convert(ViewHolder helper, HomeMainAdsBean item, int position) {
            helper.setImageResource(R.id.iv_custom_list,R.drawable.loading_banner, item.getImg_url());
        }
    }

    /*
普通点击事件
*/
    @OnClick(R.id.iv_custom_back)
    public void OnClick(View view){
        finish();
    }
}
