package com.example.cm.juyiz.util;

import android.os.Handler;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.json.Product;

import java.util.List;

/**
 *暂时没用！！！
 */

public class MyOnScrollListener implements OnScrollListener {
    //ListView总共显示多少条
    private int totalItemCount;
    //ListView可显示多少条
    private int visibleItemCount;

    //ListView最后的item项
    private int lastItem;

    //用于判断当前是否在加载
    private boolean isLoading;

    //数据
    private List<Product> allData;
    private List<Product> loadData;

    public MyOnScrollListener(List<Product> allData,List<Product> loadData) {
        this.allData = allData;
        this.loadData = loadData;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //如果数据没有加载，并且滑动状态是停止的，而且到达了最后一个item项
        if (!isLoading && lastItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
            //显示加载更多
            Handler handler = new Handler();
            //模拟一个延迟两秒的刷新功能
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        //开始加载更多数据
                        loadMoreData();
                        //回调设置ListView的数据
                        listener.onLoadData(loadData);
                        //加载完成后操作什么
                        loadComplete();
                    }
                }
            }, 1000);
        }
    }

    /**
     * firstVisibleItem第一个Item的位置
     * visibleItemCount 可见的Item的数量
     * totalItemCount item的总数
     */
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //当可见界面的第一个item + 当前界面多有可见的界面个数就可以得到最后一个item项了
        lastItem = firstVisibleItem + visibleItemCount;
        //总listView的item个数
        this.totalItemCount = totalItemCount;
        this.visibleItemCount = visibleItemCount;
    }


    /**
     * 当加载数据完成后，设置加载标志为false表示没有加载数据了
     * 并且设置底部加载更多为隐藏
     */
    private void loadComplete() {
        isLoading = false;
    }

    /**
     * 开始加载更多新数据，这里每次只更新三条数据
     */
    private void loadMoreData() {
        isLoading = true;
        Product product = null;
        for (int i = 1; i < visibleItemCount; i++) {
            int num = loadData.size()+i;
            product = new Product();
            product.setImgId(R.drawable.homemain_list);
            product.setTitle("首页产品"+(num));
            product.setIntroduce("这是第" +num +"个产品");
            product.setPrice(25*num);
            loadData.add(product);
        }
    }

    //回调接口
    public interface OnloadDataListener {
        void onLoadData(List<Product> data);
    }
    //接口回调的实例
    private OnloadDataListener listener;
    //设置接口回调的实例
    public void setOnLoadDataListener(OnloadDataListener listener) {
        this.listener = listener;
    }

}
