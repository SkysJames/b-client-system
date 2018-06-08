package com.lx.cus.repository.common;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.lx.cus.vo.DataGrid;


public abstract class NativeRepository {

	@PersistenceContext
	private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * 根据原生sql获取数据
	 * 
	 * @param sql
	 *            原生sql
	 * @param params
	 *            sql中的参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> listByNativeSql(String sql, Map<String, Object> params) {
		SQLQuery query = getEntityManager().createNativeQuery(sql).unwrap(SQLQuery.class);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 执行统计的原生sql
	 * 
	 * @param countSql
	 * @param params
	 * @return
	 */
	protected Number countByNativeSql(String countSql, Map<String, Object> params) {
		SQLQuery sqlQuery = getEntityManager().createNativeQuery(countSql).unwrap(SQLQuery.class);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				sqlQuery.setParameter(p.getKey(), p.getValue());
			}
		}
		return (Number) sqlQuery.uniqueResult();
	}

	/**
	 * 原生sql分页查询
	 * 
	 * @param querySql
	 * @param queryParams
	 * @param page
	 * @param pageSize
	 * @return
	 */
	protected DataGrid<Map<String, Object>> listByNativeSqlPage(String querySql, Map<String, Object> queryParams, int page, int pageSize) {
		return listByNativeSqlPage(querySql, queryParams, null, null, page, pageSize);
	}

	/**
	 * 原生sql分页查询
	 * 
	 * @param querySql
	 *            查询sql
	 * @param queryParams
	 *            查询sql中的参数
	 * @param countSql
	 *            统计sql
	 * @param countParams
	 *            统计sql中的参数
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected DataGrid<Map<String, Object>> listByNativeSqlPage(String querySql, Map<String, Object> queryParams, //
			String countSql, Map<String, Object> countParams, int page, int pageSize) {
		
		DataGrid<Map<String, Object>> dataGrid = new DataGrid<>();
		
		if (countSql == null || "".equals(countSql)) {
			countSql = "select count(1) from (" + querySql + ")r";
			countParams = queryParams;
		}
		
		int count = countByNativeSql(countSql, countParams).intValue();
		dataGrid.setTotal(count);
		if (count == 0) {
			return dataGrid;
		}

		Query query = getEntityManager().createNativeQuery(querySql);
		if (queryParams != null && queryParams.size() > 0) {
			for (Map.Entry<String, Object> p : queryParams.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Map<String, Object>> values = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		dataGrid.setRows(values);
		return dataGrid;
	}

	/**
	 * 执行原生sql
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	protected int executeByNativeSql(String sql, Map<String, Object> params) {
		Query query = getEntityManager().createNativeQuery(sql);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		return query.executeUpdate();
	}

	/**
	 * 只支持只有输入参数的存储过程
	 * 
	 * @param procedure
	 *            存储过程 {call proc_name(?,?...)}
	 * @param params
	 *            输入参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> listByProcedure(String procedure, Object[] params) {
		Query query = getEntityManager().createNativeQuery(procedure);
		if (params != null && params.length > 0) {
			for (int i = 1; i <= params.length; ++i) {
				query.setParameter(i, params[i - 1]);
			}
		}
		return query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	
}
