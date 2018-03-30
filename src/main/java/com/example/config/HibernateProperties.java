package com.example.config;

import java.util.Properties;

/**
 * hibernate配置
 * 
 * @author Administrator
 *
 */
public class HibernateProperties {

	private String dialect;
	private boolean show_sql;
	private boolean format_sql;
	private boolean use_sql_comments;
	private int fetch_size;
	private int batch_size;
	private boolean use_scrollable_resultset;
	private boolean use_query_cache;
	private boolean use_second_level_cache;
	private String hbm2ddl_auto;
	private String factory_class;

	private Properties properties = new Properties();
	
	

	public String getDialect() {
		return dialect;
	}



	public void setDialect(String dialect) {
		this.dialect = dialect;
	}



	public boolean isShow_sql() {
		return show_sql;
	}



	public void setShow_sql(boolean show_sql) {
		this.show_sql = show_sql;
	}



	public boolean isFormat_sql() {
		return format_sql;
	}



	public void setFormat_sql(boolean format_sql) {
		this.format_sql = format_sql;
	}



	public boolean isUse_sql_comments() {
		return use_sql_comments;
	}



	public void setUse_sql_comments(boolean use_sql_comments) {
		this.use_sql_comments = use_sql_comments;
	}

	public int getFetch_size() {
		return fetch_size;
	}



	public void setFetch_size(int fetch_size) {
		this.fetch_size = fetch_size;
	}



	public int getBatch_size() {
		return batch_size;
	}



	public void setBatch_size(int batch_size) {
		this.batch_size = batch_size;
	}



	public boolean isUse_scrollable_resultset() {
		return use_scrollable_resultset;
	}



	public void setUse_scrollable_resultset(boolean use_scrollable_resultset) {
		this.use_scrollable_resultset = use_scrollable_resultset;
	}



	public boolean isUse_query_cache() {
		return use_query_cache;
	}



	public void setUse_query_cache(boolean use_query_cache) {
		this.use_query_cache = use_query_cache;
	}



	public boolean isUse_second_level_cache() {
		return use_second_level_cache;
	}



	public void setUse_second_level_cache(boolean use_second_level_cache) {
		this.use_second_level_cache = use_second_level_cache;
	}



	public String getFactory_class() {
		return factory_class;
	}



	public void setFactory_class(String factory_class) {
		this.factory_class = factory_class;
	}



	public void setProperties(Properties properties) {
		this.properties = properties;
	}



	public String getHbm2ddl_auto() {
		return hbm2ddl_auto;
	}



	public void setHbm2ddl_auto(String hbm2ddl_auto) {
		this.hbm2ddl_auto = hbm2ddl_auto;
	}



	public Properties getProperties() {
		if (properties.isEmpty()) {
			properties.setProperty("hibernate.dialect", dialect);
			properties.setProperty("hibernate.show_sql", String.valueOf(show_sql));
			properties.setProperty("hibernate.format_sql", String.valueOf(format_sql));
			properties.setProperty("hibernate.use_sql_comments", String.valueOf(use_sql_comments));
			properties.setProperty("hibernate.hbm2ddl.auto", String.valueOf(hbm2ddl_auto));
			//properties.setProperty("hibernate.jdbc_use_scrollable_resultset", String.valueOf(fetch_size));
			//properties.setProperty("hibernate.jdbc_batch_size", String.valueOf(batch_size));
			//properties.setProperty("hibernate.jdbc.use_scrollable_resultset", String.valueOf(use_scrollable_resultset));
			properties.setProperty("hibernate.cache.use_query_cache", String.valueOf(use_query_cache));
			properties.setProperty("hibernate.cache.use_second_level_cache", String.valueOf(use_second_level_cache));
			properties.setProperty("hibernate.cache.region.factory_class", String.valueOf(factory_class));
		}
		return properties;
	}

}