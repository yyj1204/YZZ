package com.example.cm.juyiz.ui.activity;
/*
个人中心 --- 绑定推荐者对话框
*/

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

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

public class BindDialogActivity extends BaseActivity {

    @Bind(R.id.et_bind)
    EditText et_bind;

    private MyUtils myUtils;
    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_dialog);
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();
        user = LoginUtil.getinit().getMember();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        myUtils.showToast(this, "很遗憾您绑定失败！");
        return super.onTouchEvent(event);
    }

    /*
     普通点击事件
      */
    @OnClick(R.id.tv_bind_sure)
    public void OnClick(View view) {
        if (myUtils.isFastClick()) {
            return;
        }
        String phoneString = et_bind.getText().toString();
        if (TextUtils.isEmpty(phoneString)) { // 会员ID或手机号为空时
            myUtils.showToast(this, "请输入会员ID或手机号");
        } else {// 全都不为空时
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getMemberid());
            map.put("from", phoneString);
            new OkHttpUtil().post(DataUrl.URL_SetFrom, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    JsonUtil jsonUtil = new JsonUtil();
                    Type type = new TypeToken<Result>() {
                    }.getType();
                    Result result = (Result) jsonUtil.analysis2Objiect(data, type);
                    if (result.getCode() == 1) {
                        myUtils.showToast(BindDialogActivity.this, result.getResult());
                        setResult(10);
                        finish();
                    }
                }
            });
        }
    }
}
