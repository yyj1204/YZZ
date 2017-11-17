package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 3.最新发布界面
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.NewListBean;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeNewReleaseActivity extends BaseActivity {
    private List<NewListBean> newListBean = new ArrayList<>();

    @Bind(R.id.linear_newrelease_notnet)
    AutoLinearLayout linear_notnet;
    @Bind(R.id.scroll_newrelease)
    ScrollView scrollView;
    @Bind(R.id.iv_newrelease_banner)
    ImageView iv_banner;
    @Bind(R.id.list_newrelease)
    ListView4ScrollView list_newrelease;
    private NewReleaseAdapter newReleaseAdapter;


    private MyUtils myUtils;//自定义工具类
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new_release);
        ButterKnife.bind(this);//初始化bind库
        user = LoginUtil.getinit().getMember();
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
            scrollView.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
    }

    /*
   初始化数据
    */
    private void initData() {
        //广告图
        new OkHttpUtil().get(DataUrl.URL_NewListImg, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    JSONObject data1 = json.getJSONObject("data");
                    if (data1 != null) {
                        String img_url = data1.getString("img_url");
                        GlideUtils.loadImage(HomeNewReleaseActivity.this, img_url, R.drawable.loading_banner, iv_banner);
                    } else {
                        iv_banner.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //商品列表
        new OkHttpUtil().get(DataUrl.URL_NewList, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<NewListBean>>() {
                }.getType();
                newListBean.clear();
                newListBean = jsonUtil.analysis2List(data, type);
                if (newListBean.size() != 0) {
                    newReleaseAdapter.setData(newListBean);
                    newReleaseAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
   初始化控件
    */
    private void initView() {
        myUtils = new MyUtils();

        newReleaseAdapter = new NewReleaseAdapter(this, newListBean, R.layout.item_newrelease_list);
        list_newrelease.setAdapter(newReleaseAdapter);
        list_newrelease.setFocusable(false);
        list_newrelease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int id = newListBean.get(position).getId();
                Intent intent = new Intent(HomeNewReleaseActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details, id);
                startActivity(intent);
            }
        });
    }

    /*
    最新发布适配器
    */
    class NewReleaseAdapter extends SuperAdapter<NewListBean> {
        public NewReleaseAdapter(Context context, List<NewListBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final NewListBean item, final int position) {
            helper.setImageResource(R.id.iv_newrelease_list, R.drawable.loading_product, item.getThumb_url());
            helper.setText(R.id.tv_newrelease_title, item.getTitle());
            helper.setText(R.id.tv_newrelease_price, item.getPrice() + "");
            //立即抢购
            TextView tv_purchase = helper.getView(R.id.tv_newrelease_purchase);
            tv_purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ConnectionUtils.isConnected(HomeNewReleaseActivity.this)) {//有网络
                        if (user != null) {//已登录---加入购物车
                            if (myUtils.isFastClick()) {
                                return;
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("member_id", user.getMemberid());
                            map.put("good_id", item.getId());
                            new OkHttpUtil().post(DataUrl.URL_CartAdd, map, new OkHttpUtil.HttpCallback() {
                                @Override
                                public void onSuccess(String data) {
                                    try {
                                        JSONObject json = new JSONObject(data);
                                        int ret = json.getInt("ret");
                                        if (ret == 200) {
                                            JSONObject data1 = json.getJSONObject("data");
                                            int code = data1.getInt("code");
                                            String result = data1.getString("result");
                                            if (code == 1) {
                                                myUtils.showToast(HomeNewReleaseActivity.this, "已加入购物车");
                                            } else {
                                                myUtils.showToast(HomeNewReleaseActivity.this, result);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {//未登录---进入登录界面
                            startActivity(new Intent(HomeNewReleaseActivity.this, LoginActivity.class));
                        }
                    } else {//没网络
                        myUtils.showToast(HomeNewReleaseActivity.this, "请重新加载网络");
                    }
                }
            });
        }
    }

    /*
 普通点击事件
  */
    @OnClick({R.id.iv_newrelease_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_newrelease_back:
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
