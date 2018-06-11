package com.lx.cus.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.SaleOrder;
import com.lx.cus.entity.SaleOrderLog;
import com.lx.cus.entity.User;
import com.lx.cus.repository.SaleOrderLogRepository;
import com.lx.cus.repository.SaleOrderRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.util.UUIDUtil;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;
import com.lx.cus.vo.SaleOrderStatisticsVo;

@Service
public class SaleOrderService {
	
	@Autowired
	private SaleOrderRepository saleOrderRepository;
	
	@Autowired
	private SaleOrderLogRepository saleOrderLogRepository;

	public DataGrid<SaleOrder> search(SaleOrder saleOrder, int page, int rows) {
		return saleOrderRepository.search(saleOrder, page, rows);
	}

	public Response save(SaleOrder saleOrder) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SALES_MANAGER);
		User user = SecurityUtils.getCurrentUser();
		
		saleOrder.setId(null);
		saleOrder.setCode(UUIDUtil.uuid32());
		saleOrder.setCreateTime(new Date());
		saleOrder.setCreateUserId(user.getId());
		saleOrder.setStatus(SaleOrder.Status.PAYMENT_NON);
		saleOrder.setUpdateTime(saleOrder.getCreateTime());
		saleOrder.setUpdateUserId(user.getId());
		
//		客户充值的广告币、
//		客户返点、
//		成本返点、
//		客户应付金额（充值广告币/(1+客户返点/100)）、
//		成本价（充值广告币/(1+成本返点/100)）、
//		RMB毛利（客户应付金额-成本价）。
		
		BigDecimal advertisingMoney = saleOrder.getAdvertisingMoney();
		BigDecimal customerRebates = saleOrder.getCustomerRebates();
		BigDecimal costRebates = saleOrder.getCostRebates();
		
		BigDecimal customerPay = advertisingMoney.divide((customerRebates.divide(BigDecimal.valueOf(100L)).add(BigDecimal.valueOf(1L))), BigDecimal.ROUND_HALF_DOWN);
		BigDecimal costPrice = advertisingMoney.divide((costRebates.divide(BigDecimal.valueOf(100L)).add(BigDecimal.valueOf(1L))), BigDecimal.ROUND_HALF_DOWN);
		BigDecimal grossProfit = customerPay.add(costPrice.negate());
		
		saleOrder.setCustomerPay(customerPay);
		saleOrder.setCostPrice(costPrice);
		saleOrder.setGrossProfit(grossProfit);
		
		this.saleOrderRepository.save(saleOrder);
		return new Response(0, "添加成功", saleOrder.getCode());
	}

	public Response updateStatus(SaleOrder saleOrder) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SALES_MANAGER);
		User user = SecurityUtils.getCurrentUser();
		
		Integer id = saleOrder.getId();
		SaleOrder old = this.saleOrderRepository.get(id);
		
		String oldStatus = old.getStatus();
		String newStatus = saleOrder.getStatus();
		
		old.setStatus(newStatus);
		old.setUpdateTime(new Date());
		old.setUpdateUserId(user.getId());
		this.saleOrderRepository.update(old);
		
		SaleOrderLog log = new SaleOrderLog();
		log.setCreateTime(new Date());
		log.setCreateUserId(user.getId());
		log.setNewStatus(newStatus);
		log.setOldStatus(oldStatus);
		log.setCode(old.getCode());
		
		this.saleOrderLogRepository.save(log);
		return new Response(0, "修改成功", null);
	}

	public Response updateStatus(Integer[] ids, String status) {
		for (Integer id : ids) {
			SaleOrder saleOrder = new SaleOrder();
			saleOrder.setId(id);
			saleOrder.setStatus(status);
			this.updateStatus(saleOrder );
		}
		return new Response(0, "修改成功", null);
	}

	public Response getStatisticsInfo(SaleOrder saleOrder) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SALES_STASTIC);
		List<SaleOrderStatisticsVo> vos = this.saleOrderRepository.getStatisticsInfo(saleOrder);
		return new Response(0, "", vos);
	}

	public List<Map<String, Object>> getTypeCount() {
		return this.saleOrderRepository.getTypeCount();
	}

}
