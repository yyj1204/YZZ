package com.example.cm.juyiz.pay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cm.juyiz.pay.PayView;
import com.example.cm.juyiz.util.MyConstant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    private PayView payView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, MyConstant.WX_APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * arg0。errCode  0成功 -1支付失败 -2取消
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.e("csm", resp.errStr);

        if (resp.errCode == 0) {//支付成
            Intent intent = new Intent();
            intent.setAction("fbPayAction");
//          intent.setAction("goodsPayAction");
//          intent.setAction("integraPayAction");
            sendBroadcast(intent);
            Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
            payView.wxSuccess();
            finish();
        } else if (resp.errCode == -1) {//支付失败
            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
            payView.wxFailed();
        } else {//取消
            Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}
