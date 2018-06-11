package com.lx.cus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lx.cus.common.ApplicationConsts;
import com.lx.cus.entity.User;
import com.lx.cus.entity.UserRole;
import com.lx.cus.repository.DepartmentRepository;
import com.lx.cus.repository.UserRepository;
import com.lx.cus.repository.UserRoleRepository;
import com.lx.cus.util.BeanUtil;
import com.lx.cus.util.SecurityUtils;
import com.lx.cus.vo.DataGrid;
import com.lx.cus.vo.Response;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	private static final String DEFAULT_PASSWORD = "123456";
	
	private static final String SYSTEM_ADMIN = "admin";

	public DataGrid<User> search(User user, int page, int rows) {
		return userRepository.search(user, page, rows);
	}

	public Response save(User user) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_USER);
		if (this.userRepository.getByEmpNo(user.getEmpNo()) != null) {
			return new Response(-1, "员工编号已存在", null);
		}
		user.setId(null);
		user.setPassword(DEFAULT_PASSWORD);
		user.setCreateTime(new Date());
		user.setCreateUserId(SecurityUtils.getCurrentUser().getId());
		user.setUpdateTime(user.getCreateTime());
		user.setUpdateUserId(SecurityUtils.getCurrentUser().getId());
		this.userRepository.save(user);
		return new Response(0, "添加成功", null);
	}

	public Response update(User user) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_USER);
		User old = user.getId() != null ? this.userRepository.get(user.getId()) : null;
		if (old == null) {
			return new Response(-1, "修改员工不存在", null);
		}
		
		if (SYSTEM_ADMIN.equals(old.getEmpNo())) {
			return new Response(-1, "超级管理员不可更改", null);
		}
		
		if (!old.getEmpNo().equals(user.getEmpNo())) {
			return new Response(-1, "员工编号不可更改", null);
		}
		
		BeanUtil.copyPropertiesIgnoreNull(old, user);
		
		old.setUpdateUserId(SecurityUtils.getCurrentUser().getId());
		old.setUpdateTime(new Date());
		this.userRepository.update(old);
		return new Response(0, "还没开发", null);
	}

	public User get(Integer id) {
		return userRepository.get(id);
	}

	public Response login(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new Response(-1, "用户名密码不能为空", null);
		}
		User user = this.userRepository.getByEmpNo(username);
		if (user == null || !user.getPassword().equals(password)) {
			return new Response(-1, "用户名或密码错误", null);
		}
		
		if (!"在职".equals(user.getStatus())) {
			return new Response(-1, "该用户为" + user.getStatus() + "状态，不能使用系统", null);
		}
		
		user.setPermissions(this.userRepository.listPermissions(user.getId()));
		user.setDepartmentName(this.departmentRepository.get(user.getDepartmentId()).getName());
		if (user.getLastLoginTime() != null) {
			user.setLastLoginTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getLastLoginTime()));
		} else {
			user.setLastLoginTimeStr("-");
		}
		SecurityUtils.setCurrentUser(user);
		user.setLastLoginTime(new Date());
		this.userRepository.updateLastLoginTime(user.getId(), user.getLastLoginTime());
		return new Response(0, "登录成功", null);
	}

	public User getCurrentUser() {
		return SecurityUtils.getCurrentUser();
	}

	public Response authorize(Integer[] roleIds, Integer userId) {
		SecurityUtils.checkPermission(ApplicationConsts.Auth.SYS_USER);
		userRoleRepository.removeByUserId(userId);
		if (roleIds != null && roleIds.length != 0) {
			Set<Integer> allRoleIds = new HashSet<>(Arrays.asList(roleIds));
			List<UserRole> userRoles = new ArrayList<>(allRoleIds.size());
			allRoleIds.forEach(e -> {
				UserRole ur = new UserRole();
				ur.setUserId(userId);
				ur.setRoleId(e);
				userRoles.add(ur);
			});
			this.userRoleRepository.batchSave(userRoles);
		}
		return new Response(0, "角色绑定成功", null);
	}

	public List<Integer> listRoleIdsByUserId(Integer userId) {
		List<UserRole> userRoles = userRoleRepository.listByUserId(userId);
		if (userRoles != null && !userRoles.isEmpty()) {
			List<Integer> ids = new ArrayList<>(userRoles.size());
			userRoles.forEach(e -> ids.add(e.getRoleId()));
			return ids;
		}
		return new ArrayList<Integer>(0);
	}

	public List<User> listAll() {
		return this.userRepository.listAll();
	}

	public Response changePassword(User user) {
		User currentUser = SecurityUtils.getCurrentUser();
		User old = this.userRepository.get(currentUser.getId());
		if (!old.getPassword().equals(user.getPassword())) {
			return new Response(-1, "密码错误", null);
		}
		old.setPassword(user.getNewPassword());
		old.setUpdateTime(new Date());
		old.setUpdateUserId(old.getId());
		
		this.userRepository.update(old);
		return new Response(0, "密码修改成功", null);
	}

	public List<Map<String, Object>> getStatusCount() {
		return this.userRepository.getStatusCount();
	}
	
}
