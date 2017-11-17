package com.example.cm.juyiz.ui.activity;
/*
支付结果界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayResultActivity extends BaseActivity {

    @Bind(R.id.tv_payresult_title)//标题
            TextView tv_title;
    @Bind(R.id.iv_payresult)//成功或失败图片
            ImageView iv_payresult;
    @Bind(R.id.tv_payresult_result)//支付结果
            TextView tv_result;
    @Bind(R.id.tv_payresult_state)//状态
            TextView tv_state;

    //如果支付成功，显示订单信息
    @Bind(R.id.linear_payresult_infos)
    LinearLayout linear_infos;
    @Bind(R.id.tv_payresult_ordernumber)//订单编号
            TextView tv_ordernumber;
    @Bind(R.id.tv_payresult_tradingtime)//交易时间
            TextView tv_tradingtime;
    @Bind(R.id.tv_payresult_payway)//支付方式
            TextView tv_payway;
    @Bind(R.id.tv_payresult_paymoney)//支付金额
            TextView tv_paymoney;
    @Bind(R.id.tv_payresult_home)//返回首页 or 去支付
            TextView tv_home;
    private String pay_result;
    private int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_result);
        ButterKnife.bind(this);//初始化bind库
        Intent intent = getIntent();
        pay_result = intent.getStringExtra("payresult");
        order_id = intent.getIntExtra("order_id", 0);
        initUI();
    }

    private void initUI() {
        if (pay_result.equals("success")) {
            tv_title.setText("支付成功");
            iv_payresult.setImageResource(R.drawable.pay_success);
            tv_result.setText("支付成功");
            tv_state.setText("订单即将发货");
            linear_infos.setVisibility(View.VISIBLE);
            tv_home.setText("返回首页");
        } else if (pay_result.equals("failed")) {
            tv_title.setText("支付失败");
            iv_payresult.setImageResource(R.drawable.pay_failure);
            tv_result.setText("支付失败");
            tv_state.setText("您的订单将在60分钟后失效");
            linear_infos.setVisibility(View.GONE);
            tv_home.setText("去支付");
        }
    }


    /*
普通点击事件
*/
    @OnClick({R.id.iv_payresult_back, R.id.tv_payresult_checkorder, R.id.tv_payresult_home})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_payresult_back:
                finish();
                break;
            case R.id.tv_payresult_checkorder://个人中心订单详情

                break;
            case R.id.tv_payresult_home://返回首页/去支付
                Intent intent = null;
                if (pay_result.equals("success")) {
                    intent = new Intent(PayResultActivity.this, HomeActivity.class);
                } else if (pay_result.equals("failed")) {
                    intent = new Intent(PayResultActivity.this, PayActivity.class);
                    intent.putExtra("order_id", order_id);
                }
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
