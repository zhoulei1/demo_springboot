package com.example.service;

import java.io.Serializable;
import java.util.List;

import javax.sql.DataSource;

import com.example.vo.Filter;
import com.example.vo.Order;
import com.example.vo.PageInfo;



public interface BaseService <T, ID extends Serializable> {
	/**
	 * 持久化实例对象
	 */
	ID save(T t);
	
	/**通过id查询
	 * @param id
	 * @return
	 */
	T get(ID id);
	
	/**延迟查询对象
	 * @param id
	 * @return
	 */
	T load(ID id);
		
	/**删除
	 * @param t
	 */
	void delete(T t);
	
	/**更新
	 * @param t
	 */
	void update(T t);
	
	/**持久化对象
	 * @param t
	 */
	void persist(T t);
	
	/** 合并对象
	 * @param t
	 * @return 持久化对象
	 */
	T merge(T t);
	
	/** 查询列表
	 * @param first 起始记录 （分页）
	 * @param count 记录数（分页）
	 * @param filters 条件
	 * @param orders 排序
	 * @return
	 */
	List<T> findList(Integer first, Integer count, List<Filter> filters,Order... orders);
	
	/**
	 * @param hql
	 * @param values
	 * @return
	 */
	List<T> findByHql(String hql,Object... values);
	
	/**条件查询总记录数
	 * @param filters 过滤条件
	 * @return 总记录数
	 */
	Long count(Filter...filters);
	
	/**条件查询总记录数
	 * @param filters
	 * @return
	 */
	Long count(List<Filter> filters);
	
	/**条件查询
	 * @param filters
	 * @return
	 */
	List<T> findList(Filter...filters);
	
	/**条件查询
	 * @param filters
	 * @return
	 */
	List<T> findList(List<Filter> filters);
	
	/**通过id列查询
	 * @param ids
	 * @return
	 */
	List<T> findByIds(List<ID> ids);
	
	/**分页查询
	 * @param firstResult 起始记录数
	 * @param maxResults 每页记录数
	 * @param filters 查询条件
	 * @param orders 排序
	 * @return
	 */
	PageInfo findPage(PageInfo pageInfo, List<Filter> filters,Order... orders);
	
	DataSource getDataSource();
	
	
	/**批量删除
	 * @param ids
	 */
	int delete (List<ID> ids);
}
