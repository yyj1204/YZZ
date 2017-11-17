package com.example.cm.juyiz.bean;

/**
 * 提现记录界面
 */
public class RewardRecordBean {
    private String apply_time;
    private String apply_date;
    private String cash_number;
    private String cash_amount;
    private int status_id;
    private String status_name;

    public String getApply_date() {
        return apply_date;
    }

    public void setApply_date(String apply_date) {
        this.apply_date = apply_date;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getCash_number() {
        return cash_number;
    }

    public void setCash_number(String cash_number) {
        this.cash_number = cash_number;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
