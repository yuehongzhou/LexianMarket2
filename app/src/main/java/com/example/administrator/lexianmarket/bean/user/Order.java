package com.example.administrator.lexianmarket.bean.user;

public class Order {
    private Jiaqi jiaqi;
    private Pay pay;
    private String start;//查询开始时间
    private String end;//查询结束时间

    public Pay getPay() {
        return pay;
    }

    public void setPay(Pay pay) {
        this.pay = pay;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Jiaqi getJiaqi() {
        return jiaqi;
    }

    public void setJiaqi(Jiaqi jiaqi) {
        this.jiaqi = jiaqi;
    }
}
