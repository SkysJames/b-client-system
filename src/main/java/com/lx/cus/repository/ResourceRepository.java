package com.lx.cus.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lx.cus.entity.Resource;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.util.BeanUtil;

@Repository
public class ResourceRepository extends BaseEntityRepository<Resource, Integer> {

	public List<Resource> listAllForPropertyGrid() {
		String sql = "SELECT sr.id, sr.`name`, sr.code, t.`name` typeName FROM sys_resource sr, sys_resource_type t WHERE sr.type_id = t.id";
		List<Map<String, Object>> rows = this.listByNativeSql(sql, null);
		return BeanUtil.mapToBean(rows, Resource.class);
	}
	
}
