package com.example.cm.juyiz;
/*
强制竖屏 & 沉浸式状态栏
*/
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.zhy.autolayout.config.AutoLayoutConifg;

import static com.example.cm.juyiz.util.SystemBar.initSystemBar;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        AutoLayoutConifg.getInstance().useDeviceSize();
        initSystemBar(this);
    }
}
