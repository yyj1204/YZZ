package com.example.cm.juyiz.ui.activity;
/*
个人中心 --- 分享二维码界面
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.util.GlideUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineShareActivity extends BaseActivity {

    @Bind(R.id.iv_mineshare_code)
    ImageView iv_sharecode;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_share);
        ButterKnife.bind(this);//初始化bind库
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        initUI();
    }

    /*
 初始化控件
  */
    private void initUI() {
        if ("".equals(url) || url != null) {
            GlideUtils.loadIntoUseFitWidth(this, url, R.drawable.loading_product, iv_sharecode);
        }
    }

    /*
普通点击事件
*/
    @OnClick(R.id.iv_mineshare_back)
    public void OnClick(View view) {
        finish();
    }
}
