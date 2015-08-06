package com.retailerapp.ayesap;

import java.util.ArrayList;

public class Parent {

	String orderId, orderAmount, orderStatusBackend, currentStatusText,
			bookNowTime;
	int delTime;

	String customerMobile;
	String resName, resMobile;

	ArrayList<Child> children;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderStatusBackend() {
		return orderStatusBackend;
	}

	public void setOrderStatusBackend(String orderStatusBackend) {
		this.orderStatusBackend = orderStatusBackend;
	}

	public String getCurrentStatusText() {
		return currentStatusText;
	}

	public void setCurrentStatusText(String currentStatusText) {
		this.currentStatusText = currentStatusText;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResMobile() {
		return resMobile;
	}

	public void setResMobile(String resMobile) {
		this.resMobile = resMobile;
	}

	public ArrayList<Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Child> children) {
		this.children = children;
	}

	public String getBookNowTime() {
		return bookNowTime;
	}

	public void setBookNowTime(String bookNowTime) {
		this.bookNowTime = bookNowTime;
	}

	public int getDelTime() {
		return delTime;
	}

	public void setDelTime(int delTime) {
		this.delTime = delTime;
	}

}
