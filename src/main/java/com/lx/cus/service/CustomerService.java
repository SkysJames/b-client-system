package com.lx.cus.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Customer;
import com.lx.cus.entity.User;
import com.lx.cus.repository.CustomerRepository;
import com.lx.cus.repository.UserRepository;
import com.lx.cus.util.BeanUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserRepository userRepository;

	public DataGrid<Customer> search(Customer customer, int page, int rows) {
		return customerRepository.search(customer, page, rows);
	}

	public Response save(Customer customer) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.CUSTOMER_MANAGER);
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		customer.setCreateTime(now);
		customer.setCreateUserId(user.getId());
		customer.setUpdateTime(now);
		customer.setUpdateUserId(user.getId());
		if (customer.getFollowUserEmpNo() == null) {
			customer.setFollowUserId(user.getId());
		} else {
			User followUser = this.userRepository.getByEmpNo(customer.getFollowUserEmpNo());
			customer.setFollowUserId(followUser.getId());
		}
		customer.setId(null);
		if (customer.getShare() == null) {
			customer.setShare(false);
		}
		this.customerRepository.save(customer);
		return new Response(0, "添加成功", null);
	}

	public Response update(Customer customer) {
		User user = SecurityUtils.getCurrentUser();
		Integer custId = customer.getId();
		Customer old = get(custId);
		boolean checked = SecurityUtils.containsPermission(ApplicationConsts.Auth.CUSTOMER_MANAGER) 
				|| old.getFollowUserId().intValue() == user.getId().intValue();
		if (!checked) {
			return new Response(-1, "您没有操作的权限", null);
		}
		if (customer.getFollowUserEmpNo() == null) {
			customer.setFollowUserId(user.getId());
		} else {
			User followUser = this.userRepository.getByEmpNo(customer.getFollowUserEmpNo());
			customer.setFollowUserId(followUser.getId());
		}
		BeanUtil.copyPropertiesIgnoreNull(old, customer);
		this.customerRepository.update(old);
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		User user = SecurityUtils.getCurrentUser();
		for (Integer id : ids) {
			Customer old = get(id);
			boolean checked = SecurityUtils.containsPermission(ApplicationConsts.Auth.CUSTOMER_MANAGER) 
					|| old.getFollowUserId().intValue() == user.getId().intValue();
			if (!checked) {
				return new Response(-1, "您没有操作的权限", null);
			}
			this.customerRepository.delete(id);
		}
		return new Response(0, "删除客户成功", null);
	}

	public Customer get(Integer id) {
		Customer customer = this.customerRepository.get(id);
		customer.setFollowUserEmpNo(this.userRepository.get(customer.getFollowUserId()).getEmpNo());
		return customer;
	}

}
