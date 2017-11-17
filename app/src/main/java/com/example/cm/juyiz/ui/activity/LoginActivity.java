package com.example.cm.juyiz.ui.activity;
/*
用户登录界面
*/

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.LoginUtil;

import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_login_phone)//手机号
            EditText et_phone;
    @Bind(R.id.et_login_code)//验证码
            EditText et_code;

    @Bind(R.id.tv_login_getcode)//获取验证码
            TextView tv_getcode;
    @Bind(R.id.iv_login_agreement)
    ImageView iv_agreement;
    @Bind(R.id.tv_login_agreement)//同意协议
            TextView tv_agreement;

    private String phoneString;
    private String codeString;
    private MyUtils myUtils;//自定义工具类

    private Member user;


    private int time = 60;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tv_getcode.setText("重新发送(" + time + ")");
            } else if (msg.what == -8) {
                tv_getcode.setText("获取验证中");
                tv_getcode.setClickable(true);
                time = 60;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);//初始化bind库

        user = LoginUtil.getinit().getMember();
        myUtils = new MyUtils();
        initUI();
    }

    /*
初始化控件
 */
    private void initUI() {
        //协议---两字颜色设置
        int length = tv_agreement.getText().toString().length();//文本总长度
        SpannableStringBuilder builder = new SpannableStringBuilder(tv_agreement.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#64ADE1"));
        builder.setSpan(redSpan, length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_agreement.setText(builder);
    }

    /*
   普通点击事件
    */
    @OnClick({R.id.iv_login_back, R.id.tv_login_getcode, R.id.iv_login_agreement, R.id.tv_login_agreement, R.id.tv_login_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_back://返回
                finish();
                break;
            case R.id.iv_login_agreement://同意协议
                iv_agreement.setSelected(!iv_agreement.isSelected());
                break;
            case R.id.tv_login_agreement://打开服务协议
                Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_login://登录
                login();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_login_getcode)
    public void getCode() {
        String mobile = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) { // 手机号为空时
            myUtils.showToast(this, "请输入手机号！");
            return;
        } else if (!MyUtils.checkMobileNumber(mobile)) {// 手机格式不正确时
            myUtils.showToast(this, "请输入正确的手机号！");
            return;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("mobile", mobile);
            new OkHttpUtil().post(DataUrl.URL_IdentifyingCodeGet, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject urldata = jsonObject.getJSONObject("data");
                        String result = urldata.getString("result");
                        if ("already_exists".equals(result)) {
                            //已存在
                            myUtils.showToast(LoginActivity.this, "验证码已发送！");
                        } else if ("fail_in_send".equals(result)) {
                            //失败
                            myUtils.showToast(LoginActivity.this, "验证码发送失败！");
                        } else if ("send_successful".equals(result)) {
                            //成功
                            myUtils.showToast(LoginActivity.this, "验证码已发送！");


                            tv_getcode.setClickable(false);
                            tv_getcode.setText("再次输入倒计时" + "(" + time + ")");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (; time > 0; time--) {
                                        handler.sendEmptyMessage(-9);
                                        if (time <= 0) {
                                            break;
                                        }
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    handler.sendEmptyMessage(-8);
                                }
                            }).start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String msg) {
                }
            });
        }
    }

    /*
    登录
     */
    private void login() {
        phoneString = et_phone.getText().toString();
        codeString = et_code.getText().toString();
        if (TextUtils.isEmpty(phoneString)) { // 手机号为空时
            myUtils.showToast(this, "请输入手机号！");
        } else if (TextUtils.isEmpty(codeString)) {// 验证码为空时
            myUtils.showToast(this, "请输入验证码！");
        } else if (!iv_agreement.isSelected()) {// 未同意服务协议时
            myUtils.showToast(this, "请先阅读协议！");
        } else {// 都不为空时
            Map<String, Object> map = new HashMap<>();
            map.put("mobile", phoneString);
            map.put("code", codeString);

            new OkHttpUtil().post(DataUrl.URL_IdentifyingCodeCheck, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    Log.e("login", data);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        JSONObject urldata = jsonObject.getJSONObject("data");
                        String result = urldata.getString("result");
                        if ("code_error".equals(result)) {
                            //已存在
                            myUtils.showToast(LoginActivity.this, "验证码错误！");
                        } else if ("login_successful".equals(result)) {
                            //成功
                            if (user != null) {
                                user.setMobile(phoneString);
                                user.setMemberid(urldata.getString("id"));
                            } else {
                                user = new Member();
                                user.setMobile(phoneString);
                                user.setMemberid(urldata.getString("id"));
                            }
                            LoginUtil.getinit().saveMember(user);

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            myUtils.showToast(LoginActivity.this, "登录成功！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String msg) {
                }
            });
        }
    }


    class LoginTask extends AsyncTask<Integer, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Integer... params) {
            return null;
        }
    }


}
