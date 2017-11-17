package com.example.cm.juyiz.ui.fragment;
/*
购物车片段
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.CartProductBean;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.ui.activity.DetailsActivity;
import com.example.cm.juyiz.ui.activity.OrderDetailActivity;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingFragment extends Fragment {
    private List<CartProductBean> productBean;
    private List<CartProductBean> selectProduct = new ArrayList<>();
    private View view;

    @Bind(R.id.linear_shopping_notnet)//没网络布局
            LinearLayout linear_notnet;
    @Bind(R.id.linear_shopping)//有商品---总布局
            LinearLayout linear_shopping;
    @Bind(R.id.rela_shopping_nothing)//没有商品---总布局
            RelativeLayout rela_nothing;
    @Bind(R.id.line_shopping)//有商品---总布局
            LinearLayout linear_product;

    @Bind(R.id.tv_shopping_time)//倒计时
            TextView tv_countDown;
    @Bind(R.id.iv_shopping_all)//全选图标
            ImageView iv_selected_all;
    @Bind(R.id.tv_shopping_price)//总计价格
            TextView tv_price_total;

    @Bind(R.id.list_shopping)
    ListView list;

    private int countDown = 1;//倒计时时间
    private boolean isSelectAll;//是否全选
    private int itemSelectNum = 0;//子控件选中个数
    private float selectMoney;//选中总金额
    private ListAdapter listAdapter;
    private MyUtils myUtils;//自定义工具类
    private Member user;
    private boolean isItemClick = true;//子控件是否点击
    private int itemClickType = 0;//子控件的点击类型
    private final int TYPE_SUB = 1;//减少
    private final int TYPE_ADD = 2;//增加
    private final int TYPE_DELETE = 3;//删除

    public ShoppingFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        connecteNet();
        itemSelectNum = 0;
        isSelectAll = false;
        iv_selected_all.setSelected(isSelectAll);
        selectMoney = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_shopping, container, false);
            ButterKnife.bind(this, view);//初始化bind库
            myUtils = new MyUtils();
            user = LoginUtil.getinit().getMember();
            initUI();
            connecteNet();
        }
        return view;
    }


    /*
    判断是否有网络
     */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(getActivity());
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_shopping.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_shopping.setVisibility(View.GONE);
        }
    }

    /*
  初始化数据
   */
    private void initData() {
        if (user != null) {
            new OkHttpUtil().get(DataUrl.URL_Cart + user.getMemberid(), new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    JsonUtil jsonUtil = new JsonUtil();
                    Type listType = new TypeToken<List<CartProductBean>>() {
                    }.getType();
                    productBean = jsonUtil.analysis2List(data, listType);
                    if (productBean.size() != 0) {
                        rela_nothing.setVisibility(View.GONE);
                        linear_product.setVisibility(View.VISIBLE);
                    } else {
                        rela_nothing.setVisibility(View.VISIBLE);
                        linear_product.setVisibility(View.GONE);
                    }
                    listAdapter.setData(productBean);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /*
   初始化控件
    */
    private void initUI() {
        setCountDown();//倒计时

        isSelectAll = iv_selected_all.isSelected();//全选按钮

        listAdapter = new ListAdapter(getContext(), productBean, R.layout.item_shopping_list);
        list.setAdapter(this.listAdapter);

        //TODO ListView子控件点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                CartProductBean product = productBean.get(i);
                int good_id = product.getGood_id();
                intent.putExtra(MyConstant.KEY_Details, good_id);
                getActivity().startActivityForResult(intent, MyConstant.REQUSET_TODETAILS2);
            }
        });
    }

    //TODO 设计倒计时---暂不用
    private void setCountDown() {
        new CountDownTimer(countDown * 60000, 1000) {
            @Override
            public void onTick(long l) {
                int minute = (int) (l / 60000);//分
                int second = (int) ((l % 60000) / 1000);//秒
                StringBuffer sb = new StringBuffer();
                if (minute < 10) {
                    sb.append("0" + minute);
                } else {
                    sb.append(minute);
                }
                if (second < 10) {
                    sb.append(":0" + second);
                } else {
                    sb.append(":" + second);
                }
                tv_countDown.setText(sb.toString());
            }

            @Override
            public void onFinish() {
                tv_countDown.setText("00:00");
            }
        }.start();
    }

    /*
    普通点击事件
     */
    @OnClick({R.id.line_shopping_all, R.id.tv_shopping_pay, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.line_shopping_all://全选
                selectAll();
                break;
            case R.id.tv_shopping_pay:
                if (itemSelectNum == 0) {
                    myUtils.showToast(getActivity(), "请选择购买商品!");
                } else {//TODO 提交订单还未处理
                    String cart_id = "";
                    for (int i = 0; i < selectProduct.size(); i++) {
                        String product = selectProduct.get(i).getId() + "";
                        if (i < selectProduct.size() - 1) {
                            cart_id = cart_id + product + ",";
                        } else {
                            cart_id = cart_id + product;
                        }
                    }
                    Log.e("cart_id", cart_id);
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("cart_id", cart_id);
                    startActivity(intent);
                }
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
            default:
                break;
        }
    }

    //TODO 全选按钮
    private void selectAll() {
        isSelectAll = !isSelectAll;
        //如果全选--->取消全选，子控件选中数量为：0，总金额为：0，图标为：未选中状态
        if (!isSelectAll) {
            itemSelectNum = 0;
            selectMoney = 0;
            selectProduct.clear();
        } else { //如果未全选--->全选，子控件选中数量为：全部子控件的数量，总金额为：全部商品的总金额，图标为：选中状态
            itemSelectNum = productBean.size();
            //总金额
            selectMoney = 0;
            for (int i = 0; i < productBean.size(); i++) {
                CartProductBean product = productBean.get(i);
                float good_price = product.getGood_price();
                int good_number = product.getGood_number();
                for (int j = 1; j <= good_number; j++) {
                    BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                    BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                    selectMoney = b2.add(b1).floatValue();
                }
            }
            selectProduct.addAll(productBean);
        }
        tv_price_total.setText("¥" + selectMoney);
        iv_selected_all.setSelected(isSelectAll);
        for (int i = 0; i < productBean.size(); i++) {
            productBean.get(i).setSelected(isSelectAll);
            listAdapter.notifyDataSetChanged();
        }
    }

    /*
   ListView适配器
    */
    class ListAdapter extends SuperAdapter<CartProductBean> {
        public ListAdapter(Context context, List<CartProductBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final CartProductBean item, final int position) {
            String thumb_url = item.getThumb_url();//图片
            String title = item.getTitle();//商品名
            final int is_change = item.getIs_change();//价格是否发生改变
            final float good_price = item.getGood_price();//价格
            final int num = item.getGood_number();//购买数量
            final boolean selected = item.isSelected();//是否选中

            helper.setImageResource(R.id.iv_shopping_list, R.drawable.loading_classify, thumb_url);
            helper.setText(R.id.tv_shopping_list_name, title);
            helper.setText(R.id.tv_shopping_list_price, "¥" + good_price);
            //价格发生变化控件
            TextView tv_change = helper.getView(R.id.tv_shopping_list_change);
            if (is_change == 0) {
                tv_change.setVisibility(View.GONE);
            } else {
                tv_change.setVisibility(View.VISIBLE);
                tv_change.setText("价格已改变！");
            }
            final TextView tv_num = helper.getView(R.id.tv_shopping_list_num);
            final ImageView iv_selected = helper.getView(R.id.iv_shopping_list_all);
            //购买数量
            tv_num.setText(num + "");

            //是否选中---1.初始化item选中状态
            iv_selected.setSelected(selected);
            //2.点击事件
            iv_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick(item);
                }
            });

            //减 --- 总金额减去当前商品的价格
            helper.getView(R.id.iv_shopping_list_sub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decreaseProduct(item);
                }
            });

            //加 --- 总金额加上当前商品的价格
            helper.getView(R.id.iv_shopping_list_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct(item);
                }
            });
            //删 --- 总金额减去当前商品的价格*数量
            helper.getView(R.id.iv_shopping_list_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteProduct(item, position);
                }
            });
            tv_price_total.setText("¥" + selectMoney);
        }
    }

    //商品---选中
    private void itemClick(CartProductBean item) {
        boolean selected = item.isSelected();
        int num = item.getGood_number();
        float good_price = item.getGood_price();
        if (!selected) {//选中，选中数量+1，总计价格+，
            itemSelectNum++;
            for (int i = 1; i <= num; i++) {
                BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                selectMoney = b1.add(b2).floatValue();
            }
            selectProduct.add(item);
        } else {//取消选中，选中数量-1，总计价格-，
            itemSelectNum--;
            for (int i = 1; i <= num; i++) {
                BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                selectMoney = b2.subtract(b1).floatValue();
            }
            selectProduct.remove(item);
        }
        //3选中数量与总数量相等，全选
        if (itemSelectNum == productBean.size()) {
            iv_selected_all.setSelected(true);
        } else {
            iv_selected_all.setSelected(false);
        }
        item.setSelected(!selected);
        listAdapter.notifyDataSetChanged();
    }

    //商品---减少
    private void decreaseProduct(final CartProductBean item) {
        if (itemClickType != TYPE_SUB) {
            itemClickType = TYPE_SUB;
            isItemClick = true;
        }
        final int num = item.getGood_number();
        final float good_price = item.getGood_price();
        final boolean selected = item.isSelected();

        if (isItemClick) {
            isItemClick = false;
            if (ConnectionUtils.isConnected(getActivity())) {//有网络
                new OkHttpUtil().get(DataUrl.URL_CartNumberDecrease + item.getId(), new OkHttpUtil.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject json = new JSONObject(data);
                            int ret = json.getInt("ret");
                            if (ret == 200) {//请求成功
                                JSONObject data1 = json.getJSONObject("data");
                                int code = data1.getInt("code");
                                final String result = data1.getString("result");
                                if (code == 1) {//减少成功
                                    item.setGood_number(num - 1);
                                    if (selected) {
                                        BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                                        BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                                        selectMoney = b2.subtract(b1).floatValue();
                                    }
                                    listAdapter.notifyDataSetChanged();
                                    isItemClick = true;
                                } else {//减少失败，延迟按钮点击
                                    isItemClick = false;
                                    myUtils.showToast(getActivity(), result);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isItemClick = true;
                                        }
                                    }, 2500);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {//没网络
                isItemClick = false;
                myUtils.showToast(getActivity(), "请重新加载网络");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isItemClick = true;
                    }
                }, 2500);
            }
        }
    }

    //商品---增加
    private void addProduct(final CartProductBean item) {
        if (itemClickType != TYPE_ADD) {
            itemClickType = TYPE_ADD;
            isItemClick = true;
        }
        final int num = item.getGood_number();
        final float good_price = item.getGood_price();
        final boolean selected = item.isSelected();
        if (isItemClick) {
            isItemClick = false;
            if (ConnectionUtils.isConnected(getActivity())) {//有网络
                new OkHttpUtil().get(DataUrl.URL_CartNumberAdd + item.getId(), new OkHttpUtil.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject json = new JSONObject(data);
                            int ret = json.getInt("ret");
                            if (ret == 200) {//请求成功
                                JSONObject data1 = json.getJSONObject("data");
                                int code = data1.getInt("code");
                                final String result = data1.getString("result");
                                if (code == 1) {//增加成功
                                    item.setGood_number(num + 1);
                                    if (selected) {
                                        BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                                        BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                                        selectMoney = b2.add(b1).floatValue();
                                    }
                                    listAdapter.notifyDataSetChanged();
                                    isItemClick = true;
                                } else {//增加失败，延迟按钮点击
                                    isItemClick = false;
                                    myUtils.showToast(getActivity(), result);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isItemClick = true;
                                        }
                                    }, 2500);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {//没网络
                isItemClick = false;
                myUtils.showToast(getActivity(), "请重新加载网络");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isItemClick = true;
                    }
                }, 2500);
            }
        }
    }

    //商品---删除
    private void deleteProduct(final CartProductBean item, final int position) {
        if (itemClickType != TYPE_DELETE) {
            itemClickType = TYPE_DELETE;
            isItemClick = true;
        }
        final int num = item.getGood_number();
        final float good_price = item.getGood_price();
        final boolean selected = item.isSelected();
        if (isItemClick) {
            isItemClick = false;
            if (ConnectionUtils.isConnected(getActivity())) {//有网络
                new OkHttpUtil().get(DataUrl.URL_CartDelete + item.getId(), new OkHttpUtil.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject json = new JSONObject(data);
                            int ret = json.getInt("ret");
                            if (ret == 200) {//请求成功
                                JSONObject data1 = json.getJSONObject("data");
                                int code = data1.getInt("code");
                                String result = data1.getString("result");
                                if (code == 1) {//删除成功
                                    productBean.remove(position);
                                    //1.删除后数据不为0的话，逻辑如下，为0的话，全选状态为false，商品总价格为0
                                    if (productBean.size() == 0) {
                                        rela_nothing.setVisibility(View.VISIBLE);
                                        linear_product.setVisibility(View.GONE);
                                        tv_price_total.setText("¥0");
                                        iv_selected_all.setSelected(false);
                                    } else {//如果该删除的商品是被选中  选中数量减一，总计商品价格减 价格*数量
                                        if (selected) {
                                            itemSelectNum--;
                                            for (int i = 1; i <= num; i++) {
                                                BigDecimal b1 = new BigDecimal(Float.toString(good_price));
                                                BigDecimal b2 = new BigDecimal(Float.toString(selectMoney));
                                                selectMoney = b2.subtract(b1).floatValue();
                                            }
                                        }
                                        //2.选中数量与总数量相等，全选
                                        if (itemSelectNum == productBean.size()) {
                                            iv_selected_all.setSelected(true);
                                        } else {
                                            iv_selected_all.setSelected(false);
                                        }
                                    }
                                    listAdapter.notifyDataSetChanged();
                                    isItemClick = true;

                                    //3.发送广播---通知首界面更新购物车图标数量
                                    Intent intent = new Intent();
                                    intent.setAction("com.yzz.action.broadcast");
                                    //要发送的内容---购物车数量
                                    intent.putExtra("product", productBean.size());
                                    getActivity().sendBroadcast(intent);
                                } else {//删除失败，延迟按钮点击
                                    isItemClick = false;
                                    myUtils.showToast(getActivity(), result);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isItemClick = true;
                                        }
                                    }, 2500);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {//没网络
                isItemClick = false;
                myUtils.showToast(getActivity(), "请重新加载网络");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isItemClick = true;
                    }
                }, 2500);
            }
        }
    }

}
