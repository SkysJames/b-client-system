package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lx.cus.entity.UserRole;
import com.lx.cus.repository.common.BaseEntityRepository;

@Repository
public class UserRoleRepository extends BaseEntityRepository<UserRole, Integer> {

	public int removeByUserId(Integer userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return this.executeByNativeSql("delete from sys_user_role  where user_id = :userId", params);
	}

	public List<UserRole> listByUserId(Integer userId) {
		String hql = "from " + getEntityClass().getName() + " t where t.userId = :userId";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return this.listByParams(hql, params );
	}

}
