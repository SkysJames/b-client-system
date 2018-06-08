package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lx.cus.entity.RoleResource;
import com.lx.cus.repository.common.BaseEntityRepository;

@Repository
public class RoleResourceRepository extends BaseEntityRepository<RoleResource, Integer> {

	public List<RoleResource> listByRoleId(Integer roleId) {
		String hql = "from " + getEntityClass().getName() + " t where t.roleId = :roleId";
		Map<String, Object> params = new HashMap<>();
		params.put("roleId", roleId);
		return this.listByParams(hql, params );
	}

	public int removeByRoleId(Integer roleId) {
		Map<String, Object> params = new HashMap<>();
		params.put("roleId", roleId);
		return this.executeByNativeSql("delete from sys_role_res  where role_id = :roleId", params);
	}

}
