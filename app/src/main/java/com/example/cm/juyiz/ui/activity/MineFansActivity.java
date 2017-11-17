package com.example.cm.juyiz.ui.activity;
/*
个人中心 --- 粉丝界面
*/

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.MineFansBean;
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

public class MineFansActivity extends BaseActivity {

    @Bind(R.id.linear_minefans_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_mine_fans)
    AutoLinearLayout linear_fans;
    @Bind(R.id.tv_mine_notfans)//没粉丝
            TextView tv_notfans;

    @Bind(R.id.list_mine_fans)
    ListView list_mine_fans;

    private List<MineFansBean> mineFansBean = new ArrayList<>();
    private MineFansAdapter fansAdapter;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_fans);
        ButterKnife.bind(this);//初始化bind库
        //user = MemberUtils.getUser(APP.getContext());
        user = LoginUtil.getinit().getMember();
        initUI();
        connecteNet();
    }

    /*
初始化控件
 */
    private void initUI() {
        fansAdapter = new MineFansAdapter(this, mineFansBean, R.layout.item_mine_fans_list);
        list_mine_fans.setAdapter(fansAdapter);
    }

    /*
   判断是否有网络
    */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_fans.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_fans.setVisibility(View.GONE);
        }
    }

    /*
初始化数据
 */
    private void initData() {
        String url = "";
        if (user != null) {
            url = DataUrl.URL_ShareFans + "?id=" + user.getMemberid();
        }
        new OkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<MineFansBean>>() {
                }.getType();
                mineFansBean = jsonUtil.analysis2List(data, type);
                if (mineFansBean.size() != 0) {
                    tv_notfans.setVisibility(View.GONE);
                    list_mine_fans.setVisibility(View.VISIBLE);
                    fansAdapter.setData(mineFansBean);
                    fansAdapter.notifyDataSetChanged();
                } else {
                    tv_notfans.setVisibility(View.VISIBLE);
                    list_mine_fans.setVisibility(View.GONE);
                }
            }
        });
    }


    /*
粉丝消息适配器
 */
    class MineFansAdapter extends SuperAdapter<MineFansBean> {
        public MineFansAdapter(Context context, List<MineFansBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, MineFansBean item, int position) {
            String wechat_headimgur = item.getWechat_headimgur();
            String wechat_nickname = item.getWechat_nickname();
            String mobile = item.getMobile();
            //微信头像
            ImageView iv_head = helper.getView(R.id.iv_minefans_head);
            if (wechat_headimgur != null) {
                GlideUtils.loadCircleImage(MineFansActivity.this, wechat_headimgur, iv_head);
            } else {
                iv_head.setImageResource(R.drawable.message_fans);
            }
            //用户名
            if (!wechat_nickname.equals("")) {
                helper.setText(R.id.tv_minefans_name, "用户名：" + wechat_nickname);
            } else {
                helper.setText(R.id.tv_minefans_name, "用户名：" + mobile);
            }
            helper.setText(R.id.tv_minefans_time, "关注时间：" + item.getFrom_time());
        }
    }

    /*
  普通点击事件
   */
    @OnClick({R.id.iv_minefans_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_minefans_back://返回
                finish();
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
        }
    }
}
