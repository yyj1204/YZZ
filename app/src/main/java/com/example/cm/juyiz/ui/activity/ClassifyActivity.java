package com.example.cm.juyiz.ui.activity;
/*
分类 --- 分类界面
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.ClassDetail;
import com.example.cm.juyiz.bean.SaleProductBean;
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

public class ClassifyActivity extends BaseActivity {
    private List<SaleProductBean> productBean = new ArrayList<>();

    @Bind(R.id.linear_classify_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_classify_activity)
    AutoLinearLayout linear_classify;
    @Bind(R.id.tv_classify_title)//标题
            TextView tv_title;
    @Bind(R.id.iv_classify_price)
    ImageView iv_price;
    @Bind(R.id.grid_classify_activity)
    GridView grid_classify;

    private ClassifyAdapter classSubAdapter;
    private int classifyId;
    private MyUtils myUtils;//自定义工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);//初始化bind库
        myUtils = new MyUtils();

        //TODO 接收首页轮播图，热点信息，广告图 以及ClassifyFragment --- 小分类 传递过来的小分类类型
        classifyId = getIntent().getIntExtra(MyConstant.KEY_Classify, 0);
        initUI();
        connecteNet(0);
    }

    /*
  初始化控件
   */
    private void initUI() {
        tv_title.setText("分类");
        classSubAdapter = new ClassifyAdapter(this, productBean, R.layout.item_vip_grid);
        grid_classify.setAdapter(classSubAdapter);
        grid_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ClassifyActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details, productBean.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /*
     初始化---判断是否有网络
      */
    private void connecteNet(int orderId) {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_classify.setVisibility(View.VISIBLE);
            initData(orderId);
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_classify.setVisibility(View.GONE);
        }
    }

    /*
     初始化数据
      */
    private void initData(int orderId) {
        //TODO 产品
        new OkHttpUtil().get(DataUrl.URL_ClassSubDetails + classifyId + "&order=" + orderId, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<ClassDetail>() {
                }.getType();
                ClassDetail classDetail = (ClassDetail) jsonUtil.analysis2Objiect(data, type);
                if (classDetail != null) {
                    tv_title.setText(classDetail.getName());
                    classSubAdapter.setData(classDetail.getGoods());
                    classSubAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
分类适配器
*/
    class ClassifyAdapter extends SuperAdapter<SaleProductBean> {
        public ClassifyAdapter(Context context, List<SaleProductBean> mdaList, int mLayoutId) {
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
    @OnClick({R.id.iv_classify_back, R.id.tv_classify_default, R.id.tv_classify_sales, R.id.linear_classify_price, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_classify_back://返回
                finish();
                break;
            case R.id.tv_classify_default://默认
                connecteNet(0);
                break;
            case R.id.tv_classify_sales://销售量
                connecteNet(1);
                break;
            case R.id.linear_classify_price://价格
                iv_price.setSelected(!iv_price.isSelected());
                boolean selected = iv_price.isSelected();
                if (selected) {
                    connecteNet(2);//升序
                } else {
                    connecteNet(3);//降序
                }
                break;
            case R.id.tv_againloading://重新加载网络
                connecteNet(0);
                break;
            default:
                break;
        }
    }
}
