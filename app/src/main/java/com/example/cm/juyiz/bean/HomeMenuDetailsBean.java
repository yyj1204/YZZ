package com.example.cm.juyiz.bean;
/**
 * 商城首页 --- 顶部其他菜单类型的数据
 */
import java.util.List;
public class HomeMenuDetailsBean {
    private String main_title;
    private String sub_title;
    private List<MenuDetailsBean> goods_id;

    public String getMain_title() {
        return main_title;
    }

    public void setMain_title(String main_title) {
        this.main_title = main_title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public List<MenuDetailsBean> getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(List<MenuDetailsBean> goods_id) {
        this.goods_id = goods_id;
    }
}
