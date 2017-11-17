package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 4.排行榜界面
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.RankingListBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
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

public class HomeRankingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private List<RankingListBean> rankingListBean = new ArrayList<>();

    @Bind(R.id.linear_rank_notnet)//没有网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_rank)
    AutoLinearLayout linear_rank;
    @Bind(R.id.tv_rank_nothing)//没有商品
            TextView tv_nothing;
    @Bind(R.id.rg_rank)
    RadioGroup rg_rank;
    @Bind(R.id.rb_rank1)
    RadioButton rb_rank1;
    @Bind(R.id.rb_rank2)
    RadioButton rb_rank2;
    @Bind(R.id.rb_rank3)
    RadioButton rb_rank3;

    @Bind(R.id.list_rank)
    ListView list_rank;

    private RankAdapter rankAdapter;
    private MyUtils myUtils;//自定义工具类
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ranking);
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();
        user = LoginUtil.getinit().getMember();
        initView();
        connecteNet();
        initData(1);
    }

    /*
  判断是否有网络
   */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_rank.setVisibility(View.VISIBLE);
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_rank.setVisibility(View.GONE);
        }
    }

    /*
   初始化数据
    */
    private void initData(int dayId) {
        new OkHttpUtil().get(DataUrl.URL_RankingList + dayId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<RankingListBean>>() {
                }.getType();
                rankingListBean.clear();
                rankingListBean = jsonUtil.analysis2List(data, type);
                if (rankingListBean.size() != 0) {
                    tv_nothing.setVisibility(View.GONE);
                    list_rank.setVisibility(View.VISIBLE);
                    rankAdapter.setData(rankingListBean);
                    rankAdapter.notifyDataSetChanged();
                } else {
                    tv_nothing.setVisibility(View.VISIBLE);
                    list_rank.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    初始化控件
     */
    private void initView() {
        rb_rank1.setSelected(true);
        rg_rank.setOnCheckedChangeListener(this);

        rankAdapter = new RankAdapter(this, rankingListBean, R.layout.item_newrelease_list);
        list_rank.setAdapter(rankAdapter);
        list_rank.setFocusable(false);
        list_rank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int good_id = rankingListBean.get(position).getGood_id();
                Intent intent = new Intent(HomeRankingActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details, good_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        radioIsChecked(checkedId, rb_rank1, 1);
        radioIsChecked(checkedId, rb_rank2, 7);
        radioIsChecked(checkedId, rb_rank3, 30);
    }

    /*
   TODO 判断RadioButton是否选中
    */
    public void radioIsChecked(int checkedId, RadioButton rb, int dayId) {
        if (checkedId == rb.getId()) {
            rb.setSelected(true);
            connecteNet();
            initData(dayId);
        } else {
            rb.setSelected(false);
        }
    }

    /*
   排行榜适配器
   */
    class RankAdapter extends SuperAdapter<RankingListBean> {
        public RankAdapter(Context context, List<RankingListBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final RankingListBean item, final int position) {
            helper.setImageResource(R.id.iv_newrelease_list, R.drawable.loading_product, item.getThumb_url());
            helper.setText(R.id.tv_newrelease_title, item.getGood_title());
            helper.setText(R.id.tv_newrelease_price, item.getPrice() + "");
            //立即抢购
            TextView tv_purchase = helper.getView(R.id.tv_newrelease_purchase);
            int number = item.getNumber();
            if (number == 0) {
                tv_purchase.setText("已售罄");
            } else {
                tv_purchase.setText("立即抢购");
                tv_purchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ConnectionUtils.isConnected(HomeRankingActivity.this)) {//有网络
                            if (user != null) {//已登录---加入购物车
                                if (myUtils.isFastClick()) {
                                    return;
                                }
                                Map<String, Object> map = new HashMap<>();
                                map.put("member_id", user.getMemberid());
                                map.put("good_id", item.getGood_id());
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
                                                    myUtils.showToast(HomeRankingActivity.this, "已加入购物车");
                                                } else {
                                                    myUtils.showToast(HomeRankingActivity.this, result);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {//未登录---进入登录界面
                                startActivity(new Intent(HomeRankingActivity.this, LoginActivity.class));
                            }
                        } else {//没网络
                            myUtils.showToast(HomeRankingActivity.this, "请重新加载网络");
                        }
                    }
                });
            }
        }
    }

    /*
    普通点击事件
    */
    @OnClick({R.id.iv_rank_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rank_back:
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
