package com.example.cm.juyiz.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class RefundesDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refundes_detail);
        for (int i = 0; i < 4; i++) {
            LayoutInflater inflater = LayoutInflater.from(RefundesDetailActivity.this);
            // 获取需要被添加控件的布局
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.goods_lv);
            // 获取需要添加的布局
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.order_refundes_detail_item, null);
            // 将布局加入到当前布局中
            linearLayout.addView(layout);
        }

    }
}
