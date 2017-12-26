package com.example.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.example.dao.BaseDao;
import com.example.vo.Filter;
import com.example.vo.Filter.Operator;
import com.example.vo.Order;
import com.example.vo.Order.Direction;
import com.example.vo.PageInfo;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {
	/** 实体类类型 */
	private Class<T> entityClass;

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	@Resource(name = "dataSource")
	protected DataSource datasource;

	public BaseDaoImpl() {
		Type type = getClass().getGenericSuperclass();
		Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
		entityClass = (Class<T>) parameterizedType[0];
	}

	@Override
	public Serializable save(T entity) {
		Assert.notNull(entity,"entity can not be null");
		return hibernateTemplate.save(entity);
	}

	@Override
	public T get(ID id) {
		if (id != null) {
			return hibernateTemplate.get(entityClass, id);
		}
		return null;
	}

	@Override
	public void delete(T t) {
		if (t != null) {
			hibernateTemplate.delete(t);
		}

	}

	@Override
	public void update(T t) {
		if (t != null) {
			hibernateTemplate.update(t);
		}

	}

	@Override
	public void persist(T t) {
		Assert.notNull(t,"entity can not be null");;
		hibernateTemplate.persist(t);
	}

	@Override
	public T merge(T t) {
		Assert.notNull(t,"entity can not be null");;
		return hibernateTemplate.merge(t);
	}

	@Override
	public void clear() {
		hibernateTemplate.clear();
	}

	@Override
	public void flush() {
		hibernateTemplate.flush();
	}

	@Override
	public void evict(T t) {
		hibernateTemplate.evict(t);
	}

	@Override
	public T load(ID id) {
		if (id != null) {
			try {
				return hibernateTemplate.load(entityClass, id);
			} catch (DataAccessException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public void lock(T t, LockMode lockMode) {
		if (t != null || lockMode != null) {
			hibernateTemplate.lock(t, lockMode);
		}
	}

	@Override
	public void bulkUpdate(String hql, Object... values) {
		hibernateTemplate.bulkUpdate(hql, values);
	}

	@Override
	public List<T> findByExample(T exampleEntity, Integer firstResult, Integer maxResults) {
		if (exampleEntity == null) {
			return Collections.emptyList();
		}
		if (firstResult == null || maxResults == null) {
			return (List<T>) hibernateTemplate.findByExample(exampleEntity);
		} else {
			return (List<T>) hibernateTemplate.findByExample(exampleEntity, firstResult, maxResults);
		}
	}

	@Override
	public List<T> findList(Integer firstResult, Integer maxResults, List<Filter> filters, Order... orders) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		addFilters(criteria, filters);
		addOrders(criteria, orders);
		if (firstResult == null || maxResults == null) {
			return (List<T>) hibernateTemplate.findByCriteria(criteria);
		}
		return (List<T>) hibernateTemplate.findByCriteria(criteria, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String hql, Object... values) {
		// 是否开启二级缓存
		// hibernateTemplate.setCacheQueries(true);
		return (List<T>) hibernateTemplate.find(hql, values);
	}

	@Override
	public List<T> findByNamedParam(String hql, String[] paramNames, Object[] values) {
		return (List<T>) hibernateTemplate.findByNamedParam(hql, paramNames, values);
	}

	@Override
	public List<T> findList(Filter... filters) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		if (filters != null) {
			addFilters(criteria, Arrays.asList(filters));
		}
		return (List<T>) hibernateTemplate.findByCriteria(criteria);
	}

	@Override
	public Long count(Filter... filters) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		criteria.setProjection(Projections.rowCount());
		if (filters != null) {
			addFilters(criteria, Arrays.asList(filters));
		}
		return ((List<Long>) hibernateTemplate.findByCriteria(criteria)).get(0);
	}

	@Override
	public Long count(List<Filter> filters) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		criteria.setProjection(Projections.rowCount());
		if (filters != null) {
			addFilters(criteria, filters);
		}
		return ((List<Long>) hibernateTemplate.findByCriteria(criteria)).get(0);
	}

	@Override
	public Iterator<T> iterate(String hql, Object... values) {
		return (Iterator<T>) hibernateTemplate.iterate(hql, values);
	}

	/**
	 * 排序
	 * 
	 * @param criteria
	 * @param orders
	 */
	private void addOrders(DetachedCriteria criteria, Order... orders) {
		if (orders == null || orders.length == 0) {
			return;
		}
		for (Order order : orders) {
			if (order != null) {
				if (order.getDirection() == Direction.asc) {
					criteria.addOrder(org.hibernate.criterion.Order.asc(order.getProperty()));
				} else if (order.getDirection() == Direction.desc) {
					criteria.addOrder(org.hibernate.criterion.Order.desc(order.getProperty()));
				}
			}
		}
	}

	/**
	 * 条件过滤
	 * 
	 * @param criteria
	 * @param filters
	 */
	private void addFilters(DetachedCriteria criteria, List<Filter> filters) {
		// mysql默认对大小写不敏感，filter的ignorecase设置无效..
		if (filters == null || filters.isEmpty())
			return;
		for (Filter filter : filters) {
			if (filter == null || StringUtils.isEmpty(filter.getProperty())) {
				continue;
			}
			if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
				if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
					criteria.add(Restrictions.eq(filter.getProperty(), filter.getValue()).ignoreCase());
				} else {
					criteria.add(Restrictions.eq(filter.getProperty(), filter.getValue()));
				}
			} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
				if (filter.getIgnoreCase() != null && filter.getIgnoreCase() && filter.getValue() instanceof String) {
					criteria.add(Restrictions.ne(filter.getProperty(), filter.getValue()).ignoreCase());
				} else {
					criteria.add(Restrictions.ne(filter.getProperty(), filter.getValue()));
				}
			} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
				criteria.add(Restrictions.gt(filter.getProperty(), filter.getValue()));
			} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
				criteria.add(Restrictions.lt(filter.getProperty(), filter.getValue()));
			} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
				criteria.add(Restrictions.ge(filter.getProperty(), filter.getValue()));
			} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
				criteria.add(Restrictions.le(filter.getProperty(), filter.getValue()));
			} else if (filter.getOperator() == Operator.like && filter.getValue() != null
					&& filter.getValue() instanceof String) {
				// like语句中的"%","_"等占位符在参数中自行配置
				if (filter.getIgnoreCase() != null && filter.getIgnoreCase()) {
					criteria.add(Restrictions.like(filter.getProperty(), filter.getValue()).ignoreCase());
				} else {
					criteria.add(Restrictions.like(filter.getProperty(), filter.getValue()));
				}
			} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
				Object obj = filter.getValue();
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同 (in 与not in只支持 Collection 与
				// Object[])
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同 (in 与not in只支持 Collection 与
				// Object[])
				if (obj instanceof Collection<?> && !((Collection<?>) obj).isEmpty()) {
					criteria.add(Restrictions.in(filter.getProperty(), (Collection<?>) obj));
				} else if (obj instanceof Object[] && ((Object[]) obj).length > 0) {
					criteria.add(Restrictions.in(filter.getProperty(), (Object[]) obj));
				}
			} else if (filter.getOperator() == Operator.isNull) {
				/* 测试 */
				/* 测试 */

				criteria.add(Restrictions.isNull(filter.getProperty()));
			} else if (filter.getOperator() == Operator.isNotNull) {
				criteria.add(Restrictions.isNotNull(filter.getProperty()));
			}
		}

	}

	public T unique(String hql, Object... values) {
		List<T> l = findList(hql, values);
		return CollectionUtils.isEmpty(l) ? null : l.get(0);
	}

	@Override
	public List<T> findByIds(List<ID> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		String hql = "from " + entityClass.getSimpleName() + " where id in (:ids)";
		return (List<T>) hibernateTemplate.findByNamedParam(hql, "ids", ids);
	}

	/**
	 * 通过JPA或者参数命名的占位符查询
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public Query createQueryByMap(String hql, Map<String, Object> param) {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (String property : param.keySet()) {
				Object obj = param.get(property);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					query.setParameterList(property, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					query.setParameterList(property, (Object[]) obj);
				} else {
					query.setParameter(property, obj);
				}
			}
		}
		return query;
	}

	@Override
	public PageInfo findPage(PageInfo pageInfo, List<Filter> filters, Order... orders) {
		return new PageInfo(count(filters), pageInfo.getStartPage(), pageInfo.getLength(),
				findList(pageInfo.getFirst(), pageInfo.getLength(), filters, orders));
	}

	public SQLQuery createSQLQuery(String sql) {
		return hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
	}

	@Override
	@Deprecated
	public List<T> findListBySql(String sql) {
		return hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
	}

	@Override
	public Object findSingleBySql(String sql) {
		return hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql).uniqueResult();
	}

	@Override
	public int updateBySql(String sql) {
		return hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
	}

	@Override
	public List<T> findList(List<Filter> filters) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		if (filters != null) {
			addFilters(criteria, filters);
		}
		return (List<T>) hibernateTemplate.findByCriteria(criteria);
	}

	@Override
	public List<T> findList(List<Filter> filters, Integer start, Integer length) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		if (filters != null) {
			addFilters(criteria, filters);
		}
		return (List<T>) hibernateTemplate.findByCriteria(criteria, start, length);
	}

	@Override
	public PageInfo findPage(PageInfo pageInfo, DetachedCriteria criteria) {
		Long count = ((List<Long>) hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount())))
				.get(0);
		criteria.setProjection(null);
		List<T> list = (List<T>) hibernateTemplate.findByCriteria(criteria, pageInfo.getFirst(), pageInfo.getLength());
		return new PageInfo(count, pageInfo.getStartPage(), pageInfo.getLength(), list);

	}

	@Override
	public PageInfo findPageInAggregation(PageInfo pageInfo, DetachedCriteria criteria) {
		Long count = (long) hibernateTemplate.findByCriteria(criteria).size();
		List<T> list = (List<T>) hibernateTemplate.findByCriteria(criteria, pageInfo.getFirst(), pageInfo.getLength());
		return new PageInfo(count, pageInfo.getStartPage(), pageInfo.getLength(), list);
	}

	@Override
	public DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(hibernateTemplate.getSessionFactory());
	}

	@Override
	public int delete(List<ID> ids) {
		if (ids == null || ids.isEmpty()) {
			return -1;
		}
		String hql = "delete from " + entityClass.getName() + " where id in (:ids)";
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	@Override
	public SQLQuery createSQLQuery(String sql, Map<String, Object> param) {
		SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if (param != null && param.size() > 0) {
			for (String property : param.keySet()) {
				Object obj = param.get(property);
				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (obj instanceof Collection<?>) {
					query.setParameterList(property, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					query.setParameterList(property, (Object[]) obj);
				} else {
					query.setParameter(property, obj);
				}
			}
		}
		return query;
	}

	@Override
	public Long count(DetachedCriteria criteria) {
		return ((List<Long>) hibernateTemplate.findByCriteria(criteria.setProjection(Projections.rowCount())))
				.get(0);
	}

	@Override
	public List<?> findList(DetachedCriteria criteria,PageInfo pageInfo) {
		return  hibernateTemplate.findByCriteria(criteria, pageInfo.getFirst(), pageInfo.getLength());
	}
}
