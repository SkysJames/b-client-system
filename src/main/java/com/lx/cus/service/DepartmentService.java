package com.lx.cus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Department;
import com.lx.cus.entity.User;
import com.lx.cus.repository.DepartmentRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class DepartmentService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	public DataGrid<Department> searchByNameAndRemark(String name, String remark, int page, int rows) {
		return departmentRepository.searchByNameAndRemark(name, remark, page, rows);
	}

	public Response save(Department department) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_DEPARTMENT);
		String name = department.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "部门名称不能为空", null);
		}
		
		Department dp = departmentRepository.getByName(name);
		if (dp != null) {
			return new Response(-1, "部门名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		department.setId(null);
		department.setCreateTime(now);
		department.setCreateUserId(user.getId());
		department.setUpdateTime(now);
		department.setUpdateUserId(user.getId());
		
		departmentRepository.save(department);
		
		return new Response(0, "添加成功", null);
	}

	public Response update(Department department) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_DEPARTMENT);
		if (department == null || department.getId() == null) {
			return new Response(-1, "编辑的部门不能为空", null);
		}
		
		Department old = departmentRepository.get(department.getId());
		if (old == null) {
			return new Response(-1, "编辑的部门不存在", null);
		}
		
		String name = department.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "部门名称不能为空", null);
		}
		
		Department dp = departmentRepository.getByName(name);
		if (dp != null && dp.getId().intValue() != department.getId().intValue()) {
			return new Response(-1, "部门名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		old.setUpdateTime(new Date());
		old.setCreateUserId(user.getId());
		old.setName(department.getName());
		old.setRemark(department.getRemark());
		departmentRepository.update(old);
		
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_DEPARTMENT);
		if (ids == null || ids.length == 0) {
			return new Response(-1, "删除的部门不能为空", null);
		}
		for (Integer id : ids) {
			if (departmentRepository.existUser(id)) {
				return new Response(-1, "部门有绑定员工，不可以删除", null);
			}
		}
	    departmentRepository.batchDelete(ids);
		return new Response(0, "删除成功", null);
	}

	public Department get(Integer id) {
		return departmentRepository.get(id);
	}

	public List<Department> listAll() {
		return this.departmentRepository.listAll();
	}

}
