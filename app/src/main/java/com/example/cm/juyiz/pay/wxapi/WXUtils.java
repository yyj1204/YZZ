package com.example.cm.juyiz.pay.wxapi;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;


import com.example.cm.juyiz.pay.wxapi.util.MD5;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class WXUtils {
    private Context context;
    StringBuffer sb;
    private String out_trade_no;
    private String total_fee;
    private String body;
    private IWXAPI api;

    public WXUtils(Context context, String out_trade_no, String total_fee, String body) {
        sb = new StringBuffer();
        this.context = context;
        this.out_trade_no = out_trade_no;
        this.total_fee = total_fee;
        this.body = body;
    }

    /**
     * 获取ip
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    private String getWifiIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 签名工具
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(MyConstant.WX_APP_SECRET);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成xml
     *
     * @return
     */
    public String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        String ip = getWifiIp();
        if (ip == "" && ip == "") {
            ip = getLocalIpAddress();
        }
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", MyConstant.WX_APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", MyConstant.WX_PARTNER_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", MyConstant.NOTIFY_URL));
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);
            return xmlstring;

        } catch (Exception e) {
            Log.e("TAG", "fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e("csm", sb.toString());
        return sb.toString();
    }


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Log.e("csm", e.toString());
        }
        return null;
    }

    private Map<String, String> retmap = new HashMap<>();

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(MyConstant.WX_APP_SECRET);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("csm", appSign);
        return appSign;
    }

    private void genPayReq() {
        req.appId = MyConstant.WX_APP_ID;
        req.partnerId = MyConstant.WX_PARTNER_ID;
        req.prepayId = retmap.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sb.append("sign\n" + req.sign + "\n\n");
        Log.e("csm", signParams.toString());
    }

    private void sendPayReq() {
        api.registerApp(MyConstant.WX_APP_ID);
        api.sendReq(req);
    }

    private PayReq req = new PayReq();

    public Map<String, String> PlaceOrder() {
        String str = genProductArgs();
        new OkHttpUtil().postJson("https://api.mch.weixin.qq.com/pay/unifiedorder", str, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                retmap = decodeXml(data);
                Log.e("csm", data);
                try {
                    //从后台获取返回的参数
                    // 发起支付。
                    api = WXAPIFactory.createWXAPI(context, MyConstant.WX_APP_ID);
                    genPayReq();
                    sendPayReq();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请安装微信最新版！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return retmap;
    }

}
