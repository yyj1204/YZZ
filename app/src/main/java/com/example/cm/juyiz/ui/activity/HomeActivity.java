package com.example.cm.juyiz.ui.activity;
/*
商城首页界面
*/

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.CartProductBean;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyFragmentPagerAdapter;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.MyViewPager;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog;
import com.qiangxi.checkupdatelibrary.dialog.UpdateDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.cm.juyiz.util.SystemBar.initSystemBar;
import static com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog.FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE;
import static com.qiangxi.checkupdatelibrary.dialog.UpdateDialog.UPDATE_DIALOG_PERMISSION_REQUEST_CODE;

public class HomeActivity extends AutoLayoutActivity {
    //片段
    @Bind(R.id.vp_main)
    MyViewPager vp_main;

    //四个tab的图标
    @Bind(R.id.iv_tab_home)
    ImageView iv_home;
    @Bind(R.id.iv_tab_classify)
    ImageView iv_classify;
    @Bind(R.id.iv_tab_shopping)
    ImageView iv_shopping;
    @Bind(R.id.iv_tab_mine)
    ImageView iv_mine;


    //购物车右上角数量
    @Bind(R.id.tv_tab_shopping_num)
    TextView tv_shopping_num;

    private MyFragmentPagerAdapter mAdapter;

    private boolean isExit;
    private Handler handler = new Handler();

    private Member user;
    ForceUpdateDialog updatedialog;
    UpdateDialog dialog;


    private CheckUpdateInfo mCheckUpdateInfo;
    private MyBroadcastReciver myBroadcastReciver;//广播
    private int productSize;//购物车数量

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            String id = user.getMemberid();
            new OkHttpUtil().get(DataUrl.URL_Cart + id, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    JsonUtil jsonUtil = new JsonUtil();
                    Type listType = new TypeToken<List<CartProductBean>>() {
                    }.getType();
                    List<CartProductBean> productBean = jsonUtil.analysis2List(data, listType);
                    productSize = productBean.size();
                    if (productSize != 0) {
                        tv_shopping_num.setVisibility(View.VISIBLE);
                        if (productSize > 99) {
                            tv_shopping_num.setText("99+");
                        } else {
                            tv_shopping_num.setText(productSize + "");
                        }
                    } else {
                        tv_shopping_num.setVisibility(View.GONE);
                    }
                }
            });
        }else {
            tv_shopping_num.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);//初始化bind库
        initSystemBar(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        //user = MemberUtils.getUser(APP.getContext());
        user = LoginUtil.getinit().getMember();
        initView();
        initData();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yzz.action.broadcast");
        myBroadcastReciver = new MyBroadcastReciver();
        this.registerReceiver(myBroadcastReciver, intentFilter);
    }

    //注销广播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReciver);
    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_type", "android");
        new OkHttpUtil().post(DataUrl.URL_AppVersionUpdate, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    JSONObject apk = object.getJSONObject("data");
                    mCheckUpdateInfo = new CheckUpdateInfo();
                    mCheckUpdateInfo.setAppName("圆滋滋")
                            .setIsForceUpdate(apk.getInt("is_update"))//设置是否强制更新,该方法的参数只要和服务端商定好什么数字代表强制更新即可
                            .setNewAppReleaseTime(apk.getString("add_time"))//软件发布时间
                            .setNewAppSize((float) apk.getDouble("file_size"))//单位为M
                            .setNewAppUrl(apk.getString("down_link"))
                            .setNewAppVersionCode(apk.getInt("version_id"))//新app的VersionCode
                            .setNewAppVersionName(apk.getString("version"))
                            .setNewAppUpdateDesc(apk.getString("update_intro"));
                    // .setNewAppUpdateDesc("1,优化下载逻辑\n2,修复一些bug\n3,完全实现强制更新与非强制更新逻辑\n4,非强制更新状态下进行下载,默认在后台进行下载\n5,当下载成功时,会在通知栏显示一个通知,点击该通知,进入安装应用界面\n6,当下载失败时,会在通知栏显示一个通知,点击该通知,会重新下载该应用\n7,当下载中,会在通知栏显示实时下载进度,但前提要dialog.setShowProgress(true).");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //有更新,显示dialog等
                if (mCheckUpdateInfo.getNewAppVersionCode() > getVersionCode(HomeActivity.this)) {
                    //强制更新,这里用0表示强制更新,实际情况中可与服务端商定什么数字代表强制更新即可
                    if (mCheckUpdateInfo.getIsForceUpdate() == 1) {
                        //show ForceUpdateDialog
                        forceUpdateDialogClick(new View(HomeActivity.this));
                    }
                    //非强制更新
                    else {
                        //show UpdateDialog
                        UpdateDialogClick(new View(HomeActivity.this));
                    }
                } else {
                    //无更新,提示已是最新版等
                }
            }
        });
    }

    /**
     * 若要兼容6.0系统,则需要重写此方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意所请求的权限
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //UPDATE_DIALOG_PERMISSION_REQUEST_CODE和FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE这两个常量是library中定义好的
            //所以在进行判断时,必须要结合这两个常量进行判断.
            //非强制更新对话框
            if (requestCode == UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                //进行下载操作
                dialog.download();
            }
            //强制更新对话框
            else if (requestCode == FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                //进行下载操作
                updatedialog.download();
            }
        } else {
            //用户不同意,提示用户,如下载失败,因为您拒绝了相关权限
            Toast.makeText(this, "some description...", Toast.LENGTH_SHORT).show();
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Log.e("tag", "false.请开启读写sd卡权限,不然无法正常工作");
//            } else {
//                Log.e("tag", "true.请开启读写sd卡权限,不然无法正常工作");
//            }

        }
    }

    /**
     * 强制更新
     */
    public void forceUpdateDialogClick(View view) {
        mCheckUpdateInfo.setIsForceUpdate(1);
        if (mCheckUpdateInfo.getIsForceUpdate() == 1) {
            updatedialog = new ForceUpdateDialog(HomeActivity.this);
            updatedialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                    .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                    .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                    .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                    .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                    .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                    .setFileName("yzz_android.apk")
                    .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
        }
    }

    /**
     * 非强制更新
     */
    public void UpdateDialogClick(View view) {
        mCheckUpdateInfo.setIsForceUpdate(0);
        if (mCheckUpdateInfo.getIsForceUpdate() == 0) {
            dialog = new UpdateDialog(HomeActivity.this);
            dialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                    .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                    .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                    .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                    .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                    .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                    .setFileName("yzz_android.apk")
                    .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib")
                    .setShowProgress(true)
                    .setIconResId(R.mipmap.ic_launcher)
                    .setAppName(mCheckUpdateInfo.getAppName()).show();
        }
    }

    /**
     * 获取当前应用版本号
     */
    private int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int ver_id = packageInfo.versionCode;
            return ver_id;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
    初始化控件
     */
    private void initView() {
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        iv_home.setSelected(true);
        vp_main.setOffscreenPageLimit(4);
        vp_main.setAdapter(mAdapter);
        vp_main.setNoScroll(true);
        vp_main.setCurrentItem(0);
    }

    /*
  tab点击事件监听
   */
    @OnClick({R.id.linear_tab_home, R.id.linear_tab_classify, R.id.rela_tab_shopping, R.id.linear_tab_mine})
    public void ChooseInterface(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.linear_tab_home://商城首页
                tabSelected(iv_home, 0);
                break;
            case R.id.linear_tab_classify://分类
                tabSelected(iv_classify, 1);
                break;
            case R.id.rela_tab_shopping://购物车
                if (user != null) {
                    tabSelected(iv_shopping, 2);
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
            case R.id.linear_tab_mine://个人中心
                if (user != null) {
                    tabSelected(iv_mine, 3);
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    //设置底部选中状态
    private void tabSelected(ImageView iv_tab, int viewpageItem) {
        iv_home.setSelected(false);
        iv_classify.setSelected(false);
        iv_shopping.setSelected(false);
        iv_mine.setSelected(false);

        iv_tab.setSelected(true);
        vp_main.setCurrentItem(viewpageItem);
    }

    //TODO 系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                finish();
            } else {//第一次点击返回
                isExit = true;
                new MyUtils().showToast(this, "再按一次退出圆滋滋商城！");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //判断2秒之内是否有再次点击返回
                        isExit = false;
                    }
                }, 2000);
            }
        }
        return true;
    }

    /*
    接收购物车的广播
     */
    class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.yzz.action.broadcast")) {
                productSize = intent.getIntExtra("product", 0);
                if (productSize != 0) {
                    tv_shopping_num.setVisibility(View.VISIBLE);
                    if (productSize > 99) {
                        tv_shopping_num.setText("99+");
                    } else {
                        tv_shopping_num.setText(productSize + "");
                    }
                } else {
                    tv_shopping_num.setVisibility(View.GONE);
                }
            }
        }
    }

}
