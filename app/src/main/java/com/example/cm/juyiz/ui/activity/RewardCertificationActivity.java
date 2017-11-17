package com.example.cm.juyiz.ui.activity;
/*
奖励 --- 实名认证界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RewardCertificationActivity extends BaseActivity {

    /*编辑框*/
    @Bind(R.id.et_certification_wx)//微信号
            EditText et_wx;
    @Bind(R.id.et_certification_name)//真实姓名
            EditText et_name;
    @Bind(R.id.et_certification_IDnumber)//身份证号
            EditText et_IDnumber;
    @Bind(R.id.et_certification_alipay)//支付宝账号
            EditText et_alipay;
    @Bind(R.id.et_certification_phone)//手机号
            EditText et_phone;
    @Bind(R.id.et_certification_bankcard)//银行卡号
            EditText et_bankcard;

    private MyUtils myUtils;//自定义工具类
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_certification);
        ButterKnife.bind(this);//初化bind库
        myUtils = new MyUtils();
        // user = MemberUtils.getUser(APP.getContext());
        user = LoginUtil.getinit().getMember();
    }

    /*
  普通点击事件
   */
    @OnClick({R.id.iv_certification_back, R.id.tv_certification_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_certification_back://返回
                myUtils.showToast(this, "已取消认证");
                finish();
                break;
            case R.id.tv_certification_submit://提交
                if (myUtils.isFastClick()){
                    return;
                }
                submit();
                break;
            default:
                break;
        }
    }

    /*
    提交认证资料
     */
    private void submit() {
        String wxString = et_wx.getText().toString();
        String nameString = et_name.getText().toString();
        String IDString = et_IDnumber.getText().toString();
        String alipayString = et_alipay.getText().toString();
        String phoneString = et_phone.getText().toString();
        String bankcardString = et_bankcard.getText().toString();

        if (TextUtils.isEmpty(wxString)) { // 微信号为空时
            myUtils.showToast(this, "请输入微信号");
        } else if (TextUtils.isEmpty(nameString)) {// 真实姓名为空时
            myUtils.showToast(this, "请输入真实姓名");
        } else if (TextUtils.isEmpty(IDString)) {// 身份证号为空时
            myUtils.showToast(this, "请输入身份证号");
        } else if (TextUtils.isEmpty(alipayString)) {// 支付宝账号为空时
            myUtils.showToast(this, "请输入支付宝账号");
        } else if (TextUtils.isEmpty(phoneString)) {// 手机号为空时
            myUtils.showToast(this, "请输入手机号");
        } else if (!MyUtils.checkMobileNumber(phoneString)) {// 手机格式不正确时
            myUtils.showToast(this, "请输入正确的手机号！");
        } else if (TextUtils.isEmpty(bankcardString)) {// 银行卡号为空时
            myUtils.showToast(this, "请输入银行卡号");
        } else if (!ConnectionUtils.isConnected(this)) {//没网络时
            myUtils.showToast(this, "请重新检查网络");
        } else {// 全都不为空时
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getMemberid());
            map.put("wechat_account", wxString);
            map.put("name", nameString);
            map.put("id_card", IDString);
            map.put("alipay_account", alipayString);
            map.put("mobile", phoneString);
            map.put("bank_id", bankcardString);
            new OkHttpUtil().post(DataUrl.URL_MemberAuth, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject json = new JSONObject(data);
                        JSONObject data1 = json.getJSONObject("data");
                        if (data1 != null) {
                            String result = data1.getString("result");
                            int is_via = data1.getInt("is_via");
                            if (is_via == 1) {//认证成功：返回个人中心---奖励界面，将认证结果传递回去
                                myUtils.showToast(RewardCertificationActivity.this, "实名认证成功");
                                Intent intent = new Intent(RewardCertificationActivity.this, MineRewardActivity.class);
                                intent.putExtra("is_via", is_via);
                                user.setIs_auth(is_via);
                                LoginUtil.getinit().saveMember(user);
                                setResult(0, intent);
                                finish();
                            } else {
                                myUtils.showToast(RewardCertificationActivity.this, "信息填写错误");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
