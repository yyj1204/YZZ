package com.example.cm.juyiz.ui.activity;
/*
奖励 --- 提现记录界面
*/

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.RewardRecordBean;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.ConnectionUtils;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
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

import static com.example.cm.juyiz.R.id.tv_record_nothing;

public class RewardRecordActivity extends BaseActivity {
    private List<RewardRecordBean> recordBean = new ArrayList<>();

    @Bind(R.id.linear_record_notnet)//没网络布局
            AutoLinearLayout linear_notnet;
    @Bind(R.id.linear_record)
    AutoLinearLayout linear_record;
    @Bind(tv_record_nothing)//暂无记录
            TextView tv_nothing;
    @Bind(R.id.linear_record_sub)//提现记录布局
            LinearLayout linear_record_sub;
    @Bind(R.id.list_reward_record)
    ListView list_record;

    private RecordAdapter recordAdapter;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_record);
        ButterKnife.bind(this);//初始化bind库
        user = LoginUtil.getinit().getMember();
        initUI();
        connecteNet();
    }

    /*
   判断是否有网络
    */
    private void connecteNet() {
        boolean connected = ConnectionUtils.isConnected(this);
        if (connected) {
            linear_notnet.setVisibility(View.GONE);
            linear_record.setVisibility(View.VISIBLE);
            initData();
        } else {
            linear_notnet.setVisibility(View.VISIBLE);
            linear_record.setVisibility(View.GONE);
        }
    }

    /*
初始化数据
*/
    private void initData() {
        new OkHttpUtil().get(DataUrl.URL_MemberCashRecord + user.getMemberid(), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type type = new TypeToken<List<RewardRecordBean>>() {
                }.getType();
                recordBean.clear();
                recordBean = jsonUtil.analysis2List(data, type);
                if (recordBean.size() != 0) {
                    recordAdapter.setData(recordBean);
                    recordAdapter.notifyDataSetChanged();
                } else {
                    tv_nothing.setVisibility(View.VISIBLE);
                    linear_record_sub.setVisibility(View.GONE);
                }
            }
        });
    }

    /*
    初始化控件
     */
    private void initUI() {
        recordAdapter = new RecordAdapter(this, recordBean, R.layout.item_rewardrecord_list);
        list_record.setAdapter(recordAdapter);
    }

    /*
提现记录适配器
 */
    class RecordAdapter extends SuperAdapter<RewardRecordBean> {
        public RecordAdapter(Context context, List<RewardRecordBean> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, RewardRecordBean item, int position) {
            helper.setText(R.id.tv_record_list_date, item.getApply_date());
            helper.setText(R.id.tv_record_list_time, item.getApply_time());
            String num = item.getCash_number();
            StringBuilder sb = new StringBuilder(num);
            sb.insert(num.length() / 2, "\n");
            num = sb.toString();
            helper.setText(R.id.tv_record_list_number, num);
            helper.setText(R.id.tv_record_list_money, item.getCash_amount());
            TextView tv_state = helper.getView(R.id.tv_record_list_state);
            tv_state.setText(item.getStatus_name());
            int state = item.getStatus_id();
            if (state == 0) {
                tv_state.setTextColor(Color.BLACK);
            } else if (state == 1) {
                tv_state.setTextColor(Color.GREEN);
            } else {
                tv_state.setTextColor(Color.RED);
            }
        }
    }

    /*
 普通点击事件
  */
    @OnClick({R.id.iv_record_back, R.id.tv_againloading})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_record_back:
                finish();
                break;
            case R.id.tv_againloading:
                connecteNet();
                break;
        }
    }
}
