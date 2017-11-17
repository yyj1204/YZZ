package com.example.cm.juyiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.cm.juyiz.R;
import com.example.cm.juyiz.adapter.impl.AddressManagerView;
import com.example.cm.juyiz.bean.Address;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.superadapter.SuperAdapter;
import com.example.cm.juyiz.superadapter.ViewHolder;
import com.example.cm.juyiz.ui.activity.AddAddressActivity;
import com.example.cm.juyiz.ui.activity.AddressManageActivity;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class AddressManageAdapter extends SuperAdapter<Address> {
    private Member user;
    private List<Address> addresses;
    private Context context;
    private AddressManagerView addressManagerView;

    public AddressManageAdapter(Context context, List<Address> mdaList, int mLayoutId, Member user, List<Address> addresses, AddressManagerView addressManagerView) {
        super(context, mdaList, mLayoutId);
        this.context = context;
        this.user = user;
        this.addresses = addresses;
        this.addressManagerView = addressManagerView;
    }

    @Override
    public void setData(List<Address> mdaList) {
        super.setData(mdaList);
    }

    @Override
    public void convert(ViewHolder helper, final Address item, final int position) {
        TextView address_default = (TextView) helper.getView(R.id.address_default);
        TextView address_setdefault = (TextView) helper.getView(R.id.address_setdefault);
        TextView address_edit = (TextView) helper.getView(R.id.address_edit);
        TextView address_detele = (TextView) helper.getView(R.id.address_delete);


        helper.setText(R.id.address_consignee, item.getConsignee());
        helper.setText(R.id.address_mobile, item.getMobile());
        helper.setText(R.id.address_address, item.getProvince() + item.getCity() + item.getRegions() + item.getAddress());
        if (item.getIs_default() == 1) {
            address_default.setVisibility(View.VISIBLE);
        } else {
            address_default.setVisibility(View.GONE);
        }
        address_setdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressManagerView.setDefaultListener(item, position);
            }
        });
        address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressManagerView.setEditListener(item);
            }
        });
        address_detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressManagerView.setDeleteListener(item, position);

            }
        });
    }
}