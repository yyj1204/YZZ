package com.example.cm.juyiz.ui.activity;
/*
分类 --- 品牌界面
*/

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.BrandDetail;
import com.example.cm.juyiz.bean.ClassCarouselBean;
import com.example.cm.juyiz.bean.SaleProductBean;
import com.example.cm.juyiz.customwidget.ViewPagerIndicator;
import com.example.cm.juyiz.ui.fragment.ClassBannerFragment;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.cm.juyiz.util.SystemBar.initSystemBar;

public class ClassifyBrandActivity extends FragmentActivity {

    private List<ClassCarouselBean> carouselBean = new ArrayList<>();
    private List<SaleProductBean> productBean = new ArrayList<>();

    @Bind(R.id.linear_classifybrand_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_classifybrand)
    AutoLinearLayout linear_classifybrand;
    @Bind(R.id.tv_classifybrand_title)//标题
            TextView tv_title;
    @Bind(R.id.layout_classifybrand_banner)//轮播图
            View layout_banner;
    private ViewPager vp_banner;
    private ViewPagerIndicator indicator_banner;

    @Bind(R.id.iv_classifybrand_price)
    ImageView iv_price;

    @Bind(R.id.grid_classify_brand)
    GridView grid_classify;

    private BannerPagerAdapter bannerPagerAdapter;
    private BrandAdapter brandAdapter;

    private boolean isPagerDrag;//banner是否用户拖拽
    private int brandId;
    private MyUtils myUtils;//自定义工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_brand);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initSystemBar(this);//沉浸式状态栏
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();

        //TODO 接收首页轮播图，热点信息，广告图 以及ClassifyFragment --- 热门品牌 传递过来的小分类类型
        brandId = getIntent().getIntExtra(MyConstant.KEY_Brand, 0);
        initUI();
        connecteNet();
    }

    /*
 初始化控件
  */
    private void initUI() {
        vp_banner = (ViewPager) layout_banner.findViewById(R.id.vp_banner);
        indicator_banner = (ViewPagerIndicator) layout_banner.findViewById(R.id.indicator_banner);
        brandAdapter = new BrandAdapter(this, productBean, R.layout.item_vip_grid);
        grid_classify.setAdapter(brandAdapter);
        grid_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ClassifyBrandActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details, productBean.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /*
  初始化---判断是否有网络
   */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_classifybrand.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_classifybrand.setVisibility(View.GONE);
        }
    }

    /*
     初始化数据
      */
    private void initData() {
        //TODO  banner轮播图
        new OkHttpUtil().get(DataUrl.URL_BrandCarousel + brandId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<ClassCarouselBean>>() {
                }.getType();
                carouselBean.clear();
                carouselBean = jsonUtil.analysis2List(data, type);
                if (carouselBean.size() != 0) {
                    initBannerView();//初始化Banner
                    bannerPagerAdapter.notifyDataSetChanged();
                    vp_banner.setCurrentItem(carouselBean.size() * MyConstant.PAGER_COUNT / 2);
                }
            }
        });

        initBrandData(0);
    }

    /*
    热门品牌商品列表数据
     */
    private void initBrandData(int orderId) {
        //TODO 产品
        new OkHttpUtil().get(DataUrl.URL_BrandDetails + brandId + "&order=" + orderId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<BrandDetail>() {
                }.getType();
                BrandDetail brandDetail = (BrandDetail) jsonUtil.analysis2Objiect(data, type);
                if (brandDetail != null) {
                    if (brandDetail.getName() != null && !"".equals(brandDetail.getName())) {
                        tv_title.setText(brandDetail.getName());
                    } else {
                        tv_title.setText("品牌");
                    }
                    brandAdapter.setData(brandDetail.getGoods());
                    brandAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
 初始化Banner
  */
    private void initBannerView() {
        bannerPagerAdapter = new BannerPagerAdapter(getSupportFragmentManager());
        vp_banner.setAdapter(this.bannerPagerAdapter);
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
            ClassBannerFragment bannerFragment = new ClassBannerFragment();
            Bundle bundle = new Bundle();
            //传递轮播图的图片地址与id
            int index = position % carouselBean.size();
            ClassCarouselBean classCarouselBean = carouselBean.get(index);
            bundle.putSerializable(MyConstant.FRAGMENT_KEY, classCarouselBean);
            bannerFragment.setArguments(bundle);
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return carouselBean == null ? 0 : carouselBean.size() * MyConstant.PAGER_COUNT;
        }
    }

    /*
分类适配器
*/
    class BrandAdapter extends SuperAdapter<SaleProductBean> {
        public BrandAdapter(Context context, List<SaleProductBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, SaleProductBean item, int position) {
            helper.setImageResource(R.id.iv_vip_list, R.drawable.loading_product, item.getThumb_url());
            helper.setText(R.id.tv_vip_list_title, item.getTitle());
            helper.setText(R.id.tv_vip_list_price, item.getPrice() + "");
            helper.getView(R.id.tv_vip_list_save).setVisibility(View.GONE);
        }
    }

    /*
      普通点击事件
      */
    @OnClick({R.id.iv_classifybrand_back, R.id.tv_classifybrand_default, R.id.tv_classifybrand_sales, R.id.linear_classifybrand_price, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_classifybrand_back://返回
                finish();
                break;
            case R.id.tv_classifybrand_default://默认
                isConnect(0);
                break;
            case R.id.tv_classifybrand_sales://销售量
                isConnect(1);
                break;
            case R.id.linear_classifybrand_price://价格
                iv_price.setSelected(!iv_price.isSelected());
                boolean selected = iv_price.isSelected();
                if (selected) {
                    isConnect(2);//升序
                } else {
                    isConnect(3);//降序
                }
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet();
                break;
            default:
                break;
        }
    }

    /*
    判断网络
     */
    private void isConnect(int orderId) {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_classifybrand.setVisibility(View.VISIBLE);
            initBrandData(orderId);
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_classifybrand.setVisibility(View.GONE);
        }
    }
}
