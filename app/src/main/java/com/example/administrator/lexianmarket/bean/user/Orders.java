/**
 *  Copyright 2017  Chinasofti , Inc. All rights reserved.
 */
package com.example.administrator.lexianmarket.bean.user;


import com.example.administrator.lexianmarket.bean.commodity.Store;

import java.util.List;

/**
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 陈浩
 * @version 1.0
 */
public class Orders {
	private Integer id;
	private String orderNo;//订单编号
	private String userId;//订单所属用户id 引用 ：user.id
	private String storeNo;//引用：store.store_no; 订单取货门店
	private Double totalAmount;//订单总金额
	private String paymentType;//支付类型
	private String paymentSubtype;//支付子类型
	private long createTime;
	private String deliveryType;//配送类型
	private Integer states;//订单状态：1：待付款 2：待发货  3：已发货  4：已结单
	private String jiaqiAmount;
	private Store store;
	private User user;
	private String start;
	private String end;

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	private String total;
	private List<OrderItem> orderItems;


	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime=createTime;
	}
	public String getPaymentSubtype() {
		return paymentSubtype;
	}
	public void setPaymentSubtype(String paymentSubtype) {
		this.paymentSubtype = paymentSubtype;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getJiaqiAmount() {
		return jiaqiAmount;
	}

	public void setJiaqiAmount(String jiaqiAmount) {
		this.jiaqiAmount = jiaqiAmount;
	}
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "Orders [id=" + id + ", orderNo=" + orderNo + ", userId=" + userId + ", storeNo=" + storeNo
				+ ", totalAmount=" + totalAmount + ", paymentType=" + paymentType + ", paymentSubtype=" + paymentSubtype
				+ ", createTime=" + createTime + ", deliveryType=" + deliveryType + ", states=" + states + ", jiaqiAmount =" + jiaqiAmount + ",store="
				+ store + ", user=" + user + "]";
	}
	
	
}
