package com.example.administrator.lexianmarket.bean;

/**
 * Created by yhz on 2020/5/28.
 */

import java.util.Date;
import java.util.List;


public class JiaqiCar {
    private Integer id;
    private String carno;
    private long inTime;
    private long outTime;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCarno() {
        return carno;
    }
    public void setCarno(String carno) {
        this.carno = carno;
    }
    public long getInTime() {
        return inTime;
    }
    public void setInTime(long inTime) {
        this.inTime = inTime;
    }
    public long getOutTime() {
        return outTime;
    }
    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }



}
