package com.example.cm.juyiz.util;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public interface DataUrl {


    String URL1 = "http://yuanzhizhi.com/Api/Index/";
    /*
    商城首页
     */
    //顶部菜单✔
    String URL_HomeList = URL1 + "HomeList";
    //首页菜单轮播图✔
    String URL_HomeCarousel = URL1 + "HomeCarousel";
    //首页热点信息数据✔
    String URL_HomeHotspots = URL1 + "HomeHotspots";
    //首页五个活动数据✔
    String URL_HomePromotion = URL1 + "HomePromotion";
    //首页广告图✔
    String URL_HomeAds = URL1 + "HomeAds";
    //首页超值热卖✔
    String URL_HomeHotSale = URL1 + "HomeSale";
    //超值热卖商品✔
    String URL_HotSale = URL1 + "HomeSaleDetails?id=";
    //除首页以外其他菜单数据✔
    String URL_HomeMenuDetails = URL1 + "HomeListDetails?id=";
    //除首页以外其他菜单轮播图✔
    String URL_HomeMenuCarousel = URL1 + "HomeListCarousel?id=";
    //消息✔
    String URL_Message = URL1 + "Info";
    //奖励消息
    String URL_MessageReward = URL1 + "InfoReward";
    //粉丝消息✔
    String URL_MessageFans = URL1 + "InfoFans";
    //搜索结果
    String URL_SearchGoods = URL1 + "SearchGoods";
    //限时特价时间
    String URL_FlashSaleTime = URL1 + "FlashSale";
    //限时特价商品
    String URL_FlashSaleGoods = URL1 + "FlashSaleGoods?id=";
    //聚推荐
    String URL_Recommend = URL1 + "Recommend";
    //最新发布
    String URL_NewList = URL1 + "NewList";
    //最新发布顶部海报图(**)
    String URL_NewListImg = URL1 + "NewListImg";
    //获取验证码✔(**)
    String URL_IdentifyingCodeGet = URL1 + "IdentifyingCodeGet";
    //检验验证码、、登录接口✔(**)
    String URL_IdentifyingCodeCheck = URL1 + "IdentifyingCodeCheck";
    //分享粉丝✔
    String URL_ShareFans = URL1 + "ShareFans";
    //绑定推荐者✔
    String URL_SetFrom = URL1 + "SetFrom";
    //商品详情✔
    String URL_Goods = URL1 + "Goods";
    //商品详情推荐商品✔
    String URL_GoodsRecommend = URL1 + "GoodsRecommend";
    //排行榜✔
    String URL_RankingList = URL1 + "RankingList?day=";
    /*
    商城分类
     */
//大分类
    String URL_ClassBig = URL1 + "ClassBig";
    //大分类轮播图
    String URL_ClassCarousel = URL1 + "ClassCarousel?id=";
    //小分类
    String URL_ClassSub = URL1 + "ClassSub?id=";
    //品牌
    String URL_ClassBrand = URL1 + "Brand?id=";
    //小分类---详情
    String URL_ClassSubDetails = URL1 + "ClassSubDetails?id=";
    //品牌详情---轮播图
    String URL_BrandCarousel = URL1 + "BrandCarousel?id=";
    //品牌详情---商品
    String URL_BrandDetails = URL1 + "BrandDetails?id=";

    //更新接口✔
    String URL_AppVersionUpdate = URL1 + "AppVersionUpdate";

    //实名认证(**)
    String URL_MemberAuth = URL1 + "MemberAuth";
    //个人中心
    String URL_Member = URL1 + "Member";
    //分享奖励(**)
    String URL_ShareReward = URL1 + "ShareReward";

    //申请提现
    String URL_MemberCash = URL1 + "MemberCash";
    //申请提现记录
    String URL_MemberCashRecord = URL1 + "MemberCashRecord?id=";
    //获取地址
    String URL_AddressGet = URL1 + "AddressGet";
    //设置默认地址
    String URL_AddressDefault = URL1 + "AddressDefault";
    //编辑地址
    String URL_AddressUpdate = URL1 + "AddressUpdate";
    //添加地址(**)
    String URL_AddressAdd = URL1 + "AddressAdd";
    //删除地址
    String URL_AddressDelete = URL1 + "AddressDelete";
    //销售额
    String URL_ShareSales = URL1 + "ShareSales?id=";
    //服务协议(**)
    String URL_ServicesProtocols = URL1 + "ServicesProtocols";
    /*
    购物车系列接口
     */
    String URL2 = "http://yuanzhizhi.com/Api/Buy/";
    //加入购物车
    String URL_CartAdd = URL2 + "CartAdd";
    //购物车---商品展示
    String URL_Cart = URL2 + "Cart?id=";
    //购物车---增加
    String URL_CartNumberAdd = URL2 + "CartNumberAdd?cart_id=";
    //购物车---减少
    String URL_CartNumberDecrease = URL2 + "CartNumberDecrease?cart_id=";
    //购物车---删除
    String URL_CartDelete = URL2 + "CartDelete?cart_id=";
    //购物车--提交订单/结算
    String URL_CartSettlement = URL2 + "CartSettlement";
    //购物车-- 去支付/下单
    String URL_CartPlaceOrder = URL2 + "CartPlaceOrder";

    String URL3 = "http://yuanzhizhi.com/Api/Pay/";
    //获取支付方式
    String URL_PaymentMethodGet = URL3 + "PaymentMethodGet";
}
