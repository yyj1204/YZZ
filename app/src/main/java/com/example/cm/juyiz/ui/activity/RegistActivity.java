package com.example.cm.juyiz.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.MyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity {

    /*编辑框*/
    @Bind(R.id.et_regist_user)//用户名
    EditText et_user;
    @Bind(R.id.et_regist_pwd)//密码
    EditText et_pwd;
    @Bind(R.id.et_regist_cpwd)//确认密码
    EditText et_cpwd;
    @Bind(R.id.et_regist_phone)//手机号
    EditText et_phone;
    @Bind(R.id.et_regist_code)//验证码
    EditText et_code;
    @Bind(R.id.et_regist_region)//地区
    EditText et_region;
    @Bind(R.id.et_regist_address)//地址
    EditText et_address;

    @Bind(R.id.tv_regist_getcode)//获取验证码
    TextView tv_getcode;
    @Bind(R.id.iv_regist_agreement)
    ImageView iv_agreement;
    @Bind(R.id.tv_regist_agreement)//同意协议
    TextView tv_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);//初始化bind库
        initUI();
        initData();
    }

    /*
初始化数据
*/
    private void initData() {
    }

    /*
 初始化控件
  */
    private void initUI() {

        //协议---两字颜色设置
        int length = tv_agreement.getText().toString().length();//文本总长度
        SpannableStringBuilder builder = new SpannableStringBuilder(tv_agreement.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#64ADE1"));
        builder.setSpan(redSpan,length-6,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_agreement.setText(builder);
    }

    /*
   普通点击事件
    */
    @OnClick({R.id.iv_regist_back,R.id.tv_regist_getcode,R.id.iv_regist_agreement,R.id.tv_regist_agreement,R.id.tv_regist_regist})
    public void OnClick(View view){
        switch (view.getId())
        {
            case R.id.iv_regist_back://返回
                finish();
                break;
            case R.id.tv_regist_getcode://获取验证码
                Toast.makeText(this,"已获取验证码！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_regist_agreement://同意注册协议
                iv_agreement.setSelected(! iv_agreement.isSelected());
                break;
            case R.id.tv_regist_agreement://注册协议
                Toast.makeText(this,"已查看注册协议！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_regist_regist://忘记密码
                regist();
                break;
            default:
                break;
        }
    }

    private void regist() {
        String userString = et_user.getText().toString();
        String pwdString = et_pwd.getText().toString();
        String cpwdString = et_cpwd.getText().toString();
        String phoneString = et_phone.getText().toString();
        String codeString = et_code.getText().toString();
        String regionString = et_region.getText().toString();
        String addressString = et_address.getText().toString();

        if (userString == null || userString.equals("")) { // 用户名为空时
            myToast("请输入用户名");
        } else if (pwdString == null || pwdString.equals("")) {// 密码为空时
            myToast("请输入密码");
        } else if (cpwdString == null || cpwdString.equals("")) {// 确认密码为空时
            myToast("请输入确认密码");
        } else if (phoneString == null || phoneString.equals("")) {// 手机号为空时
            myToast("请输入手机号");
        } else if (codeString == null || codeString.equals("")) {// 验证码为空时
            myToast("请输入验证码");
        } else if (regionString == null || regionString.equals("")) {// 地区信息为空时
            myToast("请输入地区信息");
        } else if (addressString == null || addressString.equals("")) {// 详细地址为空时
            myToast("请输入详细地址");
        }else {// 全都不为空时
            myToast("注册成功");
        }
    }

    /*
    自定义弹窗
     */
    private void myToast(String toastString){
        MyToast myToast = MyToast.makeText(RegistActivity.this, toastString, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }
}
