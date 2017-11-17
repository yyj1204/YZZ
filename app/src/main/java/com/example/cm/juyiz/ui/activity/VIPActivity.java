package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 会员专享界面
*/
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.GridView4ScrollView;
import com.example.cm.juyiz.json.VipProduct;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VIPActivity extends BaseActivity {

    private List<VipProduct> vipProducts = new ArrayList<>();

    @Bind(R.id.iv_vip_banner)//广告
    ImageView iv_banner;
    @Bind(R.id.grid_vip)//GridView
    GridView4ScrollView grid_vip;
    private VipAdapter vipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        initView();
        initData();
    }

    /*
    初始化数据
     */
    private void initData() {
        vipProducts.clear();
        for (int i = 0; i <17 ; i++) {
            VipProduct vipProduct = new VipProduct();
            if (i%4==0)
            {
                vipProduct.setImgId(R.drawable.homemain_head2_img2);
            }else if (i%4==1){
                vipProduct.setImgId(R.drawable.homemain_head2_img3);
            }else if (i%4==2) {
                vipProduct.setImgId(R.drawable.homemain_head2_img4);
            }else {
                vipProduct.setImgId(R.drawable.homemain_head2_img5);
            }
            vipProduct.setTitle("[3个装]蛤老大调试安防机率大幅度");
            vipProduct.setPrice(i*20+9);
            vipProduct.setSave(i*5+1);
            vipProducts.add(vipProduct);
            vipAdapter.notifyDataSetChanged();
        }
    }

    /*
    初始化控件
     */
    private void initView() {
        ButterKnife.bind(this);//初始化bind库

        vipAdapter = new VipAdapter(this, vipProducts, R.layout.item_vip_grid);
        grid_vip.setAdapter(vipAdapter);
        grid_vip.setFocusable(false);
        grid_vip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(VIPActivity.this,"点击了会员专享第" + (position+1) +"个商品",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
  会员专享适配器
   */
    class VipAdapter extends SuperAdapter<VipProduct>
    {

        public VipAdapter(Context context, List<VipProduct> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, VipProduct item, int position) {
            helper.setImageResource(R.id.iv_vip_list,item.getImgId());
            helper.setText(R.id.tv_vip_list_title,item.getTitle());
            helper.setText(R.id.tv_vip_list_price,item.getPrice()+"");
            helper.setText(R.id.tv_vip_list_save,"省" + item.getSave() +"元");
        }
    }

    /*
  普通点击事件
   */
        @OnClick({R.id.iv_vip_back,R.id.iv_vip_banner})
        public void OnClick(View view){
            switch (view.getId())
            {
                case R.id.iv_vip_back://返回
                    finish();
                    break;
                case R.id.iv_vip_banner://广告
                    Toast.makeText(this,"这是会员专享的广告",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
        }
    }
}
