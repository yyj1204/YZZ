package com.example.cm.juyiz.ui.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.adapter.AddressManageAdapter;
import com.example.cm.juyiz.adapter.impl.AddressManagerView;
import com.example.cm.juyiz.bean.Address;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.Result;
import com.example.cm.juyiz.customwidget.ListView4ScrollView;
import com.example.cm.juyiz.ui.dialog.LoadingDialog;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class AddressManageActivity extends BaseActivity implements AddressManagerView {
    private List<Address> addresslist;
    private AddressManageAdapter newReleaseAdapter;

    @Bind(R.id.address_add)
    LinearLayout addaddress;
    @Bind(R.id.address_list)
    ListView4ScrollView address_lv;

    private Member user;

    private LoadingDialog dialog;
    private LoadingDialog.Builder builder;

    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage);
        ButterKnife.bind(this);
        user = LoginUtil.getinit().getMember();
        addresslist = new ArrayList<Address>();
        builder = new LoadingDialog.Builder(this);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        initData();
        inItUI();
    }

    private void initData() {
        getAddress();
    }

    private void getAddress() {
        dialog = builder.createSingleButtonDialog();
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getMemberid());
        new OkHttpUtil().post(DataUrl.URL_AddressGet, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(final String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<List<Address>>() {
                }.getType();
                addresslist = jsonUtil.analysis2List(data, listType);

                newReleaseAdapter.setData(addresslist);
                newReleaseAdapter.notifyDataSetChanged();
                builder.dismiss();
                dialog.dismiss();

            }
        });
    }

    private void inItUI() {
        newReleaseAdapter = new AddressManageAdapter(this, addresslist, R.layout.address_manage_item, user, addresslist, this);
        address_lv.setAdapter(newReleaseAdapter);
        address_lv.setFocusable(false);
        address_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                  if (from.equals("order")) {
                                                      Intent intent = new Intent(AddressManageActivity.this, OrderDetailActivity.class);
                                                      intent.putExtra("address", addresslist.get(position));
                                                      setResult(MyConstant.SELECT_ADDRESS, intent);
                                                      finish();
                                                  }
                                              }
                                          }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == MyConstant.EDIT_ADDRESS) {
            if (data != null) {
                Address address = (Address) data.getSerializableExtra("address");
                for (int i = 0; i < addresslist.size(); i++) {
                    if (address.getIs_default() == 1) {
                        addresslist.get(i).setIs_default(0);
                    }
                    if (addresslist.get(i).getId() == address.getId()) {
                        addresslist.set(i, address);
                    }
                }
                newReleaseAdapter.setData(addresslist);
                newReleaseAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == MyConstant.ADD_ADDRESS) {
            addresslist.clear();
            getAddress();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.address_add)
    public void gotoaddAddress() {
        Intent intent = new Intent(AddressManageActivity.this, AddAddressActivity.class);
        intent.putExtra("from", "manage");
        startActivityForResult(intent, 1);

    }

    @OnClick(R.id.address_break)
    public void goback() {
        if (from.equals("order")) {
            setResult(MyConstant.SELECT_ADDRESS);
        }
        finish();
    }

    @Override
    public void setDefaultListener(Address address, final int position) {
        Map map = new HashMap();
        map.put("address_id", address.getId());
        map.put("member_id", user.getMemberid());
        new OkHttpUtil().post(DataUrl.URL_AddressDefault, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<Result>() {
                }.getType();
                Result result = (Result) jsonUtil.analysis2Objiect(data, listType);
                if (result.getCode() == 1) {
                    for (Address address : addresslist) {
                        address.setIs_default(0);
                    }
                    addresslist.get(position).setIs_default(1);
                }
                newReleaseAdapter.setData(addresslist);
                newReleaseAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void setEditListener(Address address) {
        Intent intent = new Intent(AddressManageActivity.this, AddAddressActivity.class);
        intent.putExtra("AddressBean", address);
        startActivityForResult(intent, 0);
    }

    @Override
    public void setDeleteListener(Address address, final int position) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address_id", address.getId());
        new OkHttpUtil().post(DataUrl.URL_AddressDelete, map, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                JsonUtil jsonUtil = new JsonUtil();
                Type listType = new TypeToken<Result>() {
                }.getType();
                Result result = (Result) jsonUtil.analysis2Objiect(data, listType);
                if (result.getCode() == 1) {
                    addresslist.remove(position);
                }
                newReleaseAdapter.setData(addresslist);
                newReleaseAdapter.notifyDataSetChanged();
            }
        });
    }

}
