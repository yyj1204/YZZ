package com.example.cm.juyiz.ui.fragment;
/*
商城首页---其他类型
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cm.juyiz.ui.activity.DetailsActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.HomeMenuCarouselBean;
import com.example.cm.juyiz.bean.HomeMenuDetailsBean;
import com.example.cm.juyiz.bean.HomeTypeBean;
import com.example.cm.juyiz.bean.MenuDetailsBean;
import com.example.cm.juyiz.customwidget.GridView4ScrollView;
import com.example.cm.juyiz.customwidget.ViewPagerIndicator;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.DataUrl;
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

import static com.example.cm.juyiz.R.layout.layout_banner;

public class HomeMenuFragment extends Fragment {
    private List<HomeMenuCarouselBean> carouselBean = new ArrayList<>();//轮播图
    private List<HomeMenuDetailsBean> detailslBean = new ArrayList<>();//包含商品列表的详情

    private View view;
    private LayoutInflater inflater;
    private ListView list_menu;

    private View headview;

    private ViewPager bannerPager;/*轮播图*/
    private ViewPagerIndicator indicator;/*指示器*/
    private BannerPagerAdapter bannerPagerAdapter;
    private MenuAdapter menuAdapter;

    private boolean isPagerDrag;//banner是否用户拖拽

    private MyUtils myUtils;//自定义工具类
    private HomeTypeBean typeBean;

    public HomeMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            this.inflater = inflater;
            view = inflater.inflate(R.layout.fragment_home_menu, container, false);
            myUtils = new MyUtils();
            //TODO 接收Homefragment传递过来的类型
            typeBean = (HomeTypeBean) getArguments().getSerializable(MyConstant.KEY_Homemenu);
            initUI();
            initData();
        }
        return view;
    }

    /*
        初始化数据
         */
    private void initData() {
        int typeId = typeBean.getId();
        //轮播图
        new OkHttpUtil().get(DataUrl.URL_HomeMenuCarousel + typeId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<HomeMenuCarouselBean>>() {
                }.getType();
                carouselBean.clear();
                carouselBean = jsonUtil.analysis2List(data, type);
                if (carouselBean.size() != 0) {
                    initBannerView();//初始化Banner
                    bannerPagerAdapter.notifyDataSetChanged();
                    bannerPager.setCurrentItem(carouselBean.size() * MyConstant.PAGER_COUNT / 2);
                } else {
                    headview.setVisibility(View.GONE);
                }
            }
        });
        //商品
        new OkHttpUtil().get(DataUrl.URL_HomeMenuDetails + typeId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<List<HomeMenuDetailsBean>>() {
                }.getType();
                detailslBean.clear();
                detailslBean = jsonUtil.analysis2List(data, listType);
                menuAdapter.setData(detailslBean);
                menuAdapter.notifyDataSetChanged();
            }
        });
    }

    /*
   初始化控件
    */
    private void initUI() {
        list_menu = (ListView) view.findViewById(R.id.list_home_menu);
        headview = inflater.inflate(layout_banner, null);
        list_menu.addHeaderView(headview, null, false);
        initHeadView();
        menuAdapter = new MenuAdapter(getActivity(), detailslBean, R.layout.layout_main_type);
        list_menu.setAdapter(menuAdapter);
    }

    /*
    初始化HeadView
     */
    private void initHeadView() {
        bannerPager = (ViewPager) headview.findViewById(R.id.vp_banner);
        indicator = (ViewPagerIndicator) headview.findViewById(R.id.indicator_banner);
    }

    /*
  初始化Banner
   */
    private void initBannerView() {
        bannerPagerAdapter = new BannerPagerAdapter(getChildFragmentManager());
        bannerPager.setAdapter(bannerPagerAdapter);
        setPageChangeListener();//ViewPager改变监听事件
        setAutoSrcoll();//设置ViewPager自动滑动
    }

    //TODO ViewPager改变监听事件
    private void setPageChangeListener() {
        bannerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override//滑动到某一页时触发  从0开始
            public void onPageSelected(int position) {
                indicator.move(0, position % 2);
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
        bannerPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                //触碰时不自动滑动
                if (!isPagerDrag) {
                    int pagerCurrentItem = bannerPager.getCurrentItem();
                    bannerPager.setCurrentItem(pagerCurrentItem + 1);
                }
                bannerPager.postDelayed(this, 2000);
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
            HomeMenuBannerFragment bannerFragment = new HomeMenuBannerFragment();
            Bundle bundle = new Bundle();
            //传递轮播图的图片地址与id
            int index = position % carouselBean.size();
            HomeMenuCarouselBean homeMenuCarouselBean = carouselBean.get(index);
            bundle.putSerializable(MyConstant.FRAGMENT_KEY, homeMenuCarouselBean);
            bannerFragment.setArguments(bundle);
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return carouselBean == null ? 0 : carouselBean.size() * MyConstant.PAGER_COUNT;
        }
    }

    /*
 爆款推荐 --- 总分类适配器
  */
    class MenuAdapter extends SuperAdapter<HomeMenuDetailsBean> {
        public MenuAdapter(Context context, List<HomeMenuDetailsBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, HomeMenuDetailsBean item, final int position) {
            helper.setText(R.id.tv_menu_title1, item.getMain_title());
            helper.setText(R.id.tv_menu_introduce1, item.getSub_title());
            GridView4ScrollView grid = helper.getView(R.id.grid_home_menu1);
            final List<MenuDetailsBean> menuDetailsBeen = item.getGoods_id();
            ProductAdapter productAdapter = new ProductAdapter(getActivity(), menuDetailsBeen, R.layout.item_vip_grid);
            grid.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(MyConstant.KEY_Details, menuDetailsBeen.get(position).getId());
                    startActivity(intent);
                }
            });
        }
    }

    /*
 爆款推荐 --- 小分类适配器
  */
    class ProductAdapter extends SuperAdapter<MenuDetailsBean> {
        public ProductAdapter(Context context, List<MenuDetailsBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, MenuDetailsBean item, int position) {
            helper.setImageResource(R.id.iv_vip_list, R.drawable.loading_product, item.getThumb_url());
            helper.setText(R.id.tv_vip_list_title, item.getTitle());
            helper.setText(R.id.tv_vip_list_price, item.getPrice() + "");
            helper.getView(R.id.tv_vip_list_save).setVisibility(View.GONE);
        }
    }
}
