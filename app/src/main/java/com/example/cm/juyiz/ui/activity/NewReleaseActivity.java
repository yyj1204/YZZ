package com.example.cm.juyiz.ui.activity;
/*
商城首页 --- 最新发布界面
*/

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.json.NewReleaseProduct;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewReleaseActivity extends BaseActivity {

    private List<NewReleaseProduct> productLists = new ArrayList<>();

    @Bind(R.id.list_newrelease)
    ListView4ScrollView list_newrelease;
    private NewReleaseAdapter newReleaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_release);
        initView();
        initData();
    }

    /*
   初始化数据
    */
    private void initData() {
        productLists.clear();
        for (int i = 0; i <17 ; i++) {
            NewReleaseProduct newReleaseProduct = new NewReleaseProduct();
            newReleaseProduct.setImgId(R.drawable.homemain_list);
            newReleaseProduct.setTitle("[3个装]蛤老大调试安防机率大幅度");
            newReleaseProduct.setPrice(i*20+9);
            productLists.add(newReleaseProduct);
            newReleaseAdapter.notifyDataSetChanged();
        }
    }

    /*
   初始化控件
    */
    private void initView() {
        ButterKnife.bind(this);//初始化bind库

        newReleaseAdapter = new NewReleaseAdapter(this, productLists, R.layout.item_newrelease_list);
        list_newrelease.setAdapter(newReleaseAdapter);
        list_newrelease.setFocusable(false);
        list_newrelease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(NewReleaseActivity.this,"点击了最新发布第" + (position+1) +"个商品",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    最新发布适配器
    */
    class NewReleaseAdapter extends SuperAdapter<NewReleaseProduct>
    {
        public NewReleaseAdapter(Context context, List<NewReleaseProduct> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, NewReleaseProduct item, final int position) {
            helper.setImageResource(R.id.iv_newrelease_list,item.getImgId());
            helper.setText(R.id.tv_newrelease_title,item.getTitle());
            helper.setText(R.id.tv_newrelease_price,item.getPrice()+"");
            //立即抢购
            TextView tv_purchase = helper.getView(R.id.tv_newrelease_purchase);
            tv_purchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NewReleaseActivity.this,"抢购了第" + (position+1) +"个商品",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /*
 普通点击事件
  */
    @OnClick(R.id.iv_newrelease_back)
    public void OnClick(View view){
        finish();
    }
}
