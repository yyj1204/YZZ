package com.example.cm.juyiz.util;


import com.example.cm.juyiz.bean.Address;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class JsonUtil<T> {

    public T analysis2Objiect(String json, Type listType) {
        T obj;
        try {
            JSONObject object = new JSONObject(json);
            int ret = object.getInt("ret");
            if (ret == 200) {
                JSONObject jsonObject = object.getJSONObject("data");
                Gson gson = new GsonBuilder().create();
                obj = gson.fromJson(jsonObject.toString(), listType);
                return obj;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<T> analysis2List(String json, Type listType) {
        List<T> list;
        try {
            JSONObject object = new JSONObject(json);
            int ret = object.getInt("ret");
            if (ret == 200) {
                JSONArray array = object.getJSONArray("data");
                Gson gson = new GsonBuilder().create();
                list = gson.fromJson(array.toString(), listType);
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
