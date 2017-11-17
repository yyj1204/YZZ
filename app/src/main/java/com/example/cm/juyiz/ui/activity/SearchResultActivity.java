package com.example.cm.juyiz.ui.activity;
/**
 * 搜索结果
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.SearchResultBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.GlideUtils;
import com.example.cm.juyiz.util.HistoryUtil;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.MyUtils;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultActivity extends BaseActivity {
    private List<SearchResultBean> goods = new ArrayList<>();

    @Bind(R.id.linear_search_notnet)//没网络
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_search)
    AutoLinearLayout linear_search;
    @Bind(R.id.tv_search_nothing)//没有搜索结果
            TextView tv_nothing;
    @Bind(R.id.linear_search_sub)
    AutoLinearLayout linear_search_sub;
    @Bind(R.id.search_result_goods)
    GridView gv;
    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.search_order0)
    TextView order_default;
    @Bind(R.id.search_order1)
    TextView order_sales;
    @Bind(R.id.search_order_price)
    LinearLayout order_price;
    @Bind(R.id.search_price_iv)
    ImageView search_price_iv;

    private String searchstr;
    private List<String> historylist;
    private String[] searchStrs;
    private String searchhistorystr;
    private SearchAdapter searchAdapter;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MyUtils myUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myUtils = new MyUtils();
        Intent intent = getIntent();
        searchstr = intent.getStringExtra("search");
        et_search.setText(searchstr);
        initUI();
        connecteNet(searchstr, 0);

        String searchhistorystr = sharedPreferences.getString("searchhistorystr", "");
        historylist = new ArrayList<>();
        if (!TextUtils.isEmpty(searchhistorystr)) {
            try {
                historylist = HistoryUtil.String2SceneList(searchhistorystr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /*
   判断是否有网络
    */
    private void connecteNet(String keyword, int order) {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_search.setVisibility(View.VISIBLE);
            SearchData(keyword, order);
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_search.setVisibility(View.GONE);
        }
    }

    /*
    初始化
     */
    private void initUI() {
        searchAdapter = new SearchAdapter(this, goods, R.layout.search_result_goods_item);
        gv.setAdapter(searchAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(SearchResultActivity.this, DetailsActivity.class);
                intent1.putExtra(MyConstant.KEY_Details, goods.get(position).getId());
                startActivity(intent1);
            }
        });
    }

    //返回
    @OnClick(R.id.iv_search_back)
    public void goback() {
        finish();
    }

    //重新加载网络
    @OnClick(R.id.tv_againloading)
    public void load() {
        connecteNet(searchstr, 0);
        search_price_iv.setSelected(true);
    }

    //默认排序
    @OnClick(R.id.search_order0)
    public void OrderDefault() {
        connecteNet(searchstr, 0);
        search_price_iv.setSelected(true);
    }

    //销量排序
    @OnClick(R.id.search_order1)
    public void OrderSales() {
        connecteNet(searchstr, 0);
        search_price_iv.setSelected(true);
    }

    //价格排序
    @OnClick(R.id.search_order_price)
    public void OrderPrice() {
        search_price_iv.setSelected(!search_price_iv.isSelected());
        boolean selected = search_price_iv.isSelected();
        if (selected) {
            connecteNet(searchstr, 2);
        } else {
            connecteNet(searchstr, 3);
        }
    }

    //搜索
    @OnClick(R.id.search_search)
    public void search() {
        //实时更新搜索框内的关键字
        if (MyUtils.isFastClick()) {
            return;
        }
        String search = et_search.getText().toString().trim();
        if ("".equals(search) || search == null) {
            myUtils.showToast(this, "请输入要搜索的关键字！");
        } else {
            for (String str : historylist) {
                if (str.equals(search)) {
                    historylist.remove(str);
                    break;
                } else {
                    if (historylist.size() > 10) {
                        historylist.remove(historylist.size() - 1);
                        break;
                    }
                }
            }
            historylist.add(0, search);
            try {
                searchhistorystr = HistoryUtil.SceneList2String(historylist);
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.putString("searchhistorystr", searchhistorystr);
            editor.commit();
            connecteNet(searchstr, 0);
        }
    }

    private void SearchData(final String keyword, int order) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("order", order);

        new OkHttpUtil().post(DataUrl.URL_SearchGoods, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<SearchResultBean>>() {
                }.getType();
                goods = jsonUtil.analysis2List(data, type);
                if (goods.size() != 0) {
                    tv_nothing.setVisibility(View.GONE);
                    linear_search_sub.setVisibility(View.VISIBLE);
                    searchAdapter.setData(goods);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    tv_nothing.setVisibility(View.VISIBLE);
                    linear_search_sub.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    搜索结果适配器
     */
    class SearchAdapter extends SuperAdapter<SearchResultBean> {
        public SearchAdapter(Context context, List<SearchResultBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, SearchResultBean item, int position) {
            ImageView img = helper.getView(R.id.search_item_img);
            GlideUtils.loadIntoUseFitWidth(SearchResultActivity.this, item.getThumb_url(), R.drawable.loading_product, img);
            helper.setText(R.id.search_item_title, item.getTitle());
            helper.setText(R.id.search_item_price, "￥" + item.getPrice());
        }
    }
}
