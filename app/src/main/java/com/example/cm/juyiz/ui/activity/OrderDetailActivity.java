package com.example.cm.juyiz.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.adapter.impl.AddressManagerView;
import com.example.cm.juyiz.bean.Address;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.OrderDateil;
import com.example.cm.juyiz.bean.OrderGoods;
import com.example.cm.juyiz.bean.Result;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.ui.dialog.LoadingDialog;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class OrderDetailActivity extends BaseActivity {

    private List<OrderGoods> goods;
    private int content;
    private int userinfo;

    private OrderDateil orderDateil;


    @Bind(R.id.order_none)
    LinearLayout order_noneaddress;
    @Bind(R.id.order_manage)
    LinearLayout order_addressmanage;
    @Bind(R.id.order_detail_freight)
    TextView order_feright;
    @Bind(R.id.order_detail_address_user)
    TextView order_address_user;
    @Bind(R.id.order_detail_address_address)
    TextView order_address;
    @Bind(R.id.order_detail_amount)
    TextView order_amount;
    @Bind(R.id.goods_lv)
    ListView4ScrollView goods_lv;

    private Member user;
    private String cart_id;
    private Address address;

    private orderDetailAdapter adapter;
    private LoadingDialog dialog;
    private LoadingDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        user = LoginUtil.getinit().getMember();
        builder = new LoadingDialog.Builder(this);
        Intent intent = getIntent();
        cart_id = intent.getStringExtra("cart_id");
        initData();
        initUI();
    }

    private void initData() {
        Sumit();
    }

    private void Sumit() {
        dialog = builder.createSingleButtonDialog();
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", user.getMemberid());
        map.put("cart_id", cart_id);
        if (address != null) {
            map.put("address_id", address.getId());
        }
        new OkHttpUtil().post(DataUrl.URL_CartSettlement, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.e("order", data);
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<OrderDateil>() {
                }.getType();
                orderDateil = (OrderDateil) jsonUtil.analysis2Objiect(data, type);
                if (orderDateil != null) {
                    if (orderDateil.getAddress() != null) {
                        address = orderDateil.getAddress();
                        order_addressmanage.setVisibility(View.VISIBLE);
                        order_noneaddress.setVisibility(View.GONE);
                        order_address_user.setText(address.getConsignee());
                        order_address.setText(address.getProvince() + address.getCity() + address.getRegions() + address.getAddress());
                    } else {
                        order_addressmanage.setVisibility(View.GONE);
                        order_noneaddress.setVisibility(View.VISIBLE);
                    }
                    goods = orderDateil.getGoods();
                    if (goods.size() != 0) {
                        adapter.setData(goods);
                        adapter.notifyDataSetChanged();
                    }
                    order_feright.setText(orderDateil.getFreight() + "");
                    order_amount.setText("￥" + orderDateil.getOrder_amount() + "");

                }
                dialog.dismiss();
                builder.dismiss();
            }
        });

    }


    private void initUI() {
        adapter = new orderDetailAdapter(this, goods, R.layout.order_detail_paid_item);
        goods_lv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == MyConstant.ADD_ADDRESS) {
            if (data != null) {
                address = (Address) data.getSerializableExtra("address");
                Sumit();

            }
        } else if (resultCode == MyConstant.SELECT_ADDRESS) {
            if (data != null) {
                address = (Address) data.getSerializableExtra("address");
            } else {
                address = null;
            }
            Sumit();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.order_break)
    public void goback() {
        finish();
    }

    @OnClick(R.id.tv_shopping_pay)
    public void PlaceOrder() {
        if (address == null) {
            new MyUtils().showToast(this, "请选择收货地址");
        } else {
            Gson gson = new GsonBuilder().create();
            orderDateil.setMember_id(user.getMemberid());
            orderDateil.setTerminal_code("android");
            String json = gson.toJson(orderDateil);
            Map<String, Object> map = new HashMap<>();
            map.put("data", json);
            new OkHttpUtil().post(DataUrl.URL_CartPlaceOrder, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject obj = jsonObject.getJSONObject("data");
                        int code = obj.getInt("code");
                        int order_id = obj.getInt("order_id");
                        String result = obj.getString("result");
                        if (code == 1) {
                            Intent intent = new Intent(OrderDetailActivity.this, PayActivity.class);
                            intent.putExtra("order_id", order_id);
                            startActivity(intent);
                            finish();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    @OnClick(R.id.order_none)

    public void gotoAddaddress() {
        Intent intent = new Intent(OrderDetailActivity.this, AddressManageActivity.class);
        intent.putExtra("from", "order");
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.order_manage)
    public void gotoaddressmanage() {
        Intent intent = new Intent(OrderDetailActivity.this, AddressManageActivity.class);
        intent.putExtra("from", "order");
        startActivityForResult(intent, 0);
    }

    class orderDetailAdapter extends SuperAdapter<OrderGoods> {

        public orderDetailAdapter(Context context, List<OrderGoods> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, OrderGoods item, int position) {

            ImageView good_thumb = helper.getImageView(R.id.order_detail_item_iv);
            GlideUtils.loadImage(OrderDetailActivity.this, item.getThumb_url(), R.drawable.loading_classify, good_thumb);
            helper.setText(R.id.order_detail_name, item.getTitle());
            helper.setText(R.id.order_detail_price, "￥" + item.getGood_price());
            helper.setText(R.id.order_good_number, "X" + item.getGood_number());
            LinearLayout logistics_ll = helper.getView(R.id.logistics_ll);
            logistics_ll.setVisibility(View.GONE);


        }
    }

}
