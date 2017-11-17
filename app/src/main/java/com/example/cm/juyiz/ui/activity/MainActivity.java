package com.example.cm.juyiz.ui.activity;
/*
主界面
*/

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.ui.fragment.ClassifyFragment;
import com.example.cm.juyiz.ui.fragment.HomeFragment;
import com.example.cm.juyiz.ui.fragment.MineFragment;
import com.example.cm.juyiz.ui.fragment.ShoppingFragment;
import com.example.cm.juyiz.util.MyConstant;

import static com.example.cm.juyiz.util.SystemBar.initSystemBar;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private boolean isExit;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSystemBar(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        addTab(0,HomeFragment.class);
        addTab(1, ClassifyFragment.class);
        addTab(2, ShoppingFragment.class);
        addTab(3, MineFragment.class);
    }

    /*
    添加Tab结构
     */
    private void addTab(int index,Class clazz)
    {
        mTabHost.addTab(mTabHost.newTabSpec("" + index).setIndicator(getIndicator(index)),clazz, null);
    }

    /*
    给主界面的FragmentTabHost设置图片与标题
     */
    @SuppressLint("InflateParams")
    public View getIndicator(int index)
    {
        View view = getLayoutInflater().inflate(R.layout.item_main_tab, null);
        ImageView imgIndicator = (ImageView) view.findViewById(R.id.iv_main_indicator);
        TextView tvIndicator = (TextView) view.findViewById(R.id.tv_main_indicator);
        TextView tvShoppingIndicator = (TextView) view.findViewById(R.id.tv_mainshopping_indicator);
        imgIndicator.setImageResource(MyConstant.MAIN_TAB_IMG[index]);
        tvIndicator.setText(MyConstant.MAIN_TAB_TV[index]);
        if (index==2)
        {
            tvShoppingIndicator.setVisibility(View.VISIBLE);
            tvShoppingIndicator.setText("99+");
        }
        return view;
    }

    //TODO 系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isExit)
            {
                finish();
            }else {//第一次点击返回
                isExit = true;
                Toast.makeText(this, "再按一次退出聚亿折新系统", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //判断2秒之内是否有再次点击返回
                        isExit = false;
                    }
                }, 2000);
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //商品详情---首页&&购物车
        if (requestCode == MyConstant.REQUSET_TODETAILS1){
            if (resultCode==MyConstant.RESULT_FINISH1) {
                mTabHost.setCurrentTab(0);
            }else if (requestCode==MyConstant.RESULT_TOHOME1){
                mTabHost.setCurrentTab(0);
            }else if (requestCode==MyConstant.RESULT_TOSHOPPING1){
                mTabHost.setCurrentTab(2);
            }
        }else if (requestCode == MyConstant.REQUSET_TODETAILS2){
            if (resultCode==MyConstant.RESULT_FINISH1) {
                mTabHost.setCurrentTab(2);
            }else if (requestCode==MyConstant.RESULT_TOHOME1 ){
                mTabHost.setCurrentTab(0);
            }else if (requestCode==MyConstant.RESULT_TOSHOPPING1 ){
                mTabHost.setCurrentTab(2);
            }
        }
        int currentTab = mTabHost.getCurrentTab();
        Log.e("当前片段:----",currentTab+"");
    }
}
