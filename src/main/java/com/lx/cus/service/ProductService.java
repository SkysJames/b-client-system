package com.lx.cus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Product;
import com.lx.cus.entity.User;
import com.lx.cus.repository.ProductRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public DataGrid<Product> searchByNameAndRemark(String name, String remark, int page, int rows) {
		return productRepository.searchByNameAndRemark(name, remark, page, rows);
	}

	public Response save(Product product) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.PRODUCT_MANAGER);
		String name = product.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "产品名称不能为空", null);
		}
		
		Product dp = productRepository.getByName(name);
		if (dp != null) {
			return new Response(-1, "产品名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		product.setId(null);
		product.setCreateTime(now);
		product.setCreateUserId(user.getId());
		product.setUpdateTime(now);
		product.setUpdateUserId(user.getId());
		
		productRepository.save(product);
		
		return new Response(0, "添加成功", null);
	}

	public Response update(Product product) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.PRODUCT_MANAGER);
		if (product == null || product.getId() == null) {
			return new Response(-1, "编辑的产品不能为空", null);
		}
		
		Product old = productRepository.get(product.getId());
		if (old == null) {
			return new Response(-1, "编辑的产品不存在", null);
		}
		
		String name = product.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "产品名称不能为空", null);
		}
		
		Product dp = productRepository.getByName(name);
		if (dp != null && dp.getId().intValue() != product.getId().intValue()) {
			return new Response(-1, "产品名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		old.setUpdateTime(new Date());
		old.setCreateUserId(user.getId());
		old.setName(product.getName());
		old.setRemark(product.getRemark());
		productRepository.update(old);
		
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.PRODUCT_MANAGER);
		if (ids == null || ids.length == 0) {
			return new Response(-1, "删除的产品不能为空", null);
		}
		for (Integer id : ids) {
			if (this.productRepository.existAppoinment(id) || this.productRepository.existSaleOrder(id)) {
				return new Response(-1, "产品已被使用，不可删除", null);
			}
		}
	    productRepository.batchDelete(ids);
		return new Response(0, "删除成功", null);
	}

	public Product get(Integer id) {
		return productRepository.get(id);
	}

	public List<Product> listAll() {
		return productRepository.listAll();
	}

}
