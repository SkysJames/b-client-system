package com.lx.cus.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.User;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class UserRepository extends BaseEntityRepository<User, Integer> {

	public User getByEmpNo(String empNo) {
		String sql = "from " + getEntityClass().getName() + " t where t.empNo = :empNo";
		Map<String, Object> params = new HashMap<>();
		params.put("empNo", empNo);
		List<User> rows = this.listByParams(sql , params);
		return rows != null && !rows.isEmpty() ? rows.get(0) : null;
	}

	public void updateLastLoginTime(Integer userId, Date lastLoginTime) {
		String sql = "UPDATE sys_user SET last_login_time = :lastLoginTime WHERE id = :userId";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("lastLoginTime", lastLoginTime);
		this.executeByNativeSql(sql, params);
	}

	public DataGrid<User> search(User user, int page, int rows) {
		Map<String, Object> params = new HashMap<>();
		String sql = "SELECT u.id, u.name, u.emp_no empNo, u.email, u.status, u.tele_phone telePhone, " +
				     "u.mobile_phone mobilePhone, p.`name` departmentName, DATE_FORMAT(u.create_time, '%Y-%m-%d %T') lastLoginTimeStr " +
                     "FROM sys_user u, sys_department p " +
                     "WHERE u.department_id = p.id ";
		if (StringUtils.hasText(user.getName())) {
			sql += " AND u.`name` LIKE :userName ";
			params.put("userName", "%" + user.getName() + "%");
		}
		
		if (StringUtils.hasText(user.getEmpNo())) {
			sql += " AND u.emp_no LIKE :empNo ";
			params.put("empNo", "%" + user.getEmpNo() + "%");
		}
		
		if (StringUtils.hasText(user.getStatus())) {
			String[] strs = user.getStatus().split(",");
			sql += " AND ( u.`status` = :status0 ";
			params.put("status0", strs[0]);
			for (int i = 1; i < strs.length; ++i) {
				sql += " OR u.`status` = :status" + i + " ";
				params.put("status" + i, strs[i]);
			}
			sql += ")";
		}
		
		if (StringUtils.hasText(user.getDepartmentIds())) {
			String[] strs = user.getDepartmentIds().split(",");
			sql += " AND ( u.`department_id` = :departmentId0 ";
			params.put("departmentId0", Integer.valueOf(strs[0]));
			for (int i = 1; i < strs.length; ++i) {
				sql += " OR u.`department_id` = :departmentId" + i + " ";
				params.put("departmentId" + i, Integer.valueOf(strs[i]));
			}
			sql += ")";
		}
		
		return this.listByNativeSqlPage2(sql, params, page, rows);
	}

	public Set<String> listPermissions(Integer userId) {
		User user = get(userId);
		String sql;
		Map<String, Object> params = new HashMap<>();
		//如果是超级管理员，默认给所有权限
		if ("admin".equals(user.getEmpNo())) {
			sql = "select s.code from sys_resource s";
		} else {
			sql = "SELECT s.`code` FROM sys_user_role ur, sys_role_res r, sys_resource s " +
				     "WHERE ur.user_id = :userId AND ur.role_id = r.role_id AND r.resource_id = s.id";
		    params.put("userId", userId);
		}
		List<Map<String, Object>> rows = this.listByNativeSql(sql, params);
		Set<String> set = new HashSet<>();
		if (rows != null && !rows.isEmpty()) {
			rows.forEach(e -> set.add((String)e.get("code")));
		}
		return set;
	}

}
