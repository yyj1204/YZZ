package com.example.cm.juyiz.pay;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public interface PayView {

    //支付宝支付成功
    public void alipaySuccess();

    //支付宝支付失败
    public void alipayFailed();

    //微信支付成功
    public void wxSuccess();

    //微信支付失败
    public void wxFailed();
}
