package com.example.cm.juyiz.bean;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class Price {
    private String title;
    private int level;
    private float price;
    private long stop_time;

    public Price() {
    }

    public Price(String title, int level, float price, long stop_time) {
        this.title = title;
        this.level = level;
        this.price = price;
        this.stop_time = stop_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getStop_time() {
        return stop_time;
    }

    public void setStop_time(long stop_time) {
        this.stop_time = stop_time;
    }
}
