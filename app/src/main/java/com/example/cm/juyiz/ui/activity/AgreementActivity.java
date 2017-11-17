package com.example.cm.juyiz.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class AgreementActivity extends BaseActivity {
    @Bind(R.id.agreement_title)
    TextView title;
    @Bind(R.id.agreement_details)
    TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);
        new OkHttpUtil().get(DataUrl.URL_ServicesProtocols, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    int ret = object.getInt("ret");
                    if (ret == 200) {
                        JSONObject obj = object.getJSONObject("data");
                        title.setText(obj.getString("title"));
                        details.setText(obj.getString("details"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick(R.id.iv_agreement_back)
    public void goback() {
        finish();
    }
}
