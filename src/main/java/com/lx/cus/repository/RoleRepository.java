package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.Role;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class RoleRepository extends BaseEntityRepository<Role, Integer> {

	public DataGrid<Role> searchByNameAndRemark(String name, String remark, int page, int rows) {
		String hql = "from " + Role.class.getName() + " where 1 = 1 ";
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

	public Role getByName(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		List<Role> rows = this.listByParams(params );
		return rows != null && !rows.isEmpty() ? rows.get(0) : null;
	}

	public boolean existUser(Integer id) {
		Map<String, Object> params = new HashMap<>();
		params.put("roleId", id);
		int count = this.countByNativeSql("select count(*) from sys_user_role u where u.role_Id = :roleId limit 0, 1 ", params ).intValue();
		return count > 0;
	}

}
