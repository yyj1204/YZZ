package com.example.cm.juyiz.bean;
/**
 * 限时特价界面 --- 时间
 */
public class FlashSaleTimeBean {
    private int id;
    private long start_time;
    private long stop_time;
    private int flash_type;//抢购状态:0,结束；1：正在抢购；2：未开始


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getStop_time() {
        return stop_time;
    }

    public void setStop_time(long stop_time) {
        this.stop_time = stop_time;
    }

    public int getFlash_type() {
        return flash_type;
    }

    public void setFlash_type(int flash_type) {
        this.flash_type = flash_type;
    }

}
