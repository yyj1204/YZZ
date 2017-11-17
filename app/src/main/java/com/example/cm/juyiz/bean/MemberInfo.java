package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public class MemberInfo {
    private int id;
    private int member_id;
    private String mobile;
    private String wechat_nickname;
    private String wechat_headimgurl;
    private float purchase_amount;
    private MemberFrom from_id;
    private String share_qrcode_url;
    private int share_fans_total;
    private float share_sales_amount;
    private float share_reward_amount;


    public MemberInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWechat_nickname() {
        return wechat_nickname;
    }

    public void setWechat_nickname(String wechat_nickname) {
        this.wechat_nickname = wechat_nickname;
    }

    public String getWechat_headimgurl() {
        return wechat_headimgurl;
    }

    public void setWechat_headimgurl(String wechat_headimgurl) {
        this.wechat_headimgurl = wechat_headimgurl;
    }

    public float getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(float purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public MemberFrom getFrom_id() {
        return from_id;
    }

    public void setFrom_id(MemberFrom from_id) {
        this.from_id = from_id;
    }

    public String getShare_qrcode_url() {
        return share_qrcode_url;
    }

    public void setShare_qrcode_url(String share_qrcode_url) {
        this.share_qrcode_url = share_qrcode_url;
    }

    public int getShare_fans_total() {
        return share_fans_total;
    }

    public void setShare_fans_total(int share_fans_total) {
        this.share_fans_total = share_fans_total;
    }

    public float getShare_sales_amount() {
        return share_sales_amount;
    }

    public void setShare_sales_amount(float share_sales_amount) {
        this.share_sales_amount = share_sales_amount;
    }

    public float getShare_reward_amount() {
        return share_reward_amount;
    }

    public void setShare_reward_amount(float share_reward_amount) {
        this.share_reward_amount = share_reward_amount;
    }
}



