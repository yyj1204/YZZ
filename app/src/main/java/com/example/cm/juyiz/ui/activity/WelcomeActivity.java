package com.example.cm.juyiz.ui.activity;
/*
商城欢迎界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决按home键，再次点击程序图标重启问题
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(new MyRunnable(), 2000);
    }

    /*
    打开主界面
     */
    class MyRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
