package com.example.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.example.dao.BaseDao;
import com.example.service.BaseService;
import com.example.vo.Filter;
import com.example.vo.Order;
import com.example.vo.PageInfo;

@Transactional(transactionManager="transactionManager")
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	/** baseDao */
	private BaseDao<T, ID> baseDao;

	public void setBaseDao(BaseDao<T, ID> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public ID save(T t) {
		return (ID) baseDao.save(t);
	}

	@Override
	public T get(ID id) {
		return baseDao.get(id);
	}

	@Override
	public T load(ID id) {
		return baseDao.load(id);
	}

	@Override
	public void delete(T t) {
		baseDao.delete(t);

	}

	@Override
	public void update(T t) {
		baseDao.update(t);

	}

	@Override
	public void persist(T t) {
		baseDao.persist(t);
	}

	@Override
	public T merge(T t) {
		return baseDao.merge(t);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters, Order... orders) {
		return baseDao.findList(first, count, filters, orders);
	}

	@Override
	public List<T> findByHql(String hql, Object... values) {
		return baseDao.findList(hql, values);
	}

	@Override
	public Long count(Filter... filters) {
		return baseDao.count(filters);
	}
	@Override
	public List<T> findList(Filter... filters) {
		return baseDao.findList(filters);
	}

	@Override
	public List<T> findByIds(List<ID> ids) {
		return baseDao.findByIds(ids);
	}

	@Override
	public PageInfo findPage(PageInfo pageInfo, List<Filter> filters, Order... orders) {
		return baseDao.findPage(pageInfo, filters, orders);
	}

	@Override
	public Long count(List<Filter> filters) {
		return baseDao.count(filters);
	}

	@Override
	public List<T> findList(List<Filter> filters) {
		return baseDao.findList(filters);
	}

	@Override
	public DataSource getDataSource() {
		return baseDao.getDataSource();
	}
	
	@Override
	public int delete(List<ID> ids) {
		return baseDao.delete(ids);
	}
	
}
