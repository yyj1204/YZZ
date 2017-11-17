package com.example.cm.juyiz.ui.fragment;
/*
分类片段
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.ui.activity.ClassifyActivity;
import com.example.cm.juyiz.ui.activity.ClassifyBrandActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.ui.activity.SearchActivity;
import com.example.cm.juyiz.bean.ClassBigBean;
import com.example.cm.juyiz.bean.ClassCarouselBean;
import com.example.cm.juyiz.bean.ClassChildBean;
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
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassifyFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<ClassBigBean> classBigBean = new ArrayList<>();//大分类
    private List<ClassCarouselBean> carouselBean = new ArrayList<>();//轮播图
    private List<ClassChildBean> brandBean = new ArrayList<>();//热门品牌
    private List<ClassChildBean> classSubBean = new ArrayList<>();//小分类

    private View view;

    //广告栏控件
    private AutoRelativeLayout rela_banner;
    private ViewPager bannerPager;
    private ViewPagerIndicator indicator;
    //热门品牌
    private AutoLinearLayout linear_hot;

    private ListViewAdapter listViewAdapter;
    private BannerPagerAdapter bannerPagerAdapter;

    private boolean isPagerDrag;//banner是否用户拖拽
    private int selectPosition;//分类选中位置
    private Grid1Adapter grid1Adapter;
    private Grid1Adapter grid2Adapter;
    private MyUtils myUtils;//自定义工具类

    public ClassifyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_classify, container, false);
            myUtils = new MyUtils();
            initUI();
            initData();
        }
        return view;
    }

    /*
   初始化数据
   */
    private void initData() {
        //TODO  大类分类类型
        new OkHttpUtil().get(DataUrl.URL_ClassBig, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<ClassBigBean>>() {
                }.getType();
                classBigBean.clear();
                classBigBean = jsonUtil.analysis2List(data, type);
                if (classBigBean.size() != 0) {
                    listViewAdapter.setData(classBigBean);
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        });
        //TODO  热门品牌&&分类
        initChidData(1);
        initBannerView();//初始化Banner

    }

    /*
    轮播图&品牌&小分类数据请求
     */
    private void initChidData(int child_id) {
        //TODO  banner轮播图
        new OkHttpUtil().get(DataUrl.URL_ClassCarousel + child_id, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<ClassCarouselBean>>() {
                }.getType();
                carouselBean.clear();
                carouselBean = jsonUtil.analysis2List(data, type);
                if (carouselBean.size() != 0) {
                    bannerPagerAdapter.notifyDataSetChanged();
                    bannerPager.setCurrentItem(carouselBean.size() * MyConstant.PAGER_COUNT / 2);
                } else {
                    rela_banner.setVisibility(View.GONE);
                }
            }
        });

        //TODO  品牌
        new OkHttpUtil().get(DataUrl.URL_ClassBrand + child_id, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<ClassChildBean>>() {
                }.getType();
                brandBean.clear();
                brandBean = jsonUtil.analysis2List(data, type);
                if (brandBean.size() != 0) {
                    grid1Adapter.setData(brandBean);
                    grid1Adapter.notifyDataSetChanged();
                } else {
                    linear_hot.setVisibility(View.GONE);
                }
            }
        });

        //TODO  小分类
        new OkHttpUtil().get(DataUrl.URL_ClassSub + child_id, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<ClassChildBean>>() {
                }.getType();
                classSubBean.clear();
                classSubBean = jsonUtil.analysis2List(data, type);
                if (classSubBean.size() != 0) {
                    grid2Adapter.setData(classSubBean);
                    grid2Adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
   初始化控件
    */
    private void initUI() {
        //打开搜索界面
        view.findViewById(R.id.rela_classify_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        //ListView
        ListView listView = (ListView) view.findViewById(R.id.list_classify);
        listViewAdapter = new ListViewAdapter(getActivity(), classBigBean, R.layout.item_classify_list);
        listView.setFocusable(false);
        listView.setDivider(null);//删除listview的下划线
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);

        //广告栏
        rela_banner = (AutoRelativeLayout) view.findViewById(R.id.rela_classify_banner);
        bannerPager = (ViewPager) view.findViewById(R.id.vp_classify);
        indicator = (ViewPagerIndicator) view.findViewById(R.id.indicator_classify);
        indicator.setVisibility(View.GONE);
        //热门品牌&&分类商品
        linear_hot = (AutoLinearLayout) view.findViewById(R.id.linear_classify_hot);
        GridView4ScrollView gridView1 = (GridView4ScrollView) view.findViewById(R.id.grid_classify_hot);
        GridView4ScrollView gridView2 = (GridView4ScrollView) view.findViewById(R.id.grid_classify_type);
        grid1Adapter = new Grid1Adapter(getActivity(), brandBean, R.layout.item_classify_grid);
        grid2Adapter = new Grid1Adapter(getActivity(), classSubBean, R.layout.item_classify_grid);
        gridView1.setFocusable(false);
        gridView2.setFocusable(false);
        gridView1.setAdapter(grid1Adapter);
        gridView2.setAdapter(grid2Adapter);
        gridView1.setOnItemClickListener(this);
        gridView2.setOnItemClickListener(this);
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

    //TODO 行点击事件监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.list_classify://分类ListView
                this.selectPosition = position;
                int child_id = classBigBean.get(position).getId();
                initChidData(child_id);//请求数据
                listViewAdapter.notifyDataSetChanged();
                break;
            case R.id.grid_classify_hot://热门品牌GridView
                Intent intent = new Intent(getActivity(), ClassifyBrandActivity.class);
                intent.putExtra(MyConstant.KEY_Brand, brandBean.get(position).getId());
                startActivity(intent);
                break;
            case R.id.grid_classify_type://分类GridView
                Intent intent1 = new Intent(getActivity(), ClassifyActivity.class);
                intent1.putExtra(MyConstant.KEY_Classify, classSubBean.get(position).getId());
                startActivity(intent1);
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
  大分类ListView适配器
   */
    class ListViewAdapter extends SuperAdapter<ClassBigBean> {
        public ListViewAdapter(Context context, List<ClassBigBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, ClassBigBean item, int position) {
            TextView tv_classify = helper.getView(R.id.tv_classify_list);
            tv_classify.setText(item.getName());
            //默认选中第一个
            if (position == 0) {
                tv_classify.setBackgroundResource(R.color.white);
                tv_classify.setTextColor(Color.parseColor("#222222"));
            }
            //选中与当前行相同，设置背景颜色和文本颜色
            if (selectPosition == position) {
                tv_classify.setBackgroundResource(R.color.white);
                tv_classify.setTextColor(Color.parseColor("#222222"));
            } else {
                tv_classify.setBackgroundResource(R.color.bg_classify_list);
                tv_classify.setTextColor(Color.parseColor("#666666"));
            }
        }
    }

    /*
    热门品牌&&分类  GridView适配器
  */
    class Grid1Adapter extends SuperAdapter<ClassChildBean> {
        public Grid1Adapter(Context context, List<ClassChildBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, ClassChildBean item, int position) {
            helper.setImageResource(R.id.iv_classify_grid, R.drawable.loading_classify, item.getImg_url());
            TextView tv1 = helper.getView(R.id.tv1_classify_grid);
            TextView tv2 = helper.getView(R.id.tv2_classify_grid);
            //判断标题字数，大于四个字---显示第二行控件  并换行
            String name = item.getName();
            int length = name.length();
            if (length <= 4) {
                tv1.setText(name);
                tv2.setVisibility(View.GONE);
            } else {
                String line1String = name.substring(0, 4);//第一行
                String line2String = name.substring(4);//第二行
                tv1.setText(line1String);
                tv2.setText(line2String);
                tv2.setVisibility(View.VISIBLE);
            }
        }
    }
}
