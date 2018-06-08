package com.lx.cus.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.Role;
import com.lx.cus.entity.RoleResource;
import com.lx.cus.entity.User;
import com.lx.cus.repository.RoleRepository;
import com.lx.cus.repository.RoleResourceRepository;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleResourceRepository roleResourceRepository;
	
	public DataGrid<Role> searchByNameAndRemark(String name, String remark, int page, int rows) {
		return roleRepository.searchByNameAndRemark(name, remark, page, rows);
	}

	public Response save(Role role) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_ROLE);
		String name = role.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "角色名称不能为空", null);
		}
		
		Role dp = roleRepository.getByName(name);
		if (dp != null) {
			return new Response(-1, "角色名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		Date now = new Date();
		role.setId(null);
		role.setCreateTime(now);
		role.setCreateUserId(user.getId());
		role.setUpdateTime(now);
		role.setUpdateUserId(user.getId());
		
		roleRepository.save(role);
		
		return new Response(0, "添加成功", null);
	}

	public Response update(Role role) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_ROLE);
		if (role == null || role.getId() == null) {
			return new Response(-1, "编辑的角色不能为空", null);
		}
		
		Role old = roleRepository.get(role.getId());
		if (old == null) {
			return new Response(-1, "编辑的角色不存在", null);
		}
		
		String name = role.getName();
		if (!StringUtils.hasText(name)) {
			return new Response(-1, "角色名称不能为空", null);
		}
		
		Role dp = roleRepository.getByName(name);
		if (dp != null && dp.getId().intValue() != role.getId().intValue()) {
			return new Response(-1, "角色名称已存在", null);
		}
		User user = SecurityUtils.getCurrentUser();
		old.setUpdateTime(new Date());
		old.setCreateUserId(user.getId());
		old.setName(role.getName());
		old.setRemark(role.getRemark());
		roleRepository.update(old);
		
		return new Response(0, "修改成功", null);
	}

	public Response delete(Integer[] ids) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_ROLE);
		if (ids == null || ids.length == 0) {
			return new Response(-1, "删除的角色不能为空", null);
		}
		for (Integer id : ids) {
			if (this.roleRepository.existUser(id)) {
				return new Response(-1, "角色绑定用户，不可删除", null);
			}
		}
	    roleRepository.batchDelete(ids);
		return new Response(0, "删除成功", null);
	}

	public Role get(Integer id) {
		return roleRepository.get(id);
	}

	public Response authorize(Integer[] ids, Integer id) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_ROLE);
		roleResourceRepository.removeByRoleId(id);
		if (ids != null && ids.length != 0) {
			Set<Integer> allResIds = new HashSet<>(Arrays.asList(ids));
			List<RoleResource> roleResources = new ArrayList<>(allResIds.size());
			allResIds.forEach(e -> {
				RoleResource rs = new RoleResource();
				rs.setResourceId(e);
				rs.setRoleId(id);
				roleResources.add(rs);
			});
			this.roleResourceRepository.batchSave(roleResources);
		}
		return new Response(0, "授权成功", null);
	}

	public List<Integer> listResoureIdByRoleId(Integer id) {
		List<RoleResource> roleResources = roleResourceRepository.listByRoleId(id);
		if (roleResources != null && !roleResources.isEmpty()) {
			List<Integer> ids = new ArrayList<>(roleResources.size());
			roleResources.forEach(e -> ids.add(e.getResourceId()));
			return ids;
		}
		return new ArrayList<Integer>(0);
	}

	public DataGrid<Role> listAllGrid() {
		List<Role> rows = this.roleRepository.listAll();
		DataGrid<Role> dataGrid = new DataGrid<>();
		dataGrid.setTotal(rows.size());
		dataGrid.setRows(rows);
		return dataGrid;
	}

}
