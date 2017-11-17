package com.example.cm.juyiz.ui.activity;
/*
奖励 --- 提现申请界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.Result;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RewardApplyActivity extends BaseActivity {

    @Bind(R.id.tv_rewardapply_money)//可提现金额
            TextView tv_money;
    @Bind(R.id.et_rewardapply_money)//要提现金额
            EditText et_money;

    private Member user;
    private MyUtils myUtils;//自定义工具类
    private float balance;
    private float money;//提现金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_apply);
        ButterKnife.bind(this);//初始化bind库
        user = LoginUtil.getinit().getMember();
        myUtils = new MyUtils();
        //个人中心---奖励传递的可提现金额
        balance = getIntent().getFloatExtra("apply", 0);
        initUI();
    }

    /*
 初始化控件
  */
    private void initUI() {
        tv_money.setText(balance + "");
        //限制输入框小数点后只能两位
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    //判断小数点的位置大于倒3，将输入框的字符串截取到小数点后两位数
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_money.setText(s);
                        et_money.setSelection(s.length());
                    }
                }

                //判断字符串的第一位是小数点，则在小数点前面加个0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_money.setText(s);
                    et_money.setSelection(2);
                }

                //判断字符串第一位是0
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    //如果第二位不是小数点，限制不能输入
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_money.setText(s.subSequence(0, 1));
                        et_money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /*
  普通点击事件
   */
    @OnClick({R.id.iv_apply_back, R.id.tv_rewardapply_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply_back://返回
                finish();
                break;
            case R.id.tv_rewardapply_submit://提交
                if (myUtils.isFastClick()) {
                    return;
                }
                submit();
                break;
            default:
                break;
        }
    }

    /*
    申请提现
     */
    private void submit() {
        String moneyString = et_money.getText().toString();
        try {
            money = Float.parseFloat(moneyString);
            if (TextUtils.isEmpty(moneyString)) { // 提现金额为空时
                myUtils.showToast(this, "请输入提现金额");
            } else if (balance == 0) {// 可提现金额为0
                myUtils.showToast(this, "当前金额为0，无法提现");
            } else if (money > 5000) {// 超出可提现额度
                myUtils.showToast(this, "最大提现额度为5000");
            } else if (money > balance & money < 5000) {// 超出可提现金额
                myUtils.showToast(this, "已超出可提现金额");
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getMemberid());
                map.put("cash_amount", moneyString);
                new OkHttpUtil().post(DataUrl.URL_MemberCash, map, new OkHttpUtil.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        JsonUtil jsonUtil = new JsonUtil();
                        Type type = new TypeToken<Result>() {
                        }.getType();
                        Result result = (Result) jsonUtil.analysis2Objiect(data, type);
                        if (result != null) {
                            if (result.getCode() == 1) {
                                startActivity(new Intent(RewardApplyActivity.this, RewardRecordActivity.class));
                                finish();
                            } else {
                                myUtils.showToast(RewardApplyActivity.this, result.getResult());
                            }
                        }
                    }
                });
            }
        } catch (NumberFormatException ex) {
        }
    }
}
