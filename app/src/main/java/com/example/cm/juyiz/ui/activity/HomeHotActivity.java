package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 超值热卖界面
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.HomeMainSaleBean;
import com.example.cm.juyiz.bean.SaleProductBean;
import com.example.cm.juyiz.customwidget.GridView4ScrollView;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeHotActivity extends BaseActivity {
    private HomeMainSaleBean  saleBean;//超值热卖详情
    private List<SaleProductBean> productBean = new ArrayList<>();//超值热卖商品列表

    @Bind(R.id.iv_homehot)
    ImageView iv_homehot;
    @Bind(R.id.layout_homehot)
    View layout_homehot;

    private TextView tv_title;
    private TextView tv_introduce;

    private HotAdapter hotAdapter;
    private MyUtils myUtils;//自定义弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_hot);
        ButterKnife.bind(this);//初始化bind库
        saleBean = (HomeMainSaleBean) getIntent().getSerializableExtra(MyConstant.KEY_Hotsale);//接收首页传递过来的超值热卖详情

        initUI();
        initData();
    }

    /*
 初始化数据
  */
    private void initData() {
        //超值热卖
        new OkHttpUtil().get(DataUrl.URL_HotSale+saleBean.getId(), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    JSONArray data1 = json.getJSONArray("data");
                    if (data1.length()!=0){
                        productBean.clear();
                        for (int i = 0; i < data1.length(); i++) {
                            JSONObject saleJson = data1.getJSONObject(i);
                            SaleProductBean saleProductBean = new SaleProductBean();
                            saleProductBean.setId(Integer.parseInt(saleJson.getString("id")));
                            saleProductBean.setThumb_url(saleJson.getString("thumb_url"));
                            saleProductBean.setTitle(saleJson.getString("title"));
                            saleProductBean.setPrice(Float.parseFloat(saleJson.getString("price")));
                            productBean.add(saleProductBean);
                        }
                        hotAdapter.notifyDataSetChanged();
                    }else {
                        layout_homehot.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
 初始化控件
  */
    private void initUI() {
        myUtils = new MyUtils();

        tv_title = (TextView) layout_homehot.findViewById(R.id.tv_menu_title1);
        tv_introduce = (TextView) layout_homehot.findViewById(R.id.tv_menu_introduce1);
        GlideUtils.loadImage(this,saleBean.getImg_url(),R.drawable.loading_banner,iv_homehot);
        tv_title.setText(saleBean.getMain_title());
        tv_introduce.setText(saleBean.getSub_title());
        GridView4ScrollView grid_hot = (GridView4ScrollView) layout_homehot.findViewById(R.id.grid_home_menu1);
        hotAdapter = new HotAdapter(HomeHotActivity.this, productBean, R.layout.item_vip_grid);
        grid_hot.setAdapter(this.hotAdapter);
        grid_hot.setFocusable(false);

        grid_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(HomeHotActivity.this, DetailsActivity.class);
                intent.putExtra(MyConstant.KEY_Details,productBean.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /*
超值热卖适配器
 */
    class HotAdapter extends SuperAdapter<SaleProductBean>
    {
        public HotAdapter(Context context, List<SaleProductBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }
        @Override
        public void convert(ViewHolder helper, SaleProductBean item, int position) {
            helper.setImageResource(R.id.iv_vip_list,R.drawable.loading_product,item.getThumb_url());
            helper.setText(R.id.tv_vip_list_title,item.getTitle());
            helper.setText(R.id.tv_vip_list_price,item.getPrice()+"");
            helper.getView(R.id.tv_vip_list_save).setVisibility(View.GONE);
        }
    }
    /*
普通点击事件
 */
    @OnClick({R.id.iv_homehot_back})
    public void OnClick(View view){
        finish();
    }
}
