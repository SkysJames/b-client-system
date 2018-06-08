package com.lx.cus.repository.common;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;

import com.lx.cus.entity.BaseEntity;
import com.lx.cus.util.BeanUtil;
import com.lx.cus.vo.DataGrid;

public abstract class BaseEntityRepository<E extends BaseEntity<ID>, ID extends Serializable> extends NativeRepository {

	// 实体类的类型
	@SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
		return (Class<E>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	// 添加实体
	public E save(E e) {
		getEntityManager().persist(e);
		return e;
	}

	// 获取所有实体类列表数据
	public List<E> listAll() {
		String jpql = "from " + getEntityClass().getName();
		return getEntityManager().createQuery(jpql, getEntityClass()).getResultList();
	}

	// 更新实体
	public E update(E e) {
		getEntityManager().merge(e);
		return e;
	}

	// 根据ID删除实体
	public void delete(ID id) {
		getEntityManager().remove(get(id));
	}

	// 根据ID获取实体
	public E get(ID id) {
		E e = getEntityManager().find(getEntityClass(), id);
		return e;
	}

	/**
	 * 根据查询条件返回实体列表
	 * 
	 * @param params
	 *            查询条件，(key, value) -> (实体字段名, 字段的值)
	 * @return
	 */
	protected List<E> listByParams(Map<String, Object> params) {
		StringBuilder builder = new StringBuilder("select t from " + getEntityClass().getName() + " t where 1 = 1 ");
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				builder.append(" and t." + key + " = :" + key);
			}
		}
		String jpql = builder.toString();
		TypedQuery<E> query = getEntityManager().createQuery(jpql, getEntityClass());
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		return query.getResultList();
	}

	/**
	 * 分页获取实体数据列表
	 * 
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            页面大小
	 * @return
	 */
	public DataGrid<E> listByPage(int page, int pageSize) {
		DataGrid<E> dataGrid = new DataGrid<>();
		dataGrid.setTotal(getEntityCount().intValue());
		String jpql = "from " + getEntityClass().getName();
		TypedQuery<E> query = getEntityManager().createQuery(jpql, getEntityClass());
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<E> values = query.getResultList();
		dataGrid.setRows(values);
		return dataGrid;
	}

	// 获取当前实体的总数
	public Number getEntityCount() {
		Table table = getEntityClass().getAnnotation(Table.class);
		Number number = null;
		if (table != null) {
			String tableName = table.name();
			String countSql = "select count(1) from " + tableName;
			number = (Number) getEntityManager().createNativeQuery(countSql).unwrap(SQLQuery.class).uniqueResult();
		} else {
			number = new Long(0);
		}
		return number;
	}

	/**
	 * 分页获取实体数据列表
	 * 
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            页面大小
	 * @return
	 */
	protected DataGrid<E> listByPage(int page, int pageSize, Map<String, Object> params) {
		DataGrid<E> dataGrid = new DataGrid<>();

		StringBuilder queryBuilder = new StringBuilder("select t from " + getEntityClass().getName() + " t where 1 = 1 ");
		StringBuilder countBuilder = new StringBuilder("select count(t) from " + getEntityClass().getName() + " t where 1 = 1 ");

		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				queryBuilder.append(" and t." + key + " = :" + key);
				countBuilder.append(" and t." + key + " = :" + key);
			}
		}

		TypedQuery<E> query = getEntityManager().createQuery(queryBuilder.toString(), getEntityClass());
		TypedQuery<Long> count = getEntityManager().createQuery(countBuilder.toString(), Long.class);

		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
				count.setParameter(p.getKey(), p.getValue());
			}
		}

		dataGrid.setTotal(count.getSingleResult().intValue());
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<E> values = query.getResultList();
		dataGrid.setRows(values);
		return dataGrid;
	}

	/**
	 * 分页获取实体数据列表
	 * 
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            页面大小
	 * @return
	 */
	protected DataGrid<E> listByPage(String querySql, Map<String, Object> queryParams, String countSql, Map<String, Object> countParams, int page, int pageSize) {
		DataGrid<E> dataGrid = new DataGrid<>();

		if (countSql == null || "".equals(countSql.trim())) {
			countSql = "select count(*) " + querySql.substring(querySql.indexOf("from"));
			countParams = queryParams;
		}

		TypedQuery<E> query = getEntityManager().createQuery(querySql, getEntityClass());
		TypedQuery<Long> count = getEntityManager().createQuery(countSql, Long.class);

		if (queryParams != null && queryParams.size() > 0) {
			for (Map.Entry<String, Object> p : queryParams.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}

		if (countParams != null && countParams.size() > 0) {
			for (Map.Entry<String, Object> p : countParams.entrySet()) {
				count.setParameter(p.getKey(), p.getValue());
			}
		}

		dataGrid.setTotal(count.getSingleResult().intValue());
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<E> values = query.getResultList();
		dataGrid.setRows(values);
		return dataGrid;
	}

	protected Long getCountByParams(String sql, Map<String, Object> params) {
		TypedQuery<Long> query = getEntityManager().createQuery(sql, Long.class);
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		return query.getSingleResult();
	}

	public void batchDelete(ID ids[]) {
		if (ids != null && ids.length > 0) {
			EntityManager manager = getEntityManager();
			for (int i = 0, len = ids.length; i < len; ++i) {
				manager.remove(get(ids[i]));
				if (i % 50 == 0 && i != 0) {
					manager.flush();
					manager.clear();
				}
			}
			manager.flush();
			manager.clear();
		}
	}

	public void batchSave(E[] entities) {
		if (entities != null && entities.length > 0) {
			EntityManager manager = getEntityManager();
			for (int i = 0, len = entities.length; i < len; ++i) {
				manager.persist(entities[i]);
				if (i % 50 == 0 && i != 0) {
					manager.flush();
					manager.clear();
				}
			}
			manager.flush();
			manager.clear();
		}
	}
	
	public void batchSave(Collection<E> entities) {
		if (entities != null && entities.size() > 0) {
			EntityManager manager = getEntityManager();
			int i = 0;
			for (E e : entities) {
				manager.persist(e);
				i++;
				if (i % 50 == 0) {
					manager.flush();
					manager.clear();
				}
			}
			manager.flush();
			manager.clear();
		}
	}
	
	/**
	 * 根据查询条件返回实体列表
	 * @param sql
	 * @param params
	 *            查询条件，(key, value) -> (实体字段名, 字段的值)
	 * @return
	 */
	protected List<E> listByParams(String sql, Map<String, Object> params) {
		TypedQuery<E> query = getEntityManager().createQuery(sql, getEntityClass());
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> p : params.entrySet()) {
				query.setParameter(p.getKey(), p.getValue());
			}
		}
		return query.getResultList();
	}
	
	protected DataGrid<E> listByNativeSqlPage2(String sql, Map<String, Object> queryParams, int page, int pageSize) {
		return this.listByNativeSqlPage2(sql, queryParams, null, null, page, pageSize);
	}
	
	protected DataGrid<E> listByNativeSqlPage2(String sql, Map<String, Object> queryParams, //
			String countSql, Map<String, Object> countParams, int page, int pageSize) {
		DataGrid<E> dataGrid = new DataGrid<>();
		DataGrid<Map<String,Object>> dataGridMap = this.listByNativeSqlPage(sql, queryParams, countSql, countParams, page, pageSize);
		dataGrid.setPage(page);
		dataGrid.setTotal(dataGridMap.getTotal());
		if (dataGridMap.getTotal() <= 0) {
			dataGrid.setRows(new ArrayList<>(0));
		} else {
			dataGrid.setRows(BeanUtil.mapToBean(dataGridMap.getRows(), getEntityClass()));
		}
		return dataGrid;
	}

}
