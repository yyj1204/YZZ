package com.example.cm.juyiz.bean;

import java.io.Serializable;

/**
 * 商城首页 --- 顶部菜单类型
 */
public class HomeTypeBean implements Serializable {

    private int id;
    private String list_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }
}
