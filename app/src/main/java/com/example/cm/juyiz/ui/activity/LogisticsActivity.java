package com.example.cm.juyiz.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.json.Logistics;
import com.example.cm.juyiz.json.LogisticsStatus;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.util.OkHttpUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class LogisticsActivity extends BaseActivity {

    private ListAdapter adapter;
    private Logistics logistics;


    @Bind(R.id.logistics_lv)
    ListView4ScrollView logistics_lv;
    @Bind(R.id.logistics_name)
    TextView logistics_name;
    @Bind(R.id.logistics_id)
    TextView logistics_id;
    @Bind(R.id.logistics_status)
    TextView logistics_status;
    @Bind(R.id.logistics_tel)
    TextView logistics_tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        ButterKnife.bind(this);
        logistics = new Logistics();
        getLogistics();


    }


    private void getLogistics() {
        //String Url = "http://apis.baidu.com/netpopo/express/express1?type=" + type + "&number=" + id;


        String Url = "https://ali-deliver.showapi.com/showapi_expInfo?com=zhongtong&nu=437376193230";
        OkHttpUtil httpUtils = new OkHttpUtil();
        Map<String, Object> querys = new HashMap<String, Object>();
        querys.put("expName", "中通");

        httpUtils.getHeader(Url, "Authorization", "APPCODE 36c3661d6f8240a581863eb88feb146e", new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.e("csm", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject showapi_res_body = jsonObject.getJSONObject("showapi_res_body");
                    String number = showapi_res_body.getString("mailNo");
                    logistics.setLogistics_id(number);
                    logistics.setLogistics_type("中通快递");
                    int status = showapi_res_body.getInt("status");
                    logistics.setLogistics_deliverystatus(status);
                    String tel = showapi_res_body.getString("tel");
                    logistics.setTel(tel);
                    JSONArray jsonArray = showapi_res_body.getJSONArray("data");
                    List<LogisticsStatus> logisticsStatuses = new ArrayList<LogisticsStatus>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        LogisticsStatus logisticsStatus = new LogisticsStatus();
                        String context = json.getString("context");
                        String time = json.getString("time");
                        logisticsStatus.setLogistics_status(context);
                        logisticsStatus.setLogistics_time(time);
                        logisticsStatuses.add(logisticsStatus);
                    }
                    logistics.setLogistics_status(logisticsStatuses);


                    logistics_name.setText(logistics.getLogistics_type());
                    logistics_id.setText(logistics.getLogistics_id());
                    //-1 待查询 0 查询异常 1 暂无记录 2 在途中 3 派送中 4 已签收 5 用户拒签 6 疑难件 7 无效单 8 超时单 9 签收失败 10 退回
                    String statu = "";
                    switch (logistics.getLogistics_deliverystatus()) {
                        case -1:
                            statu = "待查询";
                            break;
                        case 0:
                            statu = "查询异常";
                            break;
                        case 1:
                            statu = "暂无记录";
                            break;
                        case 2:
                            statu = "在途中";
                            break;
                        case 3:
                            statu = "派送中";
                            break;
                        case 4:
                            statu = "已签收";
                            break;
                        case 5:
                            statu = "用户拒签";
                            break;
                        case 6:
                            statu = "疑难件";
                            break;
                        case 7:
                            statu = "无效单";
                            break;
                        case 8:
                            statu = "超时单";
                            break;
                        case 9:
                            statu = "签收失败";
                            break;
                        case 10:
                            statu = "退回";
                            break;

                    }
                    logistics_status.setText(statu);
                    logistics_tel.setText(logistics.getTel());

                    adapter = new ListAdapter(LogisticsActivity.this, logistics.getLogistics_status(), R.layout.logistics_item);
                    logistics_lv.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });


    }


    class ListAdapter extends SuperAdapter<LogisticsStatus> {
        public ListAdapter(Context context, List<LogisticsStatus> mdaList, int mLayoutId) {
            super(context, mdaList, mLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final LogisticsStatus item, final int position) {

            String logistics_status = item.getLogistics_status();
            String logistics_time = item.getLogistics_time();

            helper.setText(R.id.logistics_status_tv, logistics_status);
            helper.setText(R.id.logistics_time_tv, logistics_time);

        }
    }
}
