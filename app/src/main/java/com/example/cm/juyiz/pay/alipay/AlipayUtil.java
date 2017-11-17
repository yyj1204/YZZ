package com.example.cm.juyiz.pay.alipay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.cm.juyiz.alipay.AuthResult;
import com.example.cm.juyiz.alipay.PayResult;
import com.example.cm.juyiz.pay.PayView;
import com.example.cm.juyiz.ui.activity.PayActivity;
import com.example.cm.juyiz.util.MyConstant;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class AlipayUtil {
    private Context context;
    private PayView payView;

    public AlipayUtil(Context context, PayView payView) {
        this.context = context;
        this.payView = payView;
    }

    public AlipayUtil(Context context) {
        this.context = context;
    }

    public Handler getalipayHandler() {
        @SuppressLint("HandlerLeak")
        Handler mHandler = new Handler() {
            @SuppressWarnings("unused")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MyConstant.SDK_PAY_FLAG: {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                            payView.alipaySuccess();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                            payView.alipayFailed();
                        }
                        break;
                    }
                    case MyConstant.SDK_AUTH_FLAG: {
                        @SuppressWarnings("unchecked")
                        AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                        String resultStatus = authResult.getResultStatus();

                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                            Toast.makeText(context,
                                    "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            // 其他状态值则为授权失败
                            Toast.makeText(context,
                                    "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        return mHandler;
    }

}
