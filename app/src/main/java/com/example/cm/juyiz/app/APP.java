package com.example.cm.juyiz.app;

import android.app.Application;
import android.content.Context;


import com.example.cm.juyiz.util.PreferenceUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class APP extends Application {
    private static APP App;
    //客服SDK是否已经初始化过了
    public static boolean isKFSDK = false;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        App = this;
        mContext = this;

        //初始化参数共享工具类
        PreferenceUtils.init(this);
        //设备的物理高度进行百分比化
        AutoLayoutConifg.getInstance().useDeviceSize();
        //客服初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.example.cm.juyiz.kf_moor.utils.FaceConversionUtil.getInstace().getFileText(
                        APP.getInstance());
            }
        }).start();
    }

    public static Context getContext() {
        return mContext;
    }

    public static APP getInstance() {
        return App;
    }
}
