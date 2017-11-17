package com.example.cm.juyiz.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.example.cm.juyiz.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 状态栏区域作为内容区域
 * 依赖:
 *compile 'com.readystatesoftware.systembartint:systembartint:1.0.3'
 * Activity根布局：
 *  android:clipToPadding="true"
 *  android:fitsSystemWindows="true"
 */

public class SystemBar {
    public static void initSystemBar(Activity activity) {
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.black);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
