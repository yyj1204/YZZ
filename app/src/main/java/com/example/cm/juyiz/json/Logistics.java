package com.example.cm.juyiz.json;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class Logistics {
    private int lid;//id
    private String logistics_id;//运单编号
    private String logistics_type;//快递公司
    private int logistics_deliverystatus;//物流状态
    private List<LogisticsStatus> logistics_status;//快递状态，时间地点
    private String tel;

    public Logistics() {
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getLogistics_id() {
        return logistics_id;
    }

    public void setLogistics_id(String logistics_id) {
        this.logistics_id = logistics_id;
    }

    public String getLogistics_type() {
        return logistics_type;
    }

    public void setLogistics_type(String logistics_type) {
        this.logistics_type = logistics_type;
    }

    public int getLogistics_deliverystatus() {
        return logistics_deliverystatus;
    }

    public void setLogistics_deliverystatus(int logistics_deliverystatus) {
        this.logistics_deliverystatus = logistics_deliverystatus;
    }

    public List<LogisticsStatus> getLogistics_status() {
        return logistics_status;
    }

    public void setLogistics_status(List<LogisticsStatus> logistics_status) {
        this.logistics_status = logistics_status;
    }
}
