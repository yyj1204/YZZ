package com.example.cm.juyiz.util;
/**
 * 自定义工具类
 */

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;


import com.example.cm.juyiz.customwidget.MyToast;
import com.example.cm.juyiz.ui.activity.ClassifyActivity;
import com.example.cm.juyiz.ui.activity.ClassifyBrandActivity;
import com.example.cm.juyiz.ui.activity.CustomActivity;
import com.example.cm.juyiz.ui.activity.DetailsActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {

    /**
     * 自定义弹窗
     */
    public void showToast(Context context, String toastString) {
        MyToast myToast = MyToast.makeText(context, toastString, Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }

    /**
     * 验证手机号码格式
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern
                    .compile("^((1[0-9])\\d{9})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 首页：轮播图&热点信息&广告图点击事件
     * link_type指定链接类型，一共分为四种：
     * 1.custom自定义页，2.good商品详情页，3.brand品牌，4.class_sub小分类。
     * link_id指定链接ID，对应链接类型。
     */
    public void startActivityType(Context context, String link_type, int link_id) {
        switch (link_type) {
            case "custom":
                Intent intent1 = new Intent(context, CustomActivity.class);
                intent1.putExtra(MyConstant.KEY_Custom, link_id);
                context.startActivity(intent1);
                break;
            case "good":
                Intent intent2 = new Intent(context, DetailsActivity.class);
                intent2.putExtra(MyConstant.KEY_Details, link_id);
                context.startActivity(intent2);
                break;
            case "brand":
                Intent intent3 = new Intent(context, ClassifyBrandActivity.class);
                intent3.putExtra(MyConstant.KEY_Brand, link_id);
                context.startActivity(intent3);
                break;
            case "class_sub":
                Intent intent4 = new Intent(context, ClassifyActivity.class);
                intent4.putExtra(MyConstant.KEY_Classify, link_id);
                context.startActivity(intent4);
                break;
        }
    }

    /*
    防止按钮频繁点击
     */
    private static long lastClickTime;

    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 2500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
