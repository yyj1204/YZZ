package com.example.cm.juyiz.ui.fragment;
/*
商城首页---首页类型
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.ui.activity.DetailsActivity;
import com.example.cm.juyiz.ui.activity.HomeHotActivity;
import com.example.cm.juyiz.ui.activity.HomeNewReleaseActivity;
import com.example.cm.juyiz.ui.activity.HomeRankingActivity;
import com.example.cm.juyiz.ui.activity.HomeRecommendActivity;
import com.example.cm.juyiz.ui.activity.HomeTimedSpecialsActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.HomeMainAdsBean;
import com.example.cm.juyiz.bean.HomeMainCarouselBean;
import com.example.cm.juyiz.bean.HomeMainHotspotsBean;
import com.example.cm.juyiz.bean.HomeMainPromotionBean;
import com.example.cm.juyiz.bean.HomeMainSaleBean;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.customwidget.ViewPagerIndicator;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeMainFragment extends Fragment implements View.OnClickListener {
    private List<HomeMainCarouselBean> carouselBean = new ArrayList<>();//轮播图
    private HomeMainHotspotsBean hotspotsBean;//热点信息
    private List<HomeMainPromotionBean> promotionBean = new ArrayList<>();//五个活动
    private List<HomeMainAdsBean> adsBean = new ArrayList<>();//广告图
    private List<HomeMainSaleBean> saleBean = new ArrayList<>();//超值热卖

    private View view;
    private LayoutInflater inflater;

    private ListView listView;
    private View headView1;
    private View headView2;
    private View footView;

    //商品数量指示布局
    private LinearLayout line_imgcount;
    private TextView tv_imgnum;
    private TextView tv_imgtotal;
    private ImageView iv_totop;

    //headerView1 --- 轮播图 & 热点信息
    private View layout_banner;
    private ViewPager vp_banner;
    private ViewPagerIndicator indicator_banner;
    private TextView tv_hotmessage;

    //headerView2 --- 五个活动
    private ImageView iv_promotion1;
    private ImageView iv_promotion2;
    private ImageView iv_promotion3;
    private ImageView iv_promotion4;
    private ImageView iv_promotion5;

    //广告图
    private ListView4ScrollView list_advertise;

    //适配器 --- 轮播图 & 广告图 & 超值热卖
    private LisViewtAdapter listViewtAdapter;
    private AdvertiseAdapter advertiseAdapter;
    private BannerPagerAdapter bannerPagerAdapter;

    private boolean isPagerDrag;//轮播图是否用户拖拽
    private boolean isListViewScroll;//ListView是否用户滑动
    private MyUtils myUtils;//自定义工具类

    public HomeMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            this.inflater = inflater;
            view = inflater.inflate(R.layout.fragment_home_main, container, false);
            myUtils = new MyUtils();
            initUI();//初始化控件
            initData();//初始化数据
        }
        return view;
    }

    /*
         初始化控件
          */
    private void initUI() {
        initTopView();//初始化回到顶部布局
        initListView();//初始化ListView布局
    }

    /*
 初始化数据
  */
    private void initData() {
        //轮播图
        new OkHttpUtil().get(DataUrl.URL_HomeCarousel, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                carouselBean.clear();
                Type listType = new TypeToken<List<HomeMainCarouselBean>>() {
                }.getType();
                carouselBean = jsonUtil.analysis2List(data, listType);
                if (carouselBean.size() == 0) {
                    layout_banner.setVisibility(View.GONE);//隐藏轮播图布局
                } else {
                    initBannerView();//初始化Banner
                    bannerPagerAdapter.notifyDataSetChanged();
                    vp_banner.setCurrentItem(carouselBean.size() * MyConstant.PAGER_COUNT / 2);
                }
            }
        });
        //热点信息
        new OkHttpUtil().get(DataUrl.URL_HomeHotspots, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<HomeMainHotspotsBean>() {
                }.getType();
                hotspotsBean = (HomeMainHotspotsBean) jsonUtil.analysis2Objiect(data, listType);
                tv_hotmessage.setText(hotspotsBean.getTitle());
            }
        });
        //五个活动
        new OkHttpUtil().get(DataUrl.URL_HomePromotion, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listtype = new TypeToken<List<HomeMainPromotionBean>>() {
                }.getType();
                promotionBean.clear();
                promotionBean = jsonUtil.analysis2List(data, listtype);
                GlideUtils.loadImage(getActivity(), promotionBean.get(0).getImg_url(), R.drawable.loading_product, iv_promotion1);
                GlideUtils.loadImage(getActivity(), promotionBean.get(1).getImg_url(), R.drawable.loading_product, iv_promotion2);
                GlideUtils.loadImage(getActivity(), promotionBean.get(2).getImg_url(), R.drawable.loading_product, iv_promotion3);
                GlideUtils.loadImage(getActivity(), promotionBean.get(3).getImg_url(), R.drawable.loading_product, iv_promotion4);
                GlideUtils.loadImage(getActivity(), promotionBean.get(4).getImg_url(), R.drawable.loading_product, iv_promotion5);
            }
        });
        //广告图
        new OkHttpUtil().get(DataUrl.URL_HomeAds, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<List<HomeMainAdsBean>>() {
                }.getType();
                adsBean.clear();
                adsBean = jsonUtil.analysis2List(data, listType);
                if (adsBean.size() == 0) {
                    list_advertise.setVisibility(View.GONE);
                } else {
                    advertiseAdapter.notifyDataSetChanged();
                }
            }
        });
        //超值热卖
        new OkHttpUtil().get(DataUrl.URL_HomeHotSale, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.e("hot", data);
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<List<HomeMainSaleBean>>() {
                }.getType();
                saleBean.clear();
                saleBean = jsonUtil.analysis2List(data, listType);
                if (saleBean.size() != 0) {
                    tv_imgtotal.setText(saleBean.size() + "");
                    listViewtAdapter.setData(saleBean);
                    listViewtAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initListView() {
        listView = (ListView) view.findViewById(R.id.list_home_main);
        //增加headerView、footView，设置不可整行选中，可以监听每个控件
        headView1 = inflater.inflate(R.layout.layout_homemain_head1, null);
        headView2 = inflater.inflate(R.layout.layout_homemain_head2, null);
        footView = inflater.inflate(R.layout.layout_homemain_foot, null);
        listView.addHeaderView(headView1, null, false);
        listView.addHeaderView(headView2, null, false);
        listView.addFooterView(footView, null, false);
        initHeadView1();//初始化第一个HeaderView
        initHeadView2();//初始化第二个HeaderView

        listView.setFocusable(false);
        listView.setDivider(null);//删除listview的下划线

        listViewtAdapter = new LisViewtAdapter(getActivity(), this.saleBean, R.layout.item_homemain_list);
        listView.setAdapter(listViewtAdapter);
        setListViewLisener();//设置ListView相关监听事件
    }

    /*
    初始化回到顶部布局
    */
    private void initTopView() {
        line_imgcount = (LinearLayout) view.findViewById(R.id.line_homemain_imgcount);//数量布局
        tv_imgnum = (TextView) view.findViewById(R.id.tv_homemain_listnum);//当前序号
        tv_imgtotal = (TextView) view.findViewById(R.id.tv_homemain_listtotal);//总数量
        iv_totop = (ImageView) view.findViewById(R.id.iv_homemain_totop);//回到顶部
        iv_totop.setOnClickListener(this);
    }

    /*
    初始化第一个HeaderView
     */
    private void initHeadView1() {
        layout_banner = headView1.findViewById(R.id.layout_homemain_banner);
        vp_banner = (ViewPager) layout_banner.findViewById(R.id.vp_banner);
        indicator_banner = (ViewPagerIndicator) layout_banner.findViewById(R.id.indicator_banner);
        tv_hotmessage = (TextView) headView1.findViewById(R.id.tv_homemain_message);
        tv_hotmessage.setOnClickListener(this);
    }

    /*
   初始化Banner
    */
    private void initBannerView() {
        bannerPagerAdapter = new BannerPagerAdapter(getChildFragmentManager());
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
    初始化第二个HeaderView
     */
    private void initHeadView2() {
        //五个活动栏
        iv_promotion1 = (ImageView) headView2.findViewById(R.id.iv_homemain_activity1);
        iv_promotion2 = (ImageView) headView2.findViewById(R.id.iv_homemain_activity2);
        iv_promotion3 = (ImageView) headView2.findViewById(R.id.iv_homemain_activity3);
        iv_promotion4 = (ImageView) headView2.findViewById(R.id.iv_homemain_activity4);
        iv_promotion5 = (ImageView) headView2.findViewById(R.id.iv_homemain_activity5);
        iv_promotion1.setOnClickListener(this);
        iv_promotion2.setOnClickListener(this);
        iv_promotion3.setOnClickListener(this);
        iv_promotion4.setOnClickListener(this);
        iv_promotion5.setOnClickListener(this);

        //广告图
        list_advertise = (ListView4ScrollView) headView2.findViewById(R.id.list_homemain_advertise);
        advertiseAdapter = new AdvertiseAdapter(getActivity(), adsBean, R.layout.item_homemain_advertise_list);
        list_advertise.setAdapter(advertiseAdapter);
        listView.setFocusable(false);
        listView.setDivider(null);//删除listview的下划线
        list_advertise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeMainAdsBean homeMainAdsBean = adsBean.get(i);
                String link_type = homeMainAdsBean.getLink_type();
                int link_id = homeMainAdsBean.getLink_id();
                myUtils.startActivityType(getActivity(), link_type, link_id);
            }
        });

        //超值热卖---字体加粗
        TextView tv_hotsale = (TextView) headView2.findViewById(R.id.tv_homemain_hot);
        TextPaint tp = tv_hotsale.getPaint();
        tp.setFakeBoldText(true);
    }

    //TODO 设置ListView相关监听事件
    private void setListViewLisener() {
        //TODO 触碰到ListView的子控件时触发
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //打开商品详情界面||超值热卖详情界面
                HomeMainSaleBean homeMainSaleBean = saleBean.get(i - 2);
                int type = homeMainSaleBean.getSale_type();
                if (type == 0) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(MyConstant.KEY_Details, Integer.parseInt(homeMainSaleBean.getGoods_id()));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), HomeHotActivity.class);
                    intent.putExtra(MyConstant.KEY_Hotsale, homeMainSaleBean);
                    startActivity(intent);
                }
            }
        });

        //TODO ListView滚动时触发
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://滚动停止时的状态,显示回到顶部   隐藏产品序号
                        isListViewScroll = false;
                        int firstVisiblePosition = listView.getFirstVisiblePosition();
                        int headerViewsCount = listView.getHeaderViewsCount();
                        if (firstVisiblePosition >= headerViewsCount) {
                            line_imgcount.setVisibility(View.GONE);
                            iv_totop.setVisibility(View.VISIBLE);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://触摸正在滚动，手指还没离开界面时的状态
                        isListViewScroll = true;
                        break;
                    case SCROLL_STATE_FLING://用户在用力滑动后，ListView由于惯性将继续滑动时的状态
                        isListViewScroll = true;
                        break;
                    default:
                        break;
                }
            }

            /**
             * firstVisibleItem第一个Item的位置
             * visibleItemCount 可见的Item的数量
             * totalItemCount item的总数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //TODO 回到顶部
                int headerViewsCount = listView.getHeaderViewsCount();
                int footerViewsCount = listView.getFooterViewsCount();
                int otherCount = headerViewsCount + footerViewsCount - 1;
                int lastVisiblePosition = listView.getLastVisiblePosition();
                //显示产品序号
                if (lastVisiblePosition < saleBean.size() + headerViewsCount) {
                    tv_imgnum.setText((lastVisiblePosition - headerViewsCount + 1) + "");
                } else {
                    tv_imgnum.setText(saleBean.size() + "");
                }

                //如果滑动高度到头部布局，显示相关布局
                //否则  隐藏相关布局
                if (firstVisibleItem >= headerViewsCount) {
                    //如果用户在滑动  显示产品序号布局，隐藏回到顶部布局
                    //否则  反之
                    if (!isListViewScroll) {
                        line_imgcount.setVisibility(View.GONE);
                        iv_totop.setVisibility(View.VISIBLE);
                    } else {
                        line_imgcount.setVisibility(View.VISIBLE);
                        iv_totop.setVisibility(View.GONE);
                    }
                } else {
                    line_imgcount.setVisibility(View.GONE);
                    iv_totop.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    普通点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_homemain_totop: //回到顶部
                listView.setSelection(0);
                break;
            /*活动栏*/
            case R.id.iv_homemain_activity1://限时特价
                startActivity(new Intent(getActivity(), HomeTimedSpecialsActivity.class));
                break;
            case R.id.iv_homemain_activity2://聚推荐
                startActivity(new Intent(getActivity(), HomeRecommendActivity.class));
                break;
            case R.id.iv_homemain_activity3://最新发布
                startActivity(new Intent(getActivity(), HomeNewReleaseActivity.class));
                break;
            case R.id.iv_homemain_activity4://排行榜
                startActivity(new Intent(getActivity(), HomeRankingActivity.class));
                break;
            case R.id.iv_homemain_activity5://会员专享
                myUtils.showToast(getActivity(), "即将到来，敬请期待！");
//TODO 暂不开放                startActivity(new Intent(getActivity(), HomeVIPActivity.class));
                break;
            case R.id.tv_homemain_message: //热点信息
                String link_type = hotspotsBean.getLink_type();
                int link_id = hotspotsBean.getLink_id();
                myUtils.startActivityType(getActivity(), link_type, link_id);
                break;
            default:
                break;
        }
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
            HomeMainBannerFragment bannerFragment = new HomeMainBannerFragment();
            Bundle bundle = new Bundle();
            //传递轮播图的图片地址与id
            int index = position % carouselBean.size();
            HomeMainCarouselBean carousel = carouselBean.get(index);
            bundle.putSerializable(MyConstant.FRAGMENT_KEY, carousel);
            bannerFragment.setArguments(bundle);
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return carouselBean == null ? 0 : carouselBean.size() * MyConstant.PAGER_COUNT;
        }
    }

    /*
    ListView热卖超值适配器
     */
    class LisViewtAdapter extends SuperAdapter<HomeMainSaleBean> {
        public LisViewtAdapter(Context context, List<HomeMainSaleBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void setData(List<HomeMainSaleBean> mdaList) {
            super.setData(mdaList);
        }

        @Override
        public void convert(ViewHolder helper, HomeMainSaleBean item, int position) {
            helper.setImageResource(R.id.iv_homemain_list, R.drawable.loading_banner, item.getImg_url());
            helper.setText(R.id.tv_homemain_list_title, item.getMain_title());
            helper.setText(R.id.tv_homemain_list_introduce, item.getSub_title());
            int type = item.getSale_type();
            if (type == 0) {
                helper.setText(R.id.tv_homemain_list_price, "¥" + item.getPrice());
            } else {
                helper.setText(R.id.tv_homemain_list_price, "¥" + item.getPrice() + "起");
            }
            //如果是最后一行  分割线隐藏
            if (position == saleBean.size() - 1) {
                helper.getView(R.id.view_homemain_list).setVisibility(View.GONE);
            }
        }
    }

    /*
    ListView广告图适配器
     */
    class AdvertiseAdapter extends SuperAdapter<HomeMainAdsBean> {
        public AdvertiseAdapter(Context context, List<HomeMainAdsBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, HomeMainAdsBean item, int position) {
            helper.setImageResource(R.id.iv_homemain_advertise_list, R.drawable.loading_banner, item.getImg_url());
        }
    }
}
