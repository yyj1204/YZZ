package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class Result {
    private int code;
    private String result;

    public Result() {
    }

    public Result(int code, String result) {
        this.code = code;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
