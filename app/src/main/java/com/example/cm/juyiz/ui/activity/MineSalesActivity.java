package com.example.cm.juyiz.ui.activity;
/*
个人中心 --- 销售额界面
*/

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.MineSalesBean;
import com.example.cm.juyiz.bean.SalesVolume;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
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

public class MineSalesActivity extends BaseActivity {

    @Bind(R.id.linear_sales_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.scroll_sales)
    ScrollView scrollView;
    @Bind(R.id.tv_sales_nothing)//没销售额
            TextView tv_nothing;
    @Bind(R.id.tv_minesales_validmoney)//有效消费额
            TextView tv_validmoney;
    @Bind(R.id.list_mine_sales)
    ListView4ScrollView list_sales;

    private List<MineSalesBean> saleBean = new ArrayList<>();
    private SalesAdapter salesAdapter;
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_sales);
        ButterKnife.bind(this);//初始化bind库
        user = LoginUtil.getinit().getMember();
        initUI();
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
        new OkHttpUtil().get(DataUrl.URL_ShareSales + user.getMemberid(), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<SalesVolume>() {
                }.getType();
                SalesVolume salesVolume = (SalesVolume) jsonUtil.analysis2Objiect(data, type);
                if (salesVolume != null) {
                    tv_validmoney.setText(salesVolume.getShare_sales_amount());
                    saleBean.clear();
                    saleBean = salesVolume.getOrder();
                    if (saleBean.size() != 0) {
                        tv_nothing.setVisibility(View.GONE);
                        list_sales.setVisibility(View.VISIBLE);
                        salesAdapter.setData(saleBean);
                        salesAdapter.notifyDataSetChanged();
                    } else {
                        tv_nothing.setVisibility(View.VISIBLE);
                        list_sales.setVisibility(View.GONE);
                    }
                }

                try {
                    JSONObject json = new JSONObject(data);
                    JSONObject data1 = json.getJSONObject("data");
                    String share_sales_amount = data1.getString("share_sales_amount");
                    tv_validmoney.setText(share_sales_amount);
                    JSONArray orders = data1.getJSONArray("order");
                    if (orders.length() != 0) {
                        tv_nothing.setVisibility(View.GONE);
                        list_sales.setVisibility(View.VISIBLE);
                        saleBean.clear();
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject saleJson = orders.getJSONObject(i);

                            MineSalesBean mineSalesData = new MineSalesBean();
                            mineSalesData.setOrder_number(saleJson.getString("order_number"));
                            mineSalesData.setReward_amount(saleJson.getString("reward_amount"));
                            mineSalesData.setMobile(saleJson.getString("mobile"));
                            mineSalesData.setWechat_nickname(saleJson.getString("wechat_nickname"));
                            if (saleJson.isNull("wechat_headimgurl")) {
                                mineSalesData.setWechat_headimgurl("");
                            } else {
                                mineSalesData.setWechat_headimgurl(saleJson.getString("wechat_headimgurl"));
                            }
                            saleBean.add(mineSalesData);
                        }
                        salesAdapter.notifyDataSetChanged();
                    } else {
                        tv_nothing.setVisibility(View.VISIBLE);
                        list_sales.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
   初始化控件
    */
    private void initUI() {
        salesAdapter = new SalesAdapter(this, saleBean, R.layout.item_mine_sales_list);
        list_sales.setFocusable(false);
        list_sales.setAdapter(salesAdapter);
    }

    /*
奖励消息适配器
*/
    class SalesAdapter extends SuperAdapter<MineSalesBean> {
        public SalesAdapter(Context context, List<MineSalesBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, MineSalesBean item, int position) {
            //微信头像
            ImageView iv_head = helper.getView(R.id.iv_minesales_head);
            String wechat_headimgurl = item.getWechat_headimgurl();
            if (!wechat_headimgurl.equals("") && wechat_headimgurl != null) {
                GlideUtils.loadCircleImage(MineSalesActivity.this, wechat_headimgurl, iv_head);
            } else {
                iv_head.setImageResource(R.drawable.login);
            }
            //用户名
            String wechat_nickname = item.getWechat_nickname();
            String mobile = item.getMobile();
            if (!wechat_nickname.equals("")) {
                helper.setText(R.id.tv_minesales_name, "用户名：" + wechat_nickname);
            } else {
                helper.setText(R.id.tv_minesales_name, "用户名：" + mobile);
            }
            helper.setText(R.id.tv_minesales_money, "奖励金额：" + item.getReward_amount());
            helper.setText(R.id.tv_minesales_order, "订单编号：" + item.getOrder_number());
        }
    }

    /*
  普通点击事件
   */
    @OnClick({R.id.iv_minesales_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_minesales_back:
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
