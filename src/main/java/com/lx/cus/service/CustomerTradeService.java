package com.lx.cus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.CustomerTrade;
import com.lx.cus.entity.User;
import com.lx.cus.repository.CustomerTradeRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class CustomerTradeService {
	
	@Autowired
	private CustomerTradeRepository customerTradeRepository;
	
	public DataGrid<CustomerTrade> searchByNameAndRemark(String name, String remark, int page, int rows) {
		return customerTradeRepository.searchByNameAndRemark(name, remark, page, rows);
	}

	public Response save(CustomerTrade customerTrade) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_TRADE);
		String name = customerTrade.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "客户行业名称不能为空", null);
		}
		
		CustomerTrade dp = customerTradeRepository.getByName(name);
		if (dp != null) {
			return new Response(-1, "客户行业名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		customerTrade.setId(null);
		customerTrade.setCreateTime(now);
		customerTrade.setCreateUserId(user.getId());
		customerTrade.setUpdateTime(now);
		customerTrade.setUpdateUserId(user.getId());
		
		customerTradeRepository.save(customerTrade);
		
		return new Response(0, "添加成功", null);
	}

	public Response update(CustomerTrade customerTrade) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_TRADE);
		if (customerTrade == null || customerTrade.getId() == null) {
			return new Response(-1, "编辑的客户行业不能为空", null);
		}
		
		CustomerTrade old = customerTradeRepository.get(customerTrade.getId());
		if (old == null) {
			return new Response(-1, "编辑的客户行业不存在", null);
		}
		
		String name = customerTrade.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "客户行业名称不能为空", null);
		}
		
		CustomerTrade dp = customerTradeRepository.getByName(name);
		if (dp != null && dp.getId().intValue() != customerTrade.getId().intValue()) {
			return new Response(-1, "客户行业名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		old.setUpdateTime(new Date());
		old.setCreateUserId(user.getId());
		old.setName(customerTrade.getName());
		old.setRemark(customerTrade.getRemark());
		customerTradeRepository.update(old);
		
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_TRADE);
		if (ids == null || ids.length == 0) {
			return new Response(-1, "删除的部门不能为空", null);
		}
		for (int id : ids) {
			if (this.customerTradeRepository.existCustomer(id)) {
				return new Response(-1, "该行业已被客户使用，不可删除", null);
			}
		}
		customerTradeRepository.batchDelete(ids);
		return new Response(0, "删除成功", null);
	}

	public CustomerTrade get(Integer id) {
		return customerTradeRepository.get(id);
	}

	public List<CustomerTrade> listAll() {
		return this.customerTradeRepository.listAll();
	}

}
