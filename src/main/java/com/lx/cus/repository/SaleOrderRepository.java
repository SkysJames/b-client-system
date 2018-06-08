package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.SaleOrder;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.util.BeanUtil;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.SaleOrderStatisticsVo;

@Repository
public class SaleOrderRepository extends BaseEntityRepository<SaleOrder, Integer> {

	public DataGrid<SaleOrder> search(SaleOrder saleOrder, int page, int rows) {
		String sql = "select o.id, o.type, o.`code`, DATE_FORMAT(o.create_time, '%Y-%m-%d %T') createTimeStr," +
					       "p.`name` productName, u.`name` userName, c.`name` customerName," +
					       "c.mobile_phone customerMobilePhone, o.account, o.remark, o.`status`," +
					       "o.cost_price costPrice, o.customer_pay customerPay, o.gross_profit grossProfit " +
					 "from sale_order o, customer c, product p, sys_user u " +
					 "where o.customer_id = c.id and o.product_id = p.id and o.create_user_id = u.id and o.is_valid = 1 ";
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.hasText(saleOrder.getType())) {
			sql += " and o.type = :type ";
			params.put("type", saleOrder.getType());
		}
		
		if (StringUtils.hasText(saleOrder.getStatus())) {
			sql += " and o.status = :status ";
			params.put("status", saleOrder.getStatus());
		}
		
		if (StringUtils.hasText(saleOrder.getAccount())) {
			sql += " and o.account like :account ";
			params.put("account", "%" + saleOrder.getAccount() + "%");
		}
		
		if (StringUtils.hasText(saleOrder.getCustomerMobilePhone())) {
			sql += " and c.mobile_phone like :mobilePhone ";
			params.put("mobilePhone", "%" + saleOrder.getCustomerMobilePhone() + "%");
		}
		
		if (StringUtils.hasText(saleOrder.getStartTime()) && StringUtils.hasText(saleOrder.getEndTime())) {
			sql += " and o.create_time >= :startTime and o.create_time <= :endTime ";
			params.put("startTime", saleOrder.getStartTime());
			params.put("endTime", saleOrder.getEndTime());
		} else if (StringUtils.hasText(saleOrder.getStartTime())) {
			sql += " and o.create_time >= :startTime  ";
			params.put("startTime", saleOrder.getStartTime());
		} else if (StringUtils.hasText(saleOrder.getEndTime())) {
			sql += " and o.create_time <= :endTime ";
			params.put("endTime", saleOrder.getEndTime());
		}
		return this.listByNativeSqlPage2(sql, params, page, rows);
	}

	public List<SaleOrderStatisticsVo> getStatisticsInfo(SaleOrder saleOrder) {
		String sql = //
				"select o.type, " +
				"       sum(o.gross_profit) grossProfit, " +
				"       sum(case when o.`status` = '未付款' then o.gross_profit else 0 end) nonPayProfit, " +
				"       sum(case when o.`status` <> '未付款' then o.gross_profit else 0 end) paidProfit, " +
				"       sum(o.customer_pay) customerPay " +
				"from sale_order o " +
				"where o.is_valid = 1 ";
		Map<String, Object> params = new HashMap<>();
		if (saleOrder.getCustomerId() != null) {
			sql += " and o.customer_id = :customerId ";
			params.put("customerId", saleOrder.getCustomerId());
		}
		
		if (saleOrder.getProductId() != null) {
			sql += " and o.product_id = :productId ";
			params.put("productId", saleOrder.getProductId());
		}
		
		if (saleOrder.getCreateUserId() != null) {
			sql += " and o.create_user_id = :createUserId ";
			params.put("createUserId", saleOrder.getCreateUserId());
		}
		
		if (StringUtils.hasText(saleOrder.getAccount())) {
			sql += " and o.account = :account ";
			params.put("account", saleOrder.getAccount());
		}
		
		if (StringUtils.hasText(saleOrder.getStartTime()) && StringUtils.hasText(saleOrder.getEndTime())) {
			sql += " and o.create_time >= :startTime and o.create_time <= :endTime ";
			params.put("startTime", saleOrder.getStartTime());
			params.put("endTime", saleOrder.getEndTime());
		} else if (StringUtils.hasText(saleOrder.getStartTime())) {
			sql += " and o.create_time >= :startTime  ";
			params.put("startTime", saleOrder.getStartTime());
		} else if (StringUtils.hasText(saleOrder.getEndTime())) {
			sql += " and o.create_time <= :endTime ";
			params.put("endTime", saleOrder.getEndTime());
		}
		
		sql += " group by o.type ";
		
		List<Map<String, Object>> rows = this.listByNativeSql(sql, params);
		return BeanUtil.mapToBean(rows, SaleOrderStatisticsVo.class);
	}

}
