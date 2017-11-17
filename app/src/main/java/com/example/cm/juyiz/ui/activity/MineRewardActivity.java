package com.example.cm.juyiz.ui.activity;
/*
个人中心 --- 奖励界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineRewardActivity extends BaseActivity {

    @Bind(R.id.linear_minereward_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_minereward)
    AutoLinearLayout linear_reward;
    @Bind(R.id.tv_minereward_money)//可提现金额
            TextView tv_money;
    @Bind(R.id.tv_minereward_totalmoney)//累计提现金额
            TextView tv_totalmoney;
    @Bind(R.id.tv_minereward_apply)//申请提现&&实名认证
            TextView tv_apply;

    private Member user;
    private int is_auth;//是否身份认证
    private float money;//可提现金额

    @Override
    protected void onStart() {
        super.onStart();
        connecteNet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_reward);
        ButterKnife.bind(this);//初始化bind库
        user = LoginUtil.getinit().getMember();
        is_auth = user.getIs_auth();
        connecteNet();
    }

    /*
  判断是否有网络
   */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_reward.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_reward.setVisibility(View.GONE);
        }
    }

    /*
初始化数据
*/
    private void initData() {
        String url = "";
        if (user != null) {
            url = DataUrl.URL_ShareReward + "?id=" + user.getMemberid();
        }
        new OkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject obj = object.getJSONObject("data");
                    String balance = obj.getString("balance");//可提现金额
                    money = Float.parseFloat(balance);
                    tv_money.setText(money + "");
                    tv_totalmoney.setText(obj.getString("share_reward_amount"));
                    is_auth = obj.getInt("is_auth");
                    if (is_auth == 0) {
                        tv_apply.setText("实名认证后可提现");
                    } else if (is_auth == 1) {
                        tv_apply.setText("申请提现");
                    }
                    user.setIs_auth(is_auth);
                    LoginUtil.getinit().saveMember(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /*
  普通点击事件
   */
    @OnClick({R.id.iv_minereward_back, R.id.linear_minereward_totalmoney, R.id.linear_minereward_withdrawal, R.id.tv_minereward_apply, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_minereward_back://返回
                finish();
                break;
            case R.id.linear_minereward_totalmoney://累计提现金额
                startActivity(new Intent(this, MineSalesActivity.class));
                break;
            case R.id.linear_minereward_withdrawal://提现记录
                startActivity(new Intent(this, RewardRecordActivity.class));
                break;
            case R.id.tv_minereward_apply://申请提现&&实名认证
                if (is_auth == 0) {//未认证打开认证界面
                    Intent intent = new Intent(this, RewardCertificationActivity.class);
                    startActivityForResult(intent, 0);
                } else if (is_auth == 1) {//已认证打开申请提现
                    Intent intent = new Intent(this, RewardApplyActivity.class);
                    intent.putExtra("apply", money);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int is_via = data.getIntExtra("is_via", 0);
            is_auth = is_via;
            tv_apply.setText("申请提现");
        }
    }
}
