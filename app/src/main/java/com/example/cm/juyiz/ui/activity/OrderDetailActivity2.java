package com.example.cm.juyiz.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.app.APP;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.kf_moor.chat.ChatActivity;
import com.example.cm.juyiz.kf_moor.chat.LoadingFragmentDialog;
import com.example.cm.juyiz.kf_moor.chat.PeerDialog;
import com.example.cm.juyiz.util.LoginUtil;
import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.InitListener;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.utils.NetUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class OrderDetailActivity2 extends BaseActivity {
    private LoadingFragmentDialog loadingDialog;

    private List<Integer> goods;
    private int content;

    @Bind(R.id.order_close)
    TextView order_close;
    @Bind(R.id.order_detail_refundes_tv)
    TextView order_pay;
    @Bind(R.id.contact_service)
    LinearLayout contact_service;

    private Member user;

    private boolean islogistics = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail2);
        ButterKnife.bind(this);
        user = LoginUtil.getinit().getMember();
        Intent intent = getIntent();
        content = intent.getIntExtra("content", -1);
        switch (content) {
            case 0:
                islogistics = false;
                order_close.setVisibility(View.VISIBLE);
                order_pay.setVisibility(View.VISIBLE);
                order_pay.setText("去付款");
                break;
            case 1:
                islogistics = false;
                order_close.setVisibility(View.GONE);
                order_pay.setVisibility(View.VISIBLE);
                order_pay.setText("退款");
                break;
            case 2:
                islogistics = true;
                order_close.setVisibility(View.GONE);
                order_pay.setVisibility(View.VISIBLE);
                order_pay.setText("退货退款");
                break;
            case 3:
                islogistics = true;
                order_close.setVisibility(View.GONE);
                order_pay.setVisibility(View.GONE);
                break;
            case 4:
                islogistics = true;
                order_close.setVisibility(View.GONE);
                order_pay.setVisibility(View.GONE);
                break;


        }


        initUI();
        loadingDialog = new LoadingFragmentDialog();
    }

    private void initUI() {

        goods = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            goods.add(i);
        }

        for (int i = 0; i < goods.size(); i++) {
            final int position = i;
            LayoutInflater inflater = LayoutInflater.from(OrderDetailActivity2.this);
            // 获取需要被添加控件的布局
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.goods_lv);
            // 获取需要添加的布局
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.order_detail_paid_item, null);
            // 将布局加入到当前布局中
            LinearLayout logistics_ll = (LinearLayout) layout.findViewById(R.id.logistics_ll);
            if (islogistics) {
                logistics_ll.setVisibility(View.VISIBLE);

            } else {
                logistics_ll.setVisibility(View.GONE);
            }
            logistics_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity2.this, LogisticsActivity.class);
                    startActivity(intent);
                }
            });
            linearLayout.addView(layout);
        }
    }

    @OnClick(R.id.order_break)
    public void goback() {
        finish();
    }

    @OnClick(R.id.contact_service)
    public void ContactService() {

        //判断版本若为6.0申请权限
        if (Build.VERSION.SDK_INT < 23) {
            init();
        } else {
            //6.0
            if (ContextCompat.checkSelfPermission(OrderDetailActivity2.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                init();
            } else {
                //申请该权限
                ActivityCompat.requestPermissions(OrderDetailActivity2.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x1111);
            }
        }
    }

    private void getPeers() {
        IMChatManager.getInstance().getPeers(new GetPeersListener() {
            @Override
            public void onSuccess(List<Peer> peers) {
                System.out.println("获取技能组成功");
                if (peers.size() > 1) {
                    PeerDialog dialog = new PeerDialog();
                    Bundle b = new Bundle();
                    b.putSerializable("Peers", (Serializable) peers);
                    b.putString("type", "init");
                    dialog.setArguments(b);
                    dialog.show(getFragmentManager(), "");

                } else if (peers.size() == 1) {
                    startChatActivity(peers.get(0).getId());
                } else {
                    startChatActivity("");
                }
            }

            @Override
            public void onFailed() {
                System.out.println("获取技能组失败了");
            }
        });
    }

    private void startChatActivity(String peerId) {
        Intent chatIntent = new Intent(OrderDetailActivity2.this, ChatActivity.class);
        chatIntent.putExtra("PeerId", peerId);
        startActivity(chatIntent);
    }

    private void init() {

        if (!NetUtils.hasDataConnection(OrderDetailActivity2.this)) {
            Toast.makeText(OrderDetailActivity2.this, "当前没有网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog.show(getFragmentManager(), "");
        if (APP.isKFSDK) {
            loadingDialog.dismiss();
            getPeers();
        } else {
            startKFService();
        }
    }

    private void startKFService() {
        new Thread() {
            @Override
            public void run() {
                IMChatManager.getInstance().setOnInitListener(new InitListener() {
                    @Override
                    public void oninitSuccess() {
                        APP.isKFSDK = true;
                        loadingDialog.dismiss();
                        getPeers();
                        Log.d("MobileApplication", "sdk初始化成功");

                    }

                    @Override
                    public void onInitFailed() {
                        APP.isKFSDK = false;
                        loadingDialog.dismiss();
                        Toast.makeText(OrderDetailActivity2.this, "客服初始化失败", Toast.LENGTH_SHORT).show();
                        Log.d("MobileApplication", "sdk初始化失败, 请填写正确的accessid");
                    }
                });

                //初始化IMSdk,填入相关参数
                if(user!=null){
                    IMChatManager.getInstance().init(APP.getInstance(), "com.example.cm.juyiz.kf", "dd3846f0-5b00-11e7-a763-afd134abcbb6", user.getMobile(), user.getMemberid());
                }else{
                    IMChatManager.getInstance().init(APP.getInstance(), "com.example.cm.juyiz.kf", "dd3846f0-5b00-11e7-a763-afd134abcbb6", "user", "userId");
                }


            }
        }.start();

    }

}


