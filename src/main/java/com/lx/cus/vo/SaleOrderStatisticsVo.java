package com.lx.cus.vo;

import java.io.Serializable;

public class SaleOrderStatisticsVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String type;
	
	private String grossProfit;
	
	private String nonPayProfit;
	
	private String paidProfit;
	
	private String customerPay;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(String grossProfit) {
		this.grossProfit = grossProfit;
	}

	public String getNonPayProfit() {
		return nonPayProfit;
	}

	public void setNonPayProfit(String nonPayProfit) {
		this.nonPayProfit = nonPayProfit;
	}

	public String getPaidProfit() {
		return paidProfit;
	}

	public void setPaidProfit(String paidProfit) {
		this.paidProfit = paidProfit;
	}

	public String getCustomerPay() {
		return customerPay;
	}

	public void setCustomerPay(String customerPay) {
		this.customerPay = customerPay;
	}
	
}
