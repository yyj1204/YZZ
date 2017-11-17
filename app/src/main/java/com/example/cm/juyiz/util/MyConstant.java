package com.example.cm.juyiz.util;

import com.example.cm.juyiz.R;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public interface MyConstant {

    /*
    MainActivity的Tab图片与标题
     */
    int[] MAIN_TAB_IMG = {R.drawable.tab_main_home, R.drawable.tab_main_goods, R.drawable.tab_main_shopping, R.drawable.tab_main_mine};
    String[] MAIN_TAB_TV = {"商城首页", "分类", "购物车", "个人中心"};

    //广告栏pager数量
    int PAGER_COUNT = 10000;
    //相同布局的片段需要复用，传递过去的key
    String FRAGMENT_KEY = "position";

    //主界面Fragment打开商品详情界面---请求码
    int REQUSET_TODETAILS1 = 0;
    int REQUSET_TODETAILS2 = 1;
    //商品详情界面打开主界面Fragment---结果码
    int RESULT_FINISH1 = 0;
    int RESULT_TOHOME1 = 0;
    int RESULT_TOSHOPPING1 = 0;

    //个人中心  订单按钮打开订单界面的key
    String KEY_ORDER = "order";

    /**
     * key
     * 1.custom自定义页，2.good商品详情页，3.brand品牌，4.classify小分类,5.hotsale超值热卖,6.除首页外其他类型片段
     */
    String KEY_Custom = "custom";
    String KEY_Details = "details";
    String KEY_Brand = "brand";
    String KEY_Classify = "classify";
    String KEY_Hotsale = "hotsale";
    String KEY_Homemenu = "homemenu";

    /**
     * 添加编辑地址返回码
     */
    int ADD_ADDRESS = 1000;
    int EDIT_ADDRESS = 1001;
    int SELECT_ADDRESS = 1002;

    /**
     * 支付宝支付结果
     */
    int SDK_PAY_FLAG = 1;
    int SDK_AUTH_FLAG = 2;

    /**
     * 微信支付
     */
    //微信APPId
    String WX_APP_ID = "wxf1d790a5e356a7a6";
    //微信商户Id
    String WX_PARTNER_ID = "1465533302";
    //API密钥
    String WX_APP_SECRET = "sqbmgmry5RoiKlEM0KR4VbbJxwc6WuwJ";
    //回调地址
    String NOTIFY_URL = "http://juyiz.com/testfu/addons/ewei_shopv2/payment/wechat/notify.php";
}
