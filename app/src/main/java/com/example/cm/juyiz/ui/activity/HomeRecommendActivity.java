package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 2.聚推荐界面
*/

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.RecommendBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeRecommendActivity extends BaseActivity {
    private List<RecommendBean> recommendBean = new ArrayList<>();

    @Bind(R.id.linear_recommend_notnet)
    AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_recommend)
    AutoLinearLayout linear_recommend;
    @Bind(R.id.tv_recommend_nothing)
    TextView tv_nothing;
    @Bind(R.id.list_recommend)
    ListView list_recommend;

    private RecommendAdapter recommendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recommend);
        ButterKnife.bind(this);//初始化bind库
        initView();
        connecteNet();
    }

    /*
判断是否有网络
 */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_recommend.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_recommend.setVisibility(View.GONE);
        }
    }


    /*
  初始化数据
   */
    private void initData() {
        new OkHttpUtil().get(DataUrl.URL_Recommend, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<RecommendBean>>() {
                }.getType();
                recommendBean.clear();
                recommendBean = jsonUtil.analysis2List(data, type);
                if (recommendBean.size() != 0) {
                    tv_nothing.setVisibility(View.GONE);
                    list_recommend.setVisibility(View.VISIBLE);
                    recommendAdapter.setData(recommendBean);
                    recommendAdapter.notifyDataSetChanged();
                } else {
                    tv_nothing.setVisibility(View.VISIBLE);
                    list_recommend.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
     初始化控件
       */
    private void initView() {
        recommendAdapter = new RecommendAdapter(this, recommendBean, R.layout.item_recommend_list);
        list_recommend.setAdapter(recommendAdapter);
        list_recommend.setFocusable(false);
        list_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RecommendBean recommendBean = HomeRecommendActivity.this.recommendBean.get(position);
                String link_type = recommendBean.getLink_type();
                int link_id = recommendBean.getLink_id();
                new MyUtils().startActivityType(HomeRecommendActivity.this, link_type, link_id);
            }
        });
    }

    /*
    聚推荐适配器
    */
    class RecommendAdapter extends SuperAdapter<RecommendBean> {
        public RecommendAdapter(Context context, List<RecommendBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, RecommendBean item, final int position) {
            helper.setImageResource(R.id.iv_reconmmend_list, R.drawable.loading_product, item.getImg_url());
            helper.setText(R.id.tv_reconmmend_title, item.getMain_title());//商品标题
            helper.setText(R.id.tv_reconmmend_introduce, item.getSub_title());//商品简介
        }
    }


    /*
    普通点击事件
    */
    @OnClick({R.id.iv_reconmmend_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reconmmend_back:
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
