package com.example.cm.juyiz.ui.activity;
/*
商品分类 --- 搜索界面
*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.HistoryUtil;
import com.example.cm.juyiz.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.search_nohistory)
    TextView nohistory;
    @Bind(R.id.search_history)
    ListView4ScrollView searchhistory;
    @Bind(R.id.search_close)
    TextView searchclose;

    private Myadapt myadapt;

    private String searchhistorystr;
    private String[] searchStrs;
    private List<String> historylist;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MyUtils myUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initData();
        initUI();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
        myadapt.notifyDataSetChanged();
    }


    private void initData() {
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

    private void initUI() {
        myUtils = new MyUtils();
        myadapt = new Myadapt(SearchActivity.this, historylist, R.layout.search_history);
        searchhistory.setAdapter(myadapt);
        if (historylist.size() == 0 || historylist == null) {
            nohistory.setVisibility(View.VISIBLE);
            searchhistory.setVisibility(View.GONE);
            searchclose.setVisibility(View.GONE);
        } else {
            nohistory.setVisibility(View.GONE);
            searchhistory.setVisibility(View.VISIBLE);
            searchclose.setVisibility(View.VISIBLE);
        }
        searchhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("search", historylist.get(position));
                String search = historylist.get(position);
                historylist.remove(search);
                historylist.add(0, search);
                try {
                    searchhistorystr = HistoryUtil.SceneList2String(historylist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor.putString("searchhistorystr", searchhistorystr);
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
    }


    /*
   普通点击事件
    */
    @OnClick(R.id.search_close)
    public void close() {
        nohistory.setVisibility(View.VISIBLE);
        searchhistory.setVisibility(View.GONE);
        searchclose.setVisibility(View.GONE);
        historylist.clear();
        editor.clear();
        editor.commit();
    }

    @OnClick({R.id.iv_search_back, R.id.tv_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                if (MyUtils.isFastClick()){
                    return;
                }
                String search = et_search.getText().toString().trim();
                if ("".equals(search) || search == null) {
                    myUtils.showToast(this, "请输入要搜索的关键字！");
                } else {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("search", search);
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
                    myadapt.notifyDataSetChanged();
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    class Myadapt extends SuperAdapter<String> {

        public Myadapt(Context context, List mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }


        @Override
        public void convert(ViewHolder helper, String item, int position) {
            helper.setText(R.id.search_history_tv, item);
        }
    }


}
