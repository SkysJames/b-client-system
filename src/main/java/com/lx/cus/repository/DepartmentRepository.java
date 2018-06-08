package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.Department;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class DepartmentRepository extends BaseEntityRepository<Department, Integer> {

	public DataGrid<Department> searchByNameAndRemark(String name, String remark, int page, int rows) {
		String hql = "from " + Department.class.getName() + " where 1 = 1 ";
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.hasText(name) || StringUtils.hasText(remark)) {
			hql += " and (";
			if (StringUtils.hasText(name)) {
				hql += " name like :name ";
				params.put("name", "%" + name + "%");
			}
			if (StringUtils.hasText(remark)) {
				hql += " or remark like :remark ";
				params.put("remark", "%" + remark + "%");
			}
			hql += ")";
		}
		return this.listByPage(hql, params, null, null, page, rows);
	}

	public Department getByName(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		List<Department> rows = this.listByParams(params );
		return rows != null && !rows.isEmpty() ? rows.get(0) : null;
	}

	public boolean existUser(Integer id) {
		Map<String, Object> params = new HashMap<>();
		params.put("departmentId", id);
		int count = this.countByNativeSql("select count(*) from sys_user u where u.department_id = :departmentId limit 0, 1", params ).intValue();
		return count > 0;
	}

}
