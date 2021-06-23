package com.example.administrator.lexianmarket.bean.user;

import java.util.Date;

public class JiaqiCar {

    private Integer id;
    private String carno;
    private Date intime;
    private Date outtime;

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
    public Date getInTime() {
        return intime;
    }
    public void setInTime(Date inTime) {
        this.intime = inTime;
    }
    public Date getOutTime() {
        return outtime;
    }
    public void setOutTime(Date outTime) {
        this.outtime = outTime;
    }
}
