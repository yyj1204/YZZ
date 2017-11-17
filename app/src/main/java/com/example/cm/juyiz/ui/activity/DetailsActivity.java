package com.example.cm.juyiz.ui.activity;
/*
商品详情界面
*/

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.app.APP;
import com.example.cm.juyiz.bean.DetailsBean;
import com.example.cm.juyiz.bean.ImgUrl;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.Price;
import com.example.cm.juyiz.bean.SaleProductBean;
import com.example.cm.juyiz.customwidget.ViewPagerIndicator;
import com.example.cm.juyiz.kf_moor.chat.ChatActivity;
import com.example.cm.juyiz.kf_moor.chat.LoadingFragmentDialog;
import com.example.cm.juyiz.kf_moor.chat.PeerDialog;
import com.example.cm.juyiz.ui.fragment.DetailsBannerFragment;
import com.example.cm.juyiz.util.ConnectionUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.cm.juyiz.util.SystemBar.initSystemBar;

public class DetailsActivity extends FragmentActivity {
    private DetailsBean goodBean;//详情内容
    private List<ImgUrl> carouselBean = new ArrayList<>();
    ;//轮播图
    private List<ImgUrl> detailsBean = new ArrayList<>();//详情图
    private List<SaleProductBean> hotBean = new ArrayList<>();//热门推荐

    private LoadingFragmentDialog loadingDialog;

    @Bind(R.id.linear_details_notnet)
    AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_details)
    AutoLinearLayout linear_details;
    @Bind(R.id.layout_details_banner)//轮播图
            View layout_banner;
    private ViewPager vp_banner;
    private ViewPagerIndicator indicator_banner;
    private BannerPagerAdapter bannerPagerAdapter;
    private boolean isPagerDrag;//banner是否用户拖拽

    @Bind(R.id.tv_details_title)//详情名称
            TextView tv_details_title;
    @Bind(R.id.tv_details_price)//详情价格
            TextView tv_details_price;
    @Bind(R.id.tv_details_original)//详情原价
            TextView tv_details_original;
    @Bind(R.id.tv_details_reward)//详情奖励
            TextView tv_details_reward;
    @Bind(R.id.tv_details_infos)//详情内容
            TextView tv_details_infos;
    @Bind(R.id.iv_details_promise)//承诺图
            ImageView iv_details_promise;

    @Bind(R.id.layout_details_hot)//热门推荐
            View layout_details_hot;
    @Bind(R.id.detail_img)//商品详情图
            LinearLayout detail_img;
    @Bind(R.id.tv_details_shopping)//加入购物车
            TextView tv_shopping;

    private MyUtils myUtils;
    private int detailsId;//商品id
    private Member user;

    private long systemTime;//当前系统时间
    private long time;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -8) {
                for (Price price : goodBean.getPrice()) {
                    if (price.getLevel() == 1 && price.getPrice() != 0) {
                        tv_details_price.setText(price.getPrice() + "");//促销价
                        tv_details_original.setText("");
                    } else if (price.getLevel() == 0 && price.getPrice() != 0) {
                        tv_details_price.setText(price.getPrice() + "");//原价
                        tv_details_original.setText("");
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initSystemBar(this);//沉浸式状态栏
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();
        //TODO 接收首页轮播图，热点信息，广告图，超值热卖，以及各个界面的商品传递过来的商品类型
        detailsId = getIntent().getIntExtra(MyConstant.KEY_Details, 0);
        loadingDialog = new LoadingFragmentDialog();
        //user = MemberUtils.getUser(APP.getContext());
        user = LoginUtil.getinit().getMember();
        initUI();
        connecteNet();
    }

    /*
 判断是否有网络
  */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_details.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_details.setVisibility(View.GONE);
        }
    }

    /*
 初始化数据
  */
    private void initData() {
        //TODO 商品详情
        new OkHttpUtil().get(DataUrl.URL_Goods + "?id=" + detailsId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<DetailsBean>() {
                }.getType();
                goodBean = (DetailsBean) jsonUtil.analysis2Objiect(data, type);

                //控件初始化数据
                tv_details_title.setText(goodBean.getTitle());//商品标题

                for (Price price : goodBean.getPrice()) {
                    if (price.getLevel() == 2 && price.getPrice() != 0) {
                        tv_details_price.setText(price.getPrice() + "");
                        tv_details_original.setText("");

                        systemTime = new Date().getTime() / 1000;
                        time = price.getStop_time() - systemTime;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; time > 0; time--) {
                                    if (time <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);//倒计时结束
                            }
                        }).start();
                    } else if (price.getLevel() == 1 && price.getPrice() != 0) {
                        tv_details_price.setText(price.getPrice() + "");//促销价
                        tv_details_original.setText("");

                    } else if (price.getLevel() == 0 && price.getPrice() != 0) {
                        tv_details_price.setText(price.getPrice() + "");//原价
                    }
                }


                //奖励
                if (goodBean.getReward_ratio() == null || goodBean.getReward_ratio().equals("null")) {
                    tv_details_reward.setText("0%");
                } else {
                    tv_details_reward.setText(goodBean.getReward_ratio() + "%");
                }
                //详情内容
                tv_details_infos.setText(goodBean.getIntroduction());
                //承诺图
                GlideUtils.loadImage(DetailsActivity.this, goodBean.getPromise_img_url(), R.drawable.loading_banner, iv_details_promise);
                //商品是否可购买状态
                int status_id = goodBean.getStatus_id();
                if (status_id == 0) {
                    tv_shopping.setText("加入购物车");
                } else if (status_id == 1) {
                    tv_shopping.setText("已下架");
                } else {
                    tv_shopping.setText("已售罄");
                }
                carouselBean.clear();
                carouselBean = goodBean.getGoods_carousel();
                initBannerView();//初始化Banner
                bannerPagerAdapter.notifyDataSetChanged();
                vp_banner.setCurrentItem(carouselBean.size() * MyConstant.PAGER_COUNT / 2);
                //详情图片
                for (ImgUrl url : goodBean.getGoods_details()) {
                    LayoutInflater inflater = LayoutInflater.from(DetailsActivity.this);
                    // 获取需要被添加控件的布局
                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detail_img);
                    // 获取需要添加的布局
                    final ImageView imageView = (ImageView) inflater.inflate(
                            R.layout.detail_img_item, null);
                    // 将布局加入到当前布局中
                    GlideUtils.loadIntoUseFitWidth(DetailsActivity.this, url.getImg_url(), R.drawable.loading_banner, imageView);
                    linearLayout.addView(imageView);
                }
            }
        });

        //热卖推荐
        new OkHttpUtil().get(DataUrl.URL_GoodsRecommend + "?id=" + detailsId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<SaleProductBean>>() {
                }.getType();
                hotBean.clear();
                hotBean = jsonUtil.analysis2List(data, type);

                LinearLayout linear_hot = (LinearLayout) layout_details_hot.findViewById(R.id.horizon_deails_hot);
                for (int i = 0; i < hotBean.size(); i++) {
                    final SaleProductBean saleProductBean = hotBean.get(i);
                    View childLayout = getLayoutInflater().inflate(R.layout.item_details_hot, null);
                    ImageView iv_hot = (ImageView) childLayout.findViewById(R.id.iv_details_hot1);//图片
                    TextView tv_hot_title = (TextView) childLayout.findViewById(R.id.tv_details_hot_title);//名称
                    TextView tv_hot_price = (TextView) childLayout.findViewById(R.id.tv_details_hot_price);//价格
                    TextView tv_hot_original = (TextView) childLayout.findViewById(R.id.tv_details_hot_original);//原价
                    tv_hot_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线

                    GlideUtils.loadImage(DetailsActivity.this, saleProductBean.getThumb_url(), R.drawable.loading_banner, iv_hot);
                    tv_hot_title.setText(saleProductBean.getTitle());
                    tv_hot_price.setText("¥" + saleProductBean.getPrice());
                    // tv_hot_original.setText("¥ " + saleProductBean.getOriginal());
                    linear_hot.addView(childLayout);
                    childLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //详情界面
                            Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(MyConstant.KEY_Details, saleProductBean.getId());
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    /*
 初始化控件
  */
    private void initUI() {
        vp_banner = (ViewPager) layout_banner.findViewById(R.id.vp_banner);
        indicator_banner = (ViewPagerIndicator) layout_banner.findViewById(R.id.indicator_banner);
        tv_details_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//原价 删除线
    }

    /*
 初始化Banner
  */
    private void initBannerView() {
        bannerPagerAdapter = new BannerPagerAdapter(getSupportFragmentManager());
        vp_banner.setAdapter(bannerPagerAdapter);
        setPageChangeListener();//ViewPager改变监听事件
        setAutoSrcoll();//设置ViewPager自动滑动
    }

    //TODO ViewPager改变监听事件
    private void setPageChangeListener() {
        vp_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override//滑动到某一页时触发  从0开始
            public void onPageSelected(int position) {
                indicator_banner.move(0, position % 2);
            }

            @Override//滑动ViewPager时触发  arg1：滑动比率  arg2：滑动的像素
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override//滑动状态改变时候
            public void onPageScrollStateChanged(int state) {
                switch (state) {//判断是否是手动滑动
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isPagerDrag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        isPagerDrag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isPagerDrag = false;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //TODO 设置ViewPager自动滑动
    private void setAutoSrcoll() {
        vp_banner.postDelayed(new Runnable() {
            @Override
            public void run() {
                //触碰时不自动滑动
                if (!isPagerDrag) {
                    int pagerCurrentItem = vp_banner.getCurrentItem();
                    vp_banner.setCurrentItem(pagerCurrentItem + 1);
                }
                vp_banner.postDelayed(this, 2000);
            }
        }, 2000);
    }

    /*
   广告栏适配器
    */
    class BannerPagerAdapter extends FragmentStatePagerAdapter {
        public BannerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DetailsBannerFragment bannerFragment = new DetailsBannerFragment();
            Bundle bundle = new Bundle();
            //传递轮播图的图片地址与id
            int index = position % carouselBean.size();
            String imgUrl = carouselBean.get(index).getImg_url();
            bundle.putString(MyConstant.FRAGMENT_KEY, imgUrl);
            bannerFragment.setArguments(bundle);
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return carouselBean == null ? 0 : carouselBean.size() * MyConstant.PAGER_COUNT;
        }
    }

    /*
普通点击事件
 */
    @OnClick({R.id.iv_details_back, R.id.iv_details_shopping, R.id.iv_details_message, R.id.tv_details_shopping, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_details_back://返回
                finish();
                break;
            case R.id.iv_details_shopping://购物车
                if (user != null) {
                    startActivity(new Intent(DetailsActivity.this, ShoppingActivity.class));
                } else {
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                }
                break;
            case R.id.iv_details_message://客服
                if (ConnectionUtils.isConnected(this)) {//有网络
                    //判断版本若为6.0申请权限
                    if (Build.VERSION.SDK_INT < 23) {
                        init();
                    } else {
                        //6.0
                        if (ContextCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            //该权限已经有了
                            init();
                        } else {
                            //申请该权限
                            ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x1111);
                        }
                    }
                } else {//没网络
                    myUtils.showToast(this, "请重新加载网络");
                }
                break;
            case R.id.tv_details_shopping://加入购物车
                if (ConnectionUtils.isConnected(this)) {//有网络
                    //status_id：0：可以购买，1：下架，2：售罄
                    if (goodBean.getStatus_id() == 0) {
                        if (user != null) {//已登录---加入购物车
                            if (myUtils.isFastClick()) {
                                return;
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("member_id", user.getMemberid());
                            map.put("good_id", goodBean.getId());
                            new OkHttpUtil().post(DataUrl.URL_CartAdd, map, new OkHttpUtil.HttpCallback() {
                                @Override
                                public void onSuccess(String data) {
                                    try {
                                        JSONObject json = new JSONObject(data);
                                        int ret = json.getInt("ret");
                                        if (ret == 200) {
                                            JSONObject data1 = json.getJSONObject("data");
                                            int code = data1.getInt("code");
                                            String result = data1.getString("result");
                                            if (code == 1) {
                                                myUtils.showToast(DetailsActivity.this, "已加入购物车");
                                            } else {
                                                myUtils.showToast(DetailsActivity.this, result);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {//未登录---进入登录界面
                            startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                        }
                    }
                } else {//没网络
                    myUtils.showToast(this, "请重新加载网络");
                }
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
            default:
                break;
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
        Intent chatIntent = new Intent(DetailsActivity.this, ChatActivity.class);
        chatIntent.putExtra("PeerId", peerId);
        startActivity(chatIntent);
    }

    private void init() {
        if (!NetUtils.hasDataConnection(DetailsActivity.this)) {
            Toast.makeText(DetailsActivity.this, "当前没有网络连接", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailsActivity.this, "客服初始化失败", Toast.LENGTH_SHORT).show();
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
}
