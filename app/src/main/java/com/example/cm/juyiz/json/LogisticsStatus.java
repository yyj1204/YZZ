package com.example.cm.juyiz.json;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class LogisticsStatus {
    private String logistics_status;//状态
    private String logistics_time;//时间

    public LogisticsStatus(String logistics_status, String logistics_time) {
        this.logistics_status = logistics_status;
        this.logistics_time = logistics_time;
    }

    public LogisticsStatus() {
    }

    public String getLogistics_status() {
        return logistics_status;
    }

    public void setLogistics_status(String logistics_status) {
        this.logistics_status = logistics_status;
    }

    public String getLogistics_time() {
        return logistics_time;
    }

    public void setLogistics_time(String logistics_time) {
        this.logistics_time = logistics_time;
    }
}
