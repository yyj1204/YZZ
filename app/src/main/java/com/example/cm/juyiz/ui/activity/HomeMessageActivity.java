package com.example.cm.juyiz.ui.activity;
/*
首页 --- 我的消息界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.MessageBean;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeMessageActivity extends BaseActivity {

    @Bind(R.id.tv_message_reward)//奖励
            TextView tv_reward;
    @Bind(R.id.tv_reward_time)
    TextView tv_reward_time;
    @Bind(R.id.tv_message_fans)//粉丝
            TextView tv_fans;
    @Bind(R.id.tv_fans_time)
    TextView tv_fans_time;

    private Member user;

    private List<MessageBean> messageBeens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_message);
        ButterKnife.bind(this);//初始化bind库
        //user = MemberUtils.getUser(APP.getContext());
        user = LoginUtil.getinit().getMember();
        if (ConnectionUtils.isConnected(this)) {
            getMessageBean();
        } else {
            new MyUtils().showToast(this, "请重新检查网络");
        }
    }

    private void getMessageBean() {
        new OkHttpUtil().get(DataUrl.URL_Message + "?id=" + user.getMemberid(), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<MessageBean>>() {
                }.getType();
                messageBeens.clear();
                messageBeens = jsonUtil.analysis2List(data, type);
                for (MessageBean message : messageBeens) {
                    if ("info_reward".equals(message.getType())) {
                        tv_reward.setText(message.getOverview());
                        tv_reward_time.setText(message.getInfo_time());
                    } else if ("info_fans".equals(message.getType())) {
                        tv_fans.setText(message.getOverview());
                        tv_fans_time.setText(message.getInfo_time());
                    }
                }
            }
        });
    }

    /*
  普通点击事件
   */
    @OnClick({R.id.iv_message_back, R.id.linear_message_reward, R.id.linear_message_fans})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_message_back://返回
                finish();
                break;
            case R.id.linear_message_reward://奖励
                for (MessageBean message : messageBeens) {
                    if ("info_reward".equals(message.getType())) {
                        startActivity(new Intent(this, MessageRewardActivity.class));
                    }
                }
                break;
            case R.id.linear_message_fans://粉丝
                for (MessageBean message : messageBeens) {
                    if ("info_fans".equals(message.getType())) {
                        startActivity(new Intent(this, MessageFansActivity.class));
                    }
                }
                break;
            default:
                break;
        }
    }
}
