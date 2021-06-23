/**
 * Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.bean.user;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 陈浩
 * @version 1.0
 */
public class Jiaqi {

    private Integer id;//自增长
    private String amount;//加气量
    private String carno;//车牌号
    private String userId;//加气员编号
    private String jiaqiTime;//加气时间


    public String getJiaqiTime() {
        return jiaqiTime;
    }

    public void setJiaqiTime(String jiaqiTime) {
        this.jiaqiTime = jiaqiTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

}
