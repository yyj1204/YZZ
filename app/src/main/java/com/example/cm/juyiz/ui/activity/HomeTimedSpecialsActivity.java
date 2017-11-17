package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 1.限时特价界面
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.FlashSaleBean;
import com.example.cm.juyiz.bean.FlashSaleTimeBean;
import com.example.cm.juyiz.bean.Member;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeTimedSpecialsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private List<FlashSaleTimeBean> timeBean = new ArrayList<>();//时间
    private List<FlashSaleBean> productBean = new ArrayList<>();//商品

    @Bind(R.id.linear_timed_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_timed)//总布局（包含了时间+列表+没活动图片）
            AutoLinearLayout linear_timed;
    @Bind(R.id.linear_timed_sub)
    AutoLinearLayout linear_timed_sub;
    @Bind(R.id.iv_timed_notactivity)//全部没活动
            ImageView iv_timed_notactivity;
    @Bind(R.id.iv_notactivity)//部分时间段没活动
            ImageView iv_notactivity;
    @Bind(R.id.rg_timed)
    RadioGroup rg_timed;
    @Bind(R.id.rb_timed1)
    RadioButton rb_timed1;
    @Bind(R.id.rb_timed2)
    RadioButton rb_timed2;
    @Bind(R.id.rb_timed3)
    RadioButton rb_timed3;
    @Bind(R.id.rb_timed4)
    RadioButton rb_timed4;
    @Bind(R.id.rb_timed5)
    RadioButton rb_timed5;

    @Bind(R.id.list_timed)
    ListView list_timed;

    private MyUtils myUtils;//自定义工具类

    private TimedSpecialsAdapter timedSpecialsAdapter;
    private long systemTime;//当前系统时间

    //使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            systemTime = systemTime + 1;
            handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);
            timedSpecialsAdapter.notifyDataSetChanged();
        }
    };
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timed_specials);
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();
        user = LoginUtil.getinit().getMember();
        initView();
        connecteNet();
        initData();
    }

    /*
    判断是否有网络
     */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_timed.setVisibility(View.VISIBLE);
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_timed.setVisibility(View.GONE);
        }
    }

    /*
初始化数据
*/
    private void initData() {
        //抢购时间
        new OkHttpUtil().get(DataUrl.URL_FlashSaleTime, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<FlashSaleTimeBean>>() {
                }.getType();
                timeBean = jsonUtil.analysis2List(data, type);
                if (timeBean.size() != 0) {
                    iv_timed_notactivity.setVisibility(View.GONE);
                    linear_timed_sub.setVisibility(View.VISIBLE);
                    for (int i = 0; i < timeBean.size(); i++) {
                        FlashSaleTimeBean time = timeBean.get(i);
                        if (time.getStart_time() > systemTime) {
                            timeBean.get(i).setFlash_type(2);//活动未开始
                        } else if (time.getStart_time() <= systemTime && time.getStop_time() > systemTime) {
                            timeBean.get(i).setFlash_type(1);//抢购中
                            initProductData(time.getId(), 1, time.getStop_time());
                        } else if (time.getStop_time() < systemTime) {
                            timeBean.get(i).setFlash_type(0);//活动结束
                        }
                    }
                    setRadio();
                } else {
                    iv_timed_notactivity.setVisibility(View.VISIBLE);
                    linear_timed_sub.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    初始化商品数据
    1.Id:抢购时间id
    2.saleCase:抢购时间段
      0：结束，1：在抢购，2：即将开始
    3.stopTime：活动结束时间
     */
    private void initProductData(int Id, final int saleCase, final long stopTime) {
        new OkHttpUtil().get(DataUrl.URL_FlashSaleGoods + Id, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<FlashSaleBean>>() {
                }.getType();
                productBean = jsonUtil.analysis2List(data, type);
                if (productBean.size() != 0) {
                    iv_notactivity.setVisibility(View.GONE);
                    list_timed.setVisibility(View.VISIBLE);
                    for (int i = 0; i < productBean.size(); i++) {
                        productBean.get(i).setStatus_id(i % 3);
                        productBean.get(i).setSaleCase(saleCase);
                        productBean.get(i).setStopTime(stopTime);
                    }
                    timedSpecialsAdapter.setData(productBean);
                    timedSpecialsAdapter.notifyDataSetChanged();
                } else {
                    iv_notactivity.setVisibility(View.VISIBLE);
                    list_timed.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    初始化抢购时间控件
     */
    private void setRadio() {
        //判断在抢购时间段，RadioButton选中
        if (timeBean.get(0).getFlash_type() == 1) {
            rb_timed1.setSelected(true);
        } else if (timeBean.get(1).getFlash_type() == 1) {
            rb_timed2.setSelected(true);
        } else if (timeBean.get(2).getFlash_type() == 1) {
            rb_timed3.setSelected(true);
        } else if (timeBean.get(3).getFlash_type() == 1) {
            rb_timed4.setSelected(true);
        } else if (timeBean.get(4).getFlash_type() == 1) {
            rb_timed5.setSelected(true);
        } else {//如果都还没开始，默认选中第一个抢购时间段
            rb_timed1.setSelected(true);
            initProductData(timeBean.get(0).getId(), 2, timeBean.get(0).getStop_time());
        }
        //显示抢购活动的各个时间段
        rb_timed1.setText(getTimeFormat(timeBean.get(0).getStart_time()));
        rb_timed2.setText(getTimeFormat(timeBean.get(1).getStart_time()));
        rb_timed3.setText(getTimeFormat(timeBean.get(2).getStart_time()));
        rb_timed4.setText(getTimeFormat(timeBean.get(3).getStart_time()));
        rb_timed5.setText(getTimeFormat(timeBean.get(4).getStart_time()));
    }

    /*
   初始化控件
    */
    private void initView() {
        systemTime = new Date().getTime() / 1000;
        rg_timed.setOnCheckedChangeListener(this);

        timedSpecialsAdapter = new TimedSpecialsAdapter(this, productBean, R.layout.item_timed_list);
        list_timed.setAdapter(timedSpecialsAdapter);
        list_timed.setFocusable(false);
        list_timed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(HomeTimedSpecialsActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details, productBean.get(position).getGood_id());
                startActivity(intent);
            }
        });
        handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);
    }

    /*
    RadioGroup:RadioButton点击事件
    */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        radioIsChecked(checkedId, rb_timed1, 0);
        radioIsChecked(checkedId, rb_timed2, 1);
        radioIsChecked(checkedId, rb_timed3, 2);
        radioIsChecked(checkedId, rb_timed4, 3);
        radioIsChecked(checkedId, rb_timed5, 4);
    }

    /*
    判断RadioButton是否选中
    position：RadioButton对应的时间段
     */
    public void radioIsChecked(int checkedId, RadioButton rb, int position) {
        if (checkedId == rb.getId()) {
            connecteNet();
            rb.setSelected(true);
            //flash_type：判断当前选中的RadioButton是否在抢购时间段
            //0抢购活动已结束，1在抢购时间段，2抢购活动未开始
            //如果在抢购时间段，则还可以抢购
            //如果不在抢购时间段，则显示活动结束或者活动未开始
            FlashSaleTimeBean flashSaleTimeBean = timeBean.get(position);
            int flash_type = flashSaleTimeBean.getFlash_type();
            long stopTime = flashSaleTimeBean.getStop_time();
            if (flash_type == 0) {
                initProductData(flashSaleTimeBean.getId(), 0, stopTime);
            } else if (flash_type == 1) {
                initProductData(flashSaleTimeBean.getId(), 1, stopTime);
            } else if (flash_type == 2) {
                initProductData(flashSaleTimeBean.getId(), 2, stopTime);
            }
        } else {
            rb.setSelected(false);
        }
    }

    /*
    限时抢购商品适配器
    */
    class TimedSpecialsAdapter extends SuperAdapter<FlashSaleBean> {
        public TimedSpecialsAdapter(Context context, List<FlashSaleBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final FlashSaleBean item, final int position) {
            helper.setImageResource(R.id.iv_timed_list, R.drawable.loading_product, item.getThumb_url());
            helper.setText(R.id.tv_timed_title, item.getTitle());//商品标题
            helper.setText(R.id.tv_timed_price, item.getPromotion_price() + "");//商品价格
            TextView tv_timed_original = helper.getView(R.id.tv_timed_original);//商品原价
            tv_timed_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线
            tv_timed_original.setText(" ¥ " + item.getOriginal_price() + " ");


            //立即抢购按钮&&倒计时控件
            final int saleCase = item.getSaleCase();
            long stopTime = item.getStopTime();
            final int status_id = item.getStatus_id();
            TextView tv_timed_countdown = helper.getView(R.id.tv_timed_countdown);//倒计时
            TextView tv_purchase = helper.getView(R.id.tv_timed_purchase);//抢购按钮
            setTv_case(saleCase, tv_purchase, stopTime - systemTime, tv_timed_countdown, status_id);//判断该按钮的状态
            tv_purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ConnectionUtils.isConnected(HomeTimedSpecialsActivity.this)) {//有网络
                        //TODO 如果该商品是立即抢购状态，点击才对应有事件发生
                        if (saleCase == 1 && status_id == 0) {
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
                                                    myUtils.showToast(HomeTimedSpecialsActivity.this, "已加入购物车");
                                                } else {
                                                    myUtils.showToast(HomeTimedSpecialsActivity.this, result);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {//未登录---进入登录界面
                                startActivity(new Intent(HomeTimedSpecialsActivity.this, LoginActivity.class));
                            }
                        } else {//没网络
                            myUtils.showToast(HomeTimedSpecialsActivity.this, "请重新加载网络");
                        }
                    }
                }
            });
        }
    }

    /*
    判断抢购按钮的三种状态
    1.saleCase:抢购时间段
      0：结束，1：在抢购，2：即将开始
    2.tv_case：抢购按钮
    3.remainTime：剩余抢购时间
    4：tv_countdown：显示倒计时控件
    5：status_id：商品销售状态
      0：可以购买，1：下架，2：售罄
     */
    private void setTv_case(int saleCase, TextView tv_case, long remainTime, TextView tv_countdown, int status_id) {
        switch (saleCase) {
            case 0:
                tv_case.setText("活动结束");
                tv_case.setBackgroundColor(Color.GRAY);
                tv_countdown.setText("");
                break;
            case 1:
                if (status_id == 0) {
                    tv_case.setText("立即抢购");
                    tv_case.setBackgroundColor(Color.RED);
                    updateTextView(remainTime, tv_countdown);
                } else if (status_id == 1) {
                    tv_case.setText("已下架");
                    tv_case.setBackgroundColor(Color.GRAY);
                    tv_countdown.setText("");
                } else {
                    tv_case.setText("已售罄");
                    tv_case.setBackgroundColor(Color.GRAY);
                    tv_countdown.setText("");
                }
                break;
            case 2:
                tv_case.setText("即将开抢");
                tv_case.setBackgroundColor(Color.RED);
                tv_countdown.setText("");
                break;
            default:
                break;
        }
        timedSpecialsAdapter.notifyDataSetChanged();
    }

    /****
     * 刷新倒计时控件
     * 1.times_remain：倒计时
     * 2.tv：倒计时控件
     */
    public void updateTextView(long times_remain, TextView tv) {
        StringBuffer sb = new StringBuffer();
        sb.append("剩余时间  ");
        //秒钟
        long time_second = times_remain % 60;
        //分钟
        long time_temp = (times_remain - time_second) / 60;
        long time_minute = time_temp % 60;
        //小时
        time_temp = (time_temp - time_minute) / 60;
        long time_hour = time_temp;
        if (time_hour > 0) {
            if (time_hour < 10) {
                sb.append("0" + time_hour + ":");
            } else {
                sb.append(time_hour + ":");
            }
        }
        if (time_minute < 10) {
            sb.append("0" + time_minute);
        } else {
            sb.append(time_minute);
        }
        if (time_second < 10) {
            sb.append(":0" + time_second);
        } else {
            sb.append(":" + time_second);
        }
        tv.setText(sb.toString());
        timedSpecialsAdapter.notifyDataSetChanged();
    }

    /*
    时间转换
     */
    private String getTimeFormat(long startTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date d1 = new Date(startTime * 1000);
        String timeString = format.format(d1);
        return timeString;
    }

    /*
 普通点击事件
  */
    @OnClick({R.id.iv_timed_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_timed_back:
                finish();
                break;
            case R.id.tv_againloading:
                connecteNet();
                break;
        }
    }

    //防止当前Activity结束以后,   倒计时handler依然继续循环浪费资源
    @Override
    protected void onDestroy() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
