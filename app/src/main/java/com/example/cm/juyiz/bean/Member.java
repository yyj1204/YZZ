package com.example.cm.juyiz.bean;

import com.example.cm.juyiz.util.AESUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/2 0002.
 */

public class Member implements Serializable {
    private String memberid;
    private String mobile;
    private int is_auth;


    public Member(String memberid, String mobile) {
        this.memberid = memberid;
        this.mobile = mobile;
    }

    public Member() {
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }
}
