package com.lx.cus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sale_order")
public class SaleOrder implements BaseEntity<Integer> {

	private static final long serialVersionUID = 1L;
	
	public static class Type {
		
		public static final String ADVERTISING = "广告订单";
		
		public static final String REFUND = "退款订单";
		
	}
	
	public static class Status {
		//未付款、已付款、已完结、代理商已退款和客户已退款。
		public static final String PAYMENT_NON = "未付款";
		
		public static final String PAYMENT_BEEN = "已付款";
		
		public static final String PAYMENT_FINISH = "已完结";
		
		public static final String AGENT_REFUND = "代理商已退款";
		
		public static final String CUSTOMER_REFUND = "客户已退款";
		
		public static final String ORDER_INVALID = "订单作废";
		
	}
	
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "customer_id")
	private Integer customerId;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "advertising_money")
	private BigDecimal advertisingMoney;
	
	@Column(name = "customer_rebates")
	private BigDecimal customerRebates;
	
	@Column(name = "cost_rebates")
	private BigDecimal costRebates;
	
	@Column(name = "customer_pay")
	private BigDecimal customerPay;
	
	@Column(name = "cost_price")
	private BigDecimal costPrice;
	
	@Column(name = "gross_profit")
	private BigDecimal grossProfit;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user_id")
	private Integer createUserId;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "update_user_id")
	private Integer updateUserId;
	
	@Transient
	private String createTimeStr;
	
	@Transient
	private String productName;
	
	@Transient
	private String customerName;
	
	@Transient
	private String customerMobilePhone;
	
	@Transient
	private String userName;
	
	@Transient
	private String startTime;
	
	@Transient
	private String endTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAdvertisingMoney() {
		return advertisingMoney;
	}

	public void setAdvertisingMoney(BigDecimal advertisingMoney) {
		this.advertisingMoney = advertisingMoney;
	}

	public BigDecimal getCustomerRebates() {
		return customerRebates;
	}

	public void setCustomerRebates(BigDecimal customerRebates) {
		this.customerRebates = customerRebates;
	}

	public BigDecimal getCostRebates() {
		return costRebates;
	}

	public void setCostRebates(BigDecimal costRebates) {
		this.costRebates = costRebates;
	}

	public BigDecimal getCustomerPay() {
		return customerPay;
	}

	public void setCustomerPay(BigDecimal customerPay) {
		this.customerPay = customerPay;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobilePhone() {
		return customerMobilePhone;
	}

	public void setCustomerMobilePhone(String customerMobilePhone) {
		this.customerMobilePhone = customerMobilePhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
}
