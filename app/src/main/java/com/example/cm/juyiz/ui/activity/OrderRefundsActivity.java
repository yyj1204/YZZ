package com.example.cm.juyiz.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class OrderRefundsActivity extends BaseActivity {
    private int state;
    private RadioGroup refundes_type;
    private RadioButton refundes;
    private RadioButton returngoods;
    private LinearLayout reason;

    private ImageView refundes_img1;
    private ImageView refundes_img2;
    private ImageView refundes_img3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refunds);
        Intent intent = getIntent();
        state = intent.getIntExtra("state", 0);

        initUI();


        for (int i = 0; i < 4; i++) {
            LayoutInflater inflater = LayoutInflater.from(OrderRefundsActivity.this);
            // 获取需要被添加控件的布局
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.goods_lv);
            // 获取需要添加的布局
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.order_refundes_item, null);
            // 将布局加入到当前布局中
            linearLayout.addView(layout);
        }


    }

    private void initUI() {
        refundes_type = (RadioGroup) findViewById(R.id.refundes_type);
        refundes = (RadioButton) findViewById(R.id.refundes);
        reason = (LinearLayout) findViewById(R.id.refundes_reason);
        refundes_img1 = (ImageView) findViewById(R.id.refundes_img1);
        refundes_img2 = (ImageView) findViewById(R.id.refundes_img2);
        refundes_img3 = (ImageView) findViewById(R.id.refundes_img3);

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });


        refundes_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderRefundsActivity.this, "111", Toast.LENGTH_SHORT).show();
            }
        });
        refundes_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderRefundsActivity.this, "111", Toast.LENGTH_SHORT).show();
            }
        });
        refundes_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderRefundsActivity.this, "111", Toast.LENGTH_SHORT).show();
            }
        });

        refundes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundes.setTextColor(getResources().getColor(R.color.white));
                returngoods.setTextColor(getResources().getColor(R.color.black));
            }
        });

        returngoods = (RadioButton) findViewById(R.id.returngoods);
        returngoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundes.setTextColor(getResources().getColor(R.color.black));
                returngoods.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    String[] items = new String[]{"破损/丢件", "质量问题", "无理由退货", "正品质疑", "其他"};//性别选择

    /* 性别选择框 */
    private void showChooseDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择退货理由")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(OrderRefundsActivity.this, items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

}
