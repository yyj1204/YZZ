package com.example.cm.juyiz.ui.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cm.juyiz.BaseActivity;
import com.example.cm.juyiz.R;
import com.example.cm.juyiz.bean.Address;
import com.example.cm.juyiz.bean.Member;
import com.example.cm.juyiz.bean.Result;
import com.example.cm.juyiz.customwidget.MyToast;
import com.example.cm.juyiz.customwidget.WheelView;
import com.example.cm.juyiz.json.CityInfoModel;
import com.example.cm.juyiz.json.DistrictInfoModel;
import com.example.cm.juyiz.json.ProvinceInfoModel;
import com.example.cm.juyiz.util.AddrXmlParser;
import com.example.cm.juyiz.util.DataUrl;
import com.example.cm.juyiz.util.JsonUtil;
import com.example.cm.juyiz.util.LoginUtil;
import com.example.cm.juyiz.util.MyConstant;
import com.example.cm.juyiz.util.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    protected ArrayList<String> mProvinceDatas;
    protected Map<String, ArrayList<String>> mCitisDatasMap = new HashMap<String, ArrayList<String>>();
    protected Map<String, ArrayList<String>> mDistrictDatasMap = new HashMap<String, ArrayList<String>>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName;

    private View contentView;
    private PopupWindow addrPopWindow;
    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mCountyPicker;
    private LinearLayout boxBtnCancel;
    private LinearLayout boxBtnOk;
    protected boolean isDataLoaded = false;
    /**
     * 选择地址
     */
    private LinearLayout ll_select_province;
    private TextView tv_province;
    private LinearLayout rootview;
    private boolean isAddrChoosed = false;


    private boolean isUpdate;
    private Address address;

    @Bind(R.id.address_name_et)
    EditText et_name;
    @Bind(R.id.address_mobile_et)
    EditText et_mobile;
    @Bind(R.id.address_detail_et)
    EditText et_detail_address;
    @Bind(R.id.address_submit)
    TextView submit;
    @Bind(R.id.address_title)
    TextView address_title;
    @Bind(R.id.isdefault)
    ImageView isdefault;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        ButterKnife.bind(this);

        user = LoginUtil.getinit().getMember();
        initView();
    }

    @OnClick(R.id.address_submit)
    public void submit() {

        if (isUpdate) {

            Map<String, Object> map = new HashMap<>();
            map.put("address_id", address.getId());
            String consignee = et_name.getText().toString().trim();
            if (consignee == null || "".equals(consignee)) {
                MyToast.makeText(AddAddressActivity.this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("consignee", consignee);
                address.setConsignee(consignee);
            }
            String mobile = et_mobile.getText().toString().trim();
            if (mobile == null || "".equals(mobile)) {
                MyToast.makeText(AddAddressActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("mobile", mobile);
                address.setMobile(mobile);
            }
            map.put("province", address.getProvince());
            map.put("city", address.getCity());
            map.put("regions", address.getRegions());
            String detail = et_detail_address.getText().toString().trim();
            if (detail == null || "".equals(detail)) {
                MyToast.makeText(AddAddressActivity.this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("address", detail);
                address.setAddress(detail);
            }

            map.put("is_default", isdefault.isSelected() ? 1 : 0);
            address.setIs_default(isdefault.isSelected() ? 1 : 0);
            map.put("member_id", user.getMemberid());
            new OkHttpUtil().post(DataUrl.URL_AddressUpdate, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    JsonUtil jsonUtil = new JsonUtil();
                    Type listType = new TypeToken<Result>() {
                    }.getType();
                    Result result = (Result) jsonUtil.analysis2Objiect(data, listType);
                    if (result.getCode() == 1) {
                        Intent intent = new Intent(AddAddressActivity.this, AddressManageActivity.class);
                        intent.putExtra("address", address);
                        setResult(MyConstant.EDIT_ADDRESS, intent);
                        finish();
                    }
                }
            });


        } else {
            Map<String, Object> map = new HashMap<>();
            String consignee = et_name.getText().toString().trim();
            if (consignee == null || "".equals(consignee)) {
                MyToast.makeText(AddAddressActivity.this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("consignee", consignee);
                address.setConsignee(consignee);
            }
            String mobile = et_mobile.getText().toString().trim();
            if (mobile == null || "".equals(mobile)) {
                MyToast.makeText(AddAddressActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("mobile", mobile);
                address.setMobile(mobile);
            }
            map.put("province", address.getProvince());
            map.put("city", address.getCity());
            map.put("regions", address.getRegions());
            String detail = et_detail_address.getText().toString().trim();
            if (detail == null || "".equals(detail)) {
                MyToast.makeText(AddAddressActivity.this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                map.put("address", detail);
                address.setAddress(detail);
            }

            map.put("is_default", isdefault.isSelected() ? 1 : 0);
            address.setIs_default(isdefault.isSelected() ? 1 : 0);
            map.put("member_id", user.getMemberid());
            new OkHttpUtil().post(DataUrl.URL_AddressAdd, map, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    //解析json
                    try {
                        JSONObject object = new JSONObject(data);
                        JSONObject obj = object.getJSONObject("data");
                        address.setId(obj.getInt("address_id"));
                        int code = obj.getInt("code");
                        if (code == 1) {
                            //返回处理
                            Intent i = getIntent();
                            String from = i.getStringExtra("from");
                            if (from.equals("order")) {
                                Intent order = new Intent(AddAddressActivity.this, OrderDetailActivity.class);
                                order.putExtra("address", address);
                                setResult(MyConstant.ADD_ADDRESS, order);
                                finish();
                            } else if (from.equals("manage")) {
                                Intent manage = new Intent(AddAddressActivity.this, AddressManageActivity.class);
                                manage.putExtra("address", address);
                                setResult(MyConstant.ADD_ADDRESS, manage);
                                finish();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    @OnClick(R.id.isdefault)
    public void isdefault() {
        if (isdefault.isSelected()) {
            isdefault.setSelected(false);
        } else {
            isdefault.setSelected(true);
        }
    }

    @OnClick(R.id.address_goback)
    public void goback() {
        finish();
    }


    public void initView() {
        ll_select_province = (LinearLayout) findViewById(R.id.ll_select_province);
        tv_province = (TextView) findViewById(R.id.tv_province);
        ll_select_province.setOnClickListener(AddAddressActivity.this);
        rootview = (LinearLayout) findViewById(R.id.root_view);

        initProviceSelectView();
        Intent intent = getIntent();
        address = (Address) intent.getSerializableExtra("AddressBean");
        if (address != null) {
            isUpdate = true;
            et_name.setText(address.getConsignee());
            et_mobile.setText(address.getMobile());
            et_detail_address.setText(address.getAddress());
            tv_province.setText(address.getProvince() + address.getCity() + address.getRegions());
            isdefault.setSelected(address.getIs_default() == 1);
        } else {
            address = new Address();
        }
        if (isUpdate) {
            address_title.setText("编辑地址");
            submit.setText("确认修改");
        }
    }

    private void initProviceSelectView() {

        contentView = LayoutInflater.from(this).inflate(
                R.layout.addr_picker, null);
        mProvincePicker = (WheelView) contentView.findViewById(R.id.province);
        mCityPicker = (WheelView) contentView.findViewById(R.id.city);
        mCountyPicker = (WheelView) contentView.findViewById(R.id.county);
        boxBtnCancel = (LinearLayout) contentView.findViewById(R.id.box_btn_cancel);
        boxBtnOk = (LinearLayout) contentView.findViewById(R.id.box_btn_ok);


        addrPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //addrPopWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
        addrPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String provinceText = mProvinceDatas.get(id);
                if (!mCurrentProviceName.equals(provinceText)) {
                    mCurrentProviceName = provinceText;
                    ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                    mCityPicker.resetData(mCityData);
                    mCityPicker.setDefault(0);
                    mCurrentCityName = mCityData.get(0);

                    ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mDistrictData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mDistrictData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                String cityText = mCityData.get(id);
                if (!mCurrentCityName.equals(cityText)) {
                    mCurrentCityName = cityText;
                    ArrayList<String> mCountyData = mDistrictDatasMap.get(mCurrentCityName);
                    mCountyPicker.resetData(mCountyData);
                    mCountyPicker.setDefault(0);
                    mCurrentDistrictName = mCountyData.get(0);
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mCountyPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                String districtText = mDistrictData.get(id);
                if (!mCurrentDistrictName.equals(districtText)) {
                    mCurrentDistrictName = districtText;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        boxBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrPopWindow.dismiss();
            }
        });

        boxBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddrChoosed = true;
                String addr = mCurrentProviceName + mCurrentCityName;
                if (!mCurrentDistrictName.equals("其他")) {
                    addr += mCurrentDistrictName;
                }
                tv_province.setText(addr);

                address.setProvince(mCurrentProviceName);
                address.setCity(mCurrentCityName);
                address.setRegions(mCurrentDistrictName);
                // tvAddr.setTextColor(Color.parseColor("#000000"));
                addrPopWindow.dismiss();
            }
        });

        // 启动线程读取数据
        new Thread() {
            @Override
            public void run() {
                // 读取数据
                isDataLoaded = readAddrDatas();

                // 通知界面线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mProvincePicker.setData(mProvinceDatas);
                        mProvincePicker.setDefault(0);
                        mCurrentProviceName = mProvinceDatas.get(0);

                        ArrayList<String> mCityData = mCitisDatasMap.get(mCurrentProviceName);
                        mCityPicker.setData(mCityData);
                        mCityPicker.setDefault(0);
                        mCurrentCityName = mCityData.get(0);

                        ArrayList<String> mDistrictData = mDistrictDatasMap.get(mCurrentCityName);
                        mCountyPicker.setData(mDistrictData);
                        mCountyPicker.setDefault(0);
                        mCurrentDistrictName = mDistrictData.get(0);
                    }
                });
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_select_province:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (isDataLoaded)
                    addrPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                else
                    //toast("加载数据失败，请稍候再试！");
                    break;

                break;
        }
    }


    /**
     * 读取地址数据，请使用线程进行调用
     *
     * @return
     */
    protected boolean readAddrDatas() {
        List<ProvinceInfoModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            AddrXmlParser handler = new AddrXmlParser();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityInfoModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictInfoModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            mProvinceDatas = new ArrayList<String>();
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityInfoModel> cityList = provinceList.get(i).getCityList();
                ArrayList<String> cityNames = new ArrayList<String>();
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames.add(cityList.get(j).getName());
                    List<DistrictInfoModel> districtList = cityList.get(j).getDistrictList();
                    ArrayList<String> distrinctNameArray = new ArrayList<String>();
                    DistrictInfoModel[] distrinctArray = new DistrictInfoModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictInfoModel districtModel = new DistrictInfoModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray.add(districtModel.getName());
                    }
                    mDistrictDatasMap.put(cityNames.get(j), distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

}
