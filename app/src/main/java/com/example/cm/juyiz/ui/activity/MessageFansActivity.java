package com.example.cm.juyiz.ui.activity;
/*
我的消息 --- 粉丝消息界面
*/

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.MessageFansBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageFansActivity extends BaseActivity {

    @Bind(R.id.linear_messagefans_notnet)//没网络布局
            AutoLinearLayout linear_notnet;
    @Bind(R.id.list_message_fans)
    ListView list_message_fans;

    private List<MessageFansBean> fansMessages = new ArrayList<>();
    private FansAdapter fansAdapter;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_fans);
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
            list_message_fans.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            list_message_fans.setVisibility(View.GONE);
        }
    }

    /*
 初始化数据
  */
    private void initData() {
        fansMessages.clear();
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getMemberid());
        new OkHttpUtil().post(DataUrl.URL_MessageFans, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<MessageFansBean>>() {
                }.getType();
                fansMessages = jsonUtil.analysis2List(data, type);
                fansAdapter.setData(fansMessages);
                fansAdapter.notifyDataSetChanged();
            }
        });
    }

    /*
 初始化控件
  */
    private void initUI() {
        fansAdapter = new FansAdapter(this, fansMessages, R.layout.item_message_fans_list);
        list_message_fans.setAdapter(fansAdapter);
    }

    /*
 粉丝消息适配器
  */
    class FansAdapter extends SuperAdapter<MessageFansBean> {
        public FansAdapter(Context context, List<MessageFansBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, MessageFansBean item, int position) {
            helper.setText(R.id.tv_messagefans_name, item.getDetails());
            helper.setText(R.id.tv_messagefans_time, item.getInfo_time());
        }
    }

    /*
普通点击事件
*/
    @OnClick({R.id.iv_messagefans_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_messagefans_back://返回
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
