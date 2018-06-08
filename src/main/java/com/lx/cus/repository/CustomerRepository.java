package com.lx.cus.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Customer;
import com.lx.cus.entity.User;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;

@Repository
public class CustomerRepository extends BaseEntityRepository<Customer, Integer> {

	public DataGrid<Customer> search(Customer customer, int page, int rows) {
		boolean isCustomerManager = SecurityUtils.containsPermission(ApplicationConsts.Auth.CUSTOMER_MANAGER);
		User user = SecurityUtils.getCurrentUser();
		Map<String, Object> params = new HashMap<>();
		String sql = "SELECT c.id, c.`name`, c.company, c.qq, c.wechat, c.tele_phone telePhone, c.mobile_phone mobilePhone, " + 
			         "DATE_FORMAT(c.create_time, '%Y-%m-%d %T') createTimeStr, l.`name` levelName, t.`name` tradeName " +
			         "FROM customer c, cust_level l, cust_trade t WHERE c.level_id = l.id AND c.trade_id = t.id";
		if (!isCustomerManager) {
			sql += " and (c.follow_user_id = :userId or c.is_share = 1) ";
			params.put("userId", user.getId());
		}
		
		if (StringUtils.hasText(customer.getName())) {
			sql += " and c.name like :name ";
			params.put("name", "%" + customer.getName() + "%");
		}
		
		if (StringUtils.hasText(customer.getMobilePhone())) {
			sql += " and c.mobile_phone like :mobilePhone ";
			params.put("mobilePhone", "%" + customer.getMobilePhone() + "%");
		}
		
		if (StringUtils.hasText(customer.getStartTime()) && StringUtils.hasText(customer.getEndTime())) {
			sql += " and c.create_time >= :startTime and c.create_time <= :endTime ";
			params.put("startTime", customer.getStartTime());
			params.put("endTime", customer.getEndTime());
		} else if (StringUtils.hasText(customer.getStartTime())) {
			sql += " and c.create_time >= :startTime  ";
			params.put("startTime", customer.getStartTime());
		} else if (StringUtils.hasText(customer.getEndTime())) {
			sql += " and c.create_time <= :endTime ";
			params.put("endTime", customer.getEndTime());
		}
		
		return this.listByNativeSqlPage2(sql, params, page, rows);
	}

}
