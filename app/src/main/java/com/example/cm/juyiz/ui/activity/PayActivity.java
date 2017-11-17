package com.example.cm.juyiz.ui.activity;
//选择支付方式

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alipay.sdk.app.PayTask;
import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.PayType;

import com.example.cm.juyiz.pay.PayView;
import com.example.cm.juyiz.pay.alipay.AlipayUtil;
import com.example.cm.juyiz.pay.wxapi.WXUtils;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity implements PayView {


    @Bind(R.id.pay_type_lv)
    ListView paytype_lv;

    private PayTypeAdapter payTypeAdapter;
    private List<PayType> payTypes;


    private int order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);//初始化bind库
        payTypes = new ArrayList<PayType>();
        Intent intent = getIntent();
        order_id = intent.getIntExtra("order_id", 0);
        initData();
        initUI();
    }


    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("terminal_code", "android");
        new OkHttpUtil().post(DataUrl.URL_PaymentMethodGet, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<PayType>>() {
                }.getType();

                payTypes.addAll(jsonUtil.analysis2List(data, type));
                payTypes.get(0).setIs_select(1);//默认选择第一个支付方式
                payTypeAdapter.notifyDataSetChanged();

            }
        });

    }

    private void initUI() {

        payTypeAdapter = new PayTypeAdapter(this, payTypes, R.layout.paytype_item);
        paytype_lv.setAdapter(payTypeAdapter);
        paytype_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (PayType payType : payTypes) {
                    payType.setIs_select(0);
                }
                payTypes.get(position).setIs_select(1);
                payTypeAdapter.setData(payTypes);
                payTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.iv_pay_back)
    public void goback() {
        finish();
    }

    @OnClick(R.id.bt_pay)
    public void pay() {
        for (PayType type : payTypes) {
            if (type.getIs_select() == 1) {
                if ("alipay".equals(type.getPay_code())) {//支付宝支付
                    aliPay();
                } else if ("wechat".equals(type.getPay_code())) {
                    wxPay();
                } else if ("baidu".equals(type.getPay_code())) {

                } else if ("jd".equals(type.getPay_code())) {

                } else if ("unionpay".equals(type.getPay_code())) {

                }
            }
        }
    }


    //支付宝支付
    private void aliPay() {
        //获取请求参数


        final String orderInfo = "";
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = MyConstant.SDK_PAY_FLAG;
                msg.obj = result;
                //支付宝支付
                AlipayUtil alipayUtil = new AlipayUtil(PayActivity.this);
                Handler mHandler = alipayUtil.getalipayHandler();
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //微信支付
    private void wxPay() {
        Map<String, String> map = new HashMap<>();
        //解析参数

        WXUtils utils = new WXUtils(PayActivity.this, map.get("out_trade_no"), map.get("total_fee"), map.get("body"));
        utils.PlaceOrder();
    }


    @Override
    public void alipaySuccess() {
        Intent intent = new Intent(PayActivity.this, PayResultActivity.class);
        intent.putExtra("payresult", "success");
        startActivity(intent);
        finish();
    }

    @Override
    public void alipayFailed() {
        Intent intent = new Intent(PayActivity.this, PayResultActivity.class);
        intent.putExtra("payresult", "failed");
        intent.putExtra("order_id", order_id);
        startActivity(intent);
        finish();

    }

    @Override
    public void wxSuccess() {
        Intent intent = new Intent(PayActivity.this, PayResultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void wxFailed() {

    }

    class PayTypeAdapter extends SuperAdapter<PayType> {

        public PayTypeAdapter(Context context, List<PayType> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, PayType item, int position) {
            ImageView type_icon = (ImageView) helper.getView(R.id.pay_type_icon);
            ImageView is_select = (ImageView) helper.getView(R.id.iv_pay_wx);
            GlideUtils.loadImage(PayActivity.this, item.getPay_icon(), R.drawable.loading_classify, type_icon);
            helper.setText(R.id.pay_type_name, item.getPay_name());
            if (item.getIs_select() == 1) {
                is_select.setSelected(true);
            } else if (item.getIs_select() == 0) {
                is_select.setSelected(false);
            }

        }
    }
}
