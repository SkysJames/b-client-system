package com.lx.cus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.CustomerLevel;
import com.lx.cus.entity.User;
import com.lx.cus.repository.CustomerLevelRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class CustomerLevelService {
	
	@Autowired
	private CustomerLevelRepository customerLevelRepository;
	
	public DataGrid<CustomerLevel> searchByNameAndRemark(String name, String remark, int page, int rows) {
		return customerLevelRepository.searchByNameAndRemark(name, remark, page, rows);
	}

	public Response save(CustomerLevel customerLevel) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_LEVEL);
		String name = customerLevel.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "客户级别名称不能为空", null);
		}
		
		CustomerLevel dp = customerLevelRepository.getByName(name);
		if (dp != null) {
			return new Response(-1, "客户级别名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		customerLevel.setId(null);
		customerLevel.setCreateTime(now);
		customerLevel.setCreateUserId(user.getId());
		customerLevel.setUpdateTime(now);
		customerLevel.setUpdateUserId(user.getId());
		
		customerLevelRepository.save(customerLevel);
		
		return new Response(0, "添加成功", null);
	}

	public Response update(CustomerLevel customerLevel) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_LEVEL);
		if (customerLevel == null || customerLevel.getId() == null) {
			return new Response(-1, "编辑的客户级别不能为空", null);
		}
		
		CustomerLevel old = customerLevelRepository.get(customerLevel.getId());
		if (old == null) {
			return new Response(-1, "编辑的客户级别不存在", null);
		}
		
		String name = customerLevel.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "客户级别名称不能为空", null);
		}
		
		CustomerLevel dp = customerLevelRepository.getByName(name);
		if (dp != null && dp.getId().intValue() != customerLevel.getId().intValue()) {
			return new Response(-1, "客户级别名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		old.setUpdateTime(new Date());
		old.setCreateUserId(user.getId());
		old.setName(customerLevel.getName());
		old.setRemark(customerLevel.getRemark());
		customerLevelRepository.update(old);
		
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_LEVEL);
		if (ids == null || ids.length == 0) {
			return new Response(-1, "删除的部门不能为空", null);
		}
		for (Integer id : ids) {
			if (this.customerLevelRepository.existCustomer(id)) {
				return new Response(-1, "该级别已被客户使用，不可删除", null);
			}
		}
		customerLevelRepository.batchDelete(ids);
		return new Response(0, "删除成功", null);
	}

	public CustomerLevel get(Integer id) {
		return customerLevelRepository.get(id);
	}

	public List<CustomerLevel> listAll() {
		return this.customerLevelRepository.listAll();
	}

}
