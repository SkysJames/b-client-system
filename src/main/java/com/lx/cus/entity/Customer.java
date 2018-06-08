package com.lx.cus.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "customer")
public class Customer implements BaseEntity<Integer> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "mobile_phone")
	private String mobilePhone;
	
	@Column(name = "tele_phone")
	private String telePhone;
	
	@Column(name = "qq")
	private String qq;
	
	@Column(name = "wechat")
	private String wechat;
	
	@Column(name = "level_id")
	private Integer levelId;
	
	@Column(name = "trade_id")
	private Integer tradeId;
	
	@Column(name = "company")
	private String company;
	
	@Column(name = "follow_user_id")
	private Integer followUserId;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user_id")
	private Integer createUserId;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "update_user_id")
	private Integer updateUserId;
	
	@Column(name = "is_share")
	private Boolean share;
	
	@Transient
	private String followUserEmpNo;
	
	public Boolean getShare() {
		return share;
	}

	public void setShare(Boolean share) {
		this.share = share;
	}

	@Transient
	private String startTime;
	
	@Transient
	private String endTime;
	
	@Transient
	private String levelName;
	
	@Transient
	private String tradeName;
	
	@Transient
	private String createTimeStr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getFollowUserId() {
		return followUserId;
	}

	public void setFollowUserId(Integer followUserId) {
		this.followUserId = followUserId;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFollowUserEmpNo() {
		return followUserEmpNo;
	}

	public void setFollowUserEmpNo(String followUserEmpNo) {
		this.followUserEmpNo = followUserEmpNo;
	}

}
