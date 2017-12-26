package com.example.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.example.vo.Filter;
import com.example.vo.Order;
import com.example.vo.PageInfo;


/**
 * HQL参数占位符使用方式
 * <p>
 * <p>
 * 
 * 
 * <p>
 * 
 * 1、问号
 * <p>
 * 
 * <pre>
 * 示例：from User u where u.name = ?
 * hibernate4之后这种方式会出现警告：[DEPRECATION] Encountered positional parameter near line 1, column 95.  Positional parameter are considered deprecated; use named parameters or JPA-style positional parameters instead.
 * </pre>
 * <p>
 * 
 * 2、冒号+参数别名
 * <p>
 * 
 * <pre>
 * 示例：from User u where u.name =:sname
 * </pre>
 * <p>
 * 
 * 3、问号+数字(JPA方式)
 * <p>
 * 
 * <pre>
 * 
 * 示例：from User u where u.name =?0
 * </pre>
 * <p>
 * 
 * @author SC
 *
 */
public interface BaseDao<T, ID extends Serializable> {
	/**
	 * 持久化实例对象
	 * 
	 * @param t
	 *            持久化对象
	 * @return 主键id
	 */
	Serializable save(T t);

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	T get(ID id);

	/**
	 * 通过id查询(与get区别,load会延迟加载,查询不到会报异常,get找不到返回null)
	 * 
	 * @param id
	 * @return
	 */
	@Deprecated
	T load(ID id);

	/**
	 * 删除
	 * 
	 * @param t
	 */
	void delete(T t);

	/**
	 * 更新
	 * 
	 * @param t
	 */
	void update(T t);

	/**
	 * 合并实体对象（与update区别，update会直接更新，merge会先查询，结果不一致才会执行更新）
	 * 
	 * @param t
	 */
	T merge(T t);

	/**
	 * 持久化对象（与save区别，save会立即产生持久化标识符并返回，persist不保证立即产生持久化标识，无返回值）
	 * 
	 * @param t
	 */
	void persist(T t);

	/**
	 * 移除session中的缓存
	 * 
	 * @param t
	 */
	void evict(T t);

	/**
	 * 移除所有session缓存
	 */
	public void clear();

	/**
	 * 刷新所有session缓存
	 */
	public void flush();

	/**
	 * 删除或更新（可用于批量删除/更新，执行此操作需要进行清除一级与二级缓存）
	 * 
	 * @param hql
	 * @param values
	 */
	public void bulkUpdate(String hql, Object... values);

	/**
	 * 锁定实体对象
	 * 
	 * @param t
	 * @param lockMode
	 */
	void lock(T t, LockMode lockMode);

	/**
	 * 样式查询(不支持主键.null.空指针)--好像不能排序...
	 * 
	 * @param exampleEntity
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<T> findByExample(T exampleEntity, Integer firstResult, Integer maxResults);

	/**
	 * 通过hql语句查询
	 * 
	 * /** 查询列表
	 * 
	 * @param firstResult
	 *            起始记录 （分页）
	 * @param maxResults
	 *            记录数（分页）
	 * @param filters
	 *            条件
	 * @param orders
	 *            排序
	 * @return
	 */
	List<T> findList(Integer firstResult, Integer maxResults, List<Filter> filters, Order... orders);

	/**
	 * 通过hql语句查询(不支持in语句)
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	List<T> findList(String hql, Object... values);

	/**
	 * 参数命名占位符查询(支持in语句)
	 * 
	 * @param hql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	List<T> findByNamedParam(String hql, String[] paramNames, Object[] values);

	/**
	 * 查询记录数
	 * 
	 * @param filters
	 *            条件数组
	 * @return
	 */
	Long count(Filter... filters);

	/**
	 * 查询记录数
	 * 
	 * @param filters
	 *            条件集合
	 * @return
	 */
	Long count(List<Filter> filters);

	/**
	 * 条件查询
	 * 
	 * @param filters
	 *            条件数组
	 * @return
	 */
	List<T> findList(Filter... filters);

	/**
	 * 条件查询
	 * 
	 * @param filters
	 *            条件集合
	 * @return
	 */
	List<T> findList(List<Filter> filters);

	/**
	 * 条件查询
	 * 
	 * @param filters
	 *            条件集合
	 * @return
	 */
	List<T> findList(List<Filter> filters,Integer start,Integer length);
	
	/**
	 * 列表查询 find与iterate的区别
	 * 1.iterate会自动查询二级缓存,find需手动开启二级缓存(例如hibernateTemplate.setCacheQueries(true
	 * ))
	 * 2.iterate查询策略(1+n,先查出id列表,返回的是代理的对象(延迟加载,与load类似),当需要加载对象时,根据对象id去缓存中查,
	 * 查不到去数据库查,最多发出1+n条查询),
	 * find直接查(对于相同的查询,如果开启了二级缓存,先从缓存中取,再从数据库中取),最多发出1条查询语句
	 * 3.iterate使用不常用的结果集，频繁读写的操作使用find
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	Iterator<T> iterate(String hql, Object... values);

	/**
	 * 获取唯一实体(不支持in语句)---此种方式引用不推荐使用(因要使用？占位符，会出现警告)....
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	T unique(String hql, Object... values);

	/**
	 * 通过id查询列表
	 * 
	 * @param ids
	 * @return
	 */
	List<T> findByIds(List<ID> ids);

	/**
	 * 分页查询
	 * 
	 * @param pageInfo
	 *            分页
	 * @param filters
	 *            过滤条件
	 * @param orders
	 *            排序
	 * @return
	 */
	PageInfo findPage(PageInfo pageInfo, List<Filter> filters, Order... orders);

	/**
	 * HQL参数占位符使用方式
	 * <p>
	 * <p>
	 * 
	 * 
	 * <p>
	 * 
	 * 1、问号
	 * <p>
	 * 
	 * <pre>
	 * 示例：from User u where u.name = ?
	 * hibernate4之后这种方式会出现警告：[DEPRECATION] Encountered positional parameter near line 1, column 95.  Positional parameter are considered deprecated; use named parameters or JPA-style positional parameters instead.
	 * </pre>
	 * <p>
	 * 
	 * 2、冒号+参数别名
	 * <p>
	 * 
	 * <pre>
	 * 示例：from User u where u.name =:sname
	 * </pre>
	 * <p>
	 * 
	 * 3、问号+数字(JPA方式)
	 * <p>
	 * 
	 * <pre>
	 * 
	 * 示例：from User u where u.name =?0
	 * </pre>
	 * <p>
	 * 
	 * @author SC
	 *
	 */
	public Query createQueryByMap(String hql, Map<String, Object> param);

	/**
	 * 获取原生sqlquery
	 * 
	 * @param sql
	 * @return
	 */
	public SQLQuery createSQLQuery(String sql);

	public SQLQuery createSQLQuery(String sql, Map<String, Object> param);
	/**
	 * 原始sql语句
	 * 
	 * @param sql
	 *            原生sql语句
	 * @return
	 */
	public List<T> findListBySql(String sql);

	/**
	 * 原生sql语句查询
	 * 
	 * @param sql
	 *            原生sql语句
	 * @return
	 */
	public Object findSingleBySql(String sql);

	/**
	 * 原生sql语句更新
	 * 
	 * @param sql
	 *            原生SQL语句
	 */
	public int updateBySql(String sql);

	/**
	 * 分页(通过criteria条件查询)
	 * 
	 * @param criteria
	 * @return
	 */
	PageInfo findPage(PageInfo pageInfo, DetachedCriteria criteria);

	/**
	 * 分页查询(使用聚合函数的分页查询)
	 * 
	 * @param pageInfo
	 * @param criteria
	 * @return
	 */
	PageInfo findPageInAggregation(PageInfo pageInfo, DetachedCriteria criteria);


	DataSource getDataSource();

	int delete(List<ID> ids);
	
	/**离线查询记录数
	 * @param criteria
	 * @return
	 */
	Long count(DetachedCriteria criteria);
	
	/**离线查询列表
	 * @param criteria
	 * @return
	 */
	List<?> findList(DetachedCriteria criteria,PageInfo pageInfo);
}