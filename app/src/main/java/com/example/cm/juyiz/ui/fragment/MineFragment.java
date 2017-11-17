package com.example.cm.juyiz.ui.fragment;
/*
个人中心片段
*/

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.app.APP;
import com.example.cm.juyiz.ui.activity.AddressManageActivity;
import com.example.cm.juyiz.ui.activity.BindDialogActivity;
import com.example.cm.juyiz.ui.activity.HomeActivity;
import com.example.cm.juyiz.ui.activity.MineFansActivity;
import com.example.cm.juyiz.ui.activity.MineRewardActivity;
import com.example.cm.juyiz.ui.activity.MineSalesActivity;
import com.example.cm.juyiz.ui.activity.MineShareActivity;
import com.example.cm.juyiz.ui.activity.OrderActivity;
import com.example.cm.juyiz.ui.activity.OrderRefundesListActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.MemberInfo;
import com.example.cm.juyiz.kf_moor.chat.ChatActivity;
import com.example.cm.juyiz.kf_moor.chat.LoadingFragmentDialog;
import com.example.cm.juyiz.kf_moor.chat.PeerDialog;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.InitListener;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.utils.NetUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends Fragment {
    private LoadingFragmentDialog loadingDialog;

    private View view;

    @Bind(R.id.iv_mine_userhead)//头像
            ImageView iv_userhead;
    @Bind(R.id.tv_mine_username)//用户名
            TextView tv_username;
    @Bind(R.id.tv_mine_userid)//用户ID
            TextView tv_userid;
    @Bind(R.id.tv_mine_usermoney)//用户消费金额
            TextView tv_usermoney;
    @Bind(R.id.tv_mine_reward)//奖励金额
            TextView tv_reward;
    @Bind(R.id.tv_mine_fans)//粉丝数量
            TextView tv_fans;
    @Bind(R.id.tv_mine_sale)//销售额
            TextView tv_sale;
    @Bind(R.id.tv_mine_binder)//推荐者
            TextView tv_binder;
    @Bind(R.id.linear_mine_binding)//绑定推荐者
            AutoLinearLayout linear_binding;
    @Bind(R.id.linear_mine_onlineservice)
    LinearLayout onlineservice;

    private MyUtils myUtils;//自定义工具类
    private Member user;

    private MemberInfo minfo;


    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
            ButterKnife.bind(this, view);//初始化bind库
            myUtils = new MyUtils();
            user = LoginUtil.getinit().getMember();

            loadingDialog = new LoadingFragmentDialog();
        }
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if (user != null) {
                new MemberTask().execute(user);
            }
        }
    }

    private MemberInfo getMemberInfo(Member user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getMemberid());
        String data = new OkHttpUtil().post(DataUrl.URL_Member, map);

        JsonUtil jsonUtil = new JsonUtil();
        Type listType = new TypeToken<MemberInfo>() {
        }.getType();
        return (MemberInfo) jsonUtil.analysis2Objiect(data, listType);
    }

    private void setData(MemberInfo memberInfo) {
        //圆形头像
        String wx_headimg = memberInfo.getWechat_headimgurl();
        if (wx_headimg != null && !"".equals(wx_headimg)) {
            GlideUtils.loadCircleImage(getActivity(), wx_headimg, iv_userhead);
        } else {
            iv_userhead.setImageResource(R.drawable.login);
        }
        //用户名
        String wx_nickname = memberInfo.getWechat_nickname();
        String mobile = memberInfo.getMobile();
        if (wx_nickname != null && !"".equals(wx_nickname)) {
            tv_username.setText(memberInfo.getWechat_nickname());
        } else if (mobile != null && !"".equals(mobile)) {
            tv_username.setText(mobile);
        } else {
            tv_username.setText("");
        }
        //用户id
        tv_userid.setText(memberInfo.getId() + "");
        //
        tv_usermoney.setText(memberInfo.getPurchase_amount() + "元");
        //
        tv_reward.setText(memberInfo.getShare_reward_amount() + "元");
        //
        tv_fans.setText(memberInfo.getShare_fans_total() + "人");
        //
        tv_sale.setText(memberInfo.getShare_sales_amount() + "元");
        //推荐者绑定
        if ((memberInfo.getFrom_id().getWechat_nickname() != null && !memberInfo.getFrom_id().getWechat_nickname().equals(""))) {
            tv_binder.setText("您是由" + memberInfo.getFrom_id().getWechat_nickname() + "推荐的！");
            linear_binding.setVisibility(View.GONE);
        } else if (memberInfo.getFrom_id().getMobile() != null && !memberInfo.getFrom_id().getMobile().equals("")) {
            tv_binder.setText("您是由" + memberInfo.getFrom_id().getMobile() + "推荐的！");
            linear_binding.setVisibility(View.GONE);
        } else {
            linear_binding.setVisibility(View.VISIBLE);
            tv_binder.setText("您是由[ 圆滋滋 ]推荐的！");
        }
    }

    /*
   普通点击事件
    */
    @OnClick({R.id.linear_mine_order, R.id.linear_mine_payment, R.id.linear_mine_pending, R.id.linear_mine_tobereceived,
            R.id.linear_mine_refunds, R.id.linear_mine_share, R.id.linear_mine_reward, R.id.linear_mine_fans, R.id.linear_mine_sale,
            R.id.linear_mine_binding, R.id.linear_mine_address, R.id.linear_mine_logout})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.linear_mine_order://我的订单
                startOrderActivity("all");
                break;
            case R.id.linear_mine_payment://待付款
                startOrderActivity("payment");
                break;
            case R.id.linear_mine_pending://待发货
                startOrderActivity("pending");
                break;
            case R.id.linear_mine_tobereceived://待收货
                startOrderActivity("tobereceived");
                break;
            case R.id.linear_mine_refunds://退换货
                Intent intent1 = new Intent(getActivity(), OrderRefundesListActivity.class);
                startActivity(intent1);
                break;
            case R.id.linear_mine_share://分享赚钱
                Intent intent = new Intent(getActivity(), MineShareActivity.class);
                String url = "";
                if (minfo != null) {
                    url = minfo.getShare_qrcode_url();
                }
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.linear_mine_reward://奖励
                startActivity(new Intent(getActivity(), MineRewardActivity.class));
                break;
            case R.id.linear_mine_fans://粉丝
                startActivity(new Intent(getActivity(), MineFansActivity.class));
                break;
            case R.id.linear_mine_sale://销售额
                startActivity(new Intent(getActivity(), MineSalesActivity.class));
                break;
            case R.id.linear_mine_binding://绑定推荐者
                startActivityForResult(new Intent(getActivity(), BindDialogActivity.class), 0);
                break;
            case R.id.linear_mine_address://我的地址
                Intent intent2 = new Intent(getActivity(), AddressManageActivity.class);
                intent2.putExtra("from","mine");
                startActivity(intent2);
                break;
            case R.id.linear_mine_logout://退出登录
                myUtils.showToast(getActivity(), "已退出登录！");
                LoginUtil.getinit().logout();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            new MemberTask().execute(user);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
        进入我的订单界面
         */
    private void startOrderActivity(String putString) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra(MyConstant.KEY_ORDER, putString);
        startActivity(intent);
    }

    //客服
    @OnClick(R.id.linear_mine_onlineservice)
    public void onlineService() {

        //判断版本若为6.0申请权限
        if (Build.VERSION.SDK_INT < 23) {
            init();
        } else {
            //6.0
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                init();
            } else {
                //申请该权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 0x1111);
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
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
        chatIntent.putExtra("PeerId", peerId);
        startActivity(chatIntent);
    }

    private void init() {

        if (!NetUtils.hasDataConnection(getActivity())) {
            Toast.makeText(getActivity(), "当前没有网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        //loadingDialog.show(getFragmentManager(), "");
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
                        Toast.makeText(getActivity(), "客服初始化失败", Toast.LENGTH_SHORT).show();
                        Log.d("MobileApplication", "sdk初始化失败, 请填写正确的accessid");
                    }
                });
                //初始化IMSdk,填入相关参数
                if (user != null) {
                    IMChatManager.getInstance().init(APP.getInstance(), "com.example.cm.juyiz.kf", "dd3846f0-5b00-11e7-a763-afd134abcbb6", user.getMobile(), user.getMemberid());
                } else {
                    IMChatManager.getInstance().init(APP.getInstance(), "com.example.cm.juyiz.kf", "dd3846f0-5b00-11e7-a763-afd134abcbb6", "user", "userId");
                }
            }
        }.start();
    }


    class MemberTask extends AsyncTask<Member, Void, MemberInfo> {

        @Override
        protected MemberInfo doInBackground(Member... params) {
            return getMemberInfo(params[0]);
        }

        @Override
        protected void onPostExecute(MemberInfo memberInfo) {
            super.onPostExecute(memberInfo);
            setData(memberInfo);
            minfo = memberInfo;
        }
    }
}
