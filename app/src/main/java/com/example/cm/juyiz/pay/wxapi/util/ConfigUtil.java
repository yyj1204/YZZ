package com.example.cm.juyiz.pay.wxapi.util;

import com.example.cm.juyiz.util.MyConstant;

/**
 * @author wangkai
 * @2016年5月31日 下午8:37:21
 * @desc:微信支付相关配置信息
 */
public class ConfigUtil {
    /**
     * 服务号相关信息
     */
    public final static String APPID = MyConstant.WX_APP_ID;// 应用号
    public final static String APP_SECRECT = "";// 应用密码
    // public final static String TOKEN = "weixinCourse";//服务号的配置token
    public final static String MCH_ID = "";// 商户号 1218488501 公众号商户id

    public static final String PARTNER_ID = "";

    //
    public final static String API_KEY = "";// API密钥
    public final static String SIGN_TYPE = "MD5";// 签名加密方式
    public final static String TRADE_TYPE = "APP";// 支付类型

    // public final static String CERT_PATH =
    // "/Users/kevin/apiclient_cert.p12";//微信支付证书存放路径地址
    // 微信支付统一接口的回调
    public final static String NOTIFY_URL = "http://juyiz.com/testfu/addons/ewei_shopv2/payment/wechat/notify.php";
    // 微信支付成功支付后跳转的地址web端使用

    // 微信支付统一接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 微信退款接口(POST)
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 订单查询接口(POST)
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 关闭订单接口(POST)
    public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // 退款查询接口(POST)
    public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    // 接口调用上报接口(POST)
    public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
}
