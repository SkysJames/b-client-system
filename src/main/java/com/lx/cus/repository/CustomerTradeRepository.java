package com.lx.cus.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.lx.cus.entity.CustomerTrade;
import com.lx.cus.repository.common.BaseEntityRepository;
import com.lx.cus.vo.DataGrid;

@Repository
public class CustomerTradeRepository extends BaseEntityRepository<CustomerTrade, Integer> {

	public DataGrid<CustomerTrade> searchByNameAndRemark(String name, String remark, int page, int rows) {
		String hql = "from " + getEntityClass().getName() + " where 1 = 1 ";
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

	public CustomerTrade getByName(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		List<CustomerTrade> rows = this.listByParams(params );
		return rows != null && !rows.isEmpty() ? rows.get(0) : null;
	}

	public boolean existCustomer(Integer id) {
		String sql = "select count(*) from customer c where c.trade_id = :tradeId limit 0, 1";
		Map<String, Object> params = new HashMap<>();
		params.put("tradeId", id);
		int count = this.countByNativeSql(sql, params ).intValue();
		return count > 0;
	}

}
