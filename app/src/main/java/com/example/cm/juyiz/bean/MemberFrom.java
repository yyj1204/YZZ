package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class MemberFrom {
    private String mobile;
    private String wechat_nickname;

    public MemberFrom(String mobile, String wechat_nickname) {
        this.mobile = mobile;
        this.wechat_nickname = wechat_nickname;
    }

    public MemberFrom() {
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


}
