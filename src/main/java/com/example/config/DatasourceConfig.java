package com.example.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class DatasourceConfig {
	
	//@Qualifier(value = "dataSource")--配合@Autowired使用  用于多实现bean
	@Bean(name = "dataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.db")//读取配置文件中的spring.DB.xxx,其中xxx与创建DataSource所必须
	public ComboPooledDataSource getDataSource() {
		ComboPooledDataSource build = (ComboPooledDataSource) DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
		return build;
	}
	

	//可配置多个数据源
//	@Bean(name = "stDataSource")
//	@ConfigurationProperties(prefix = "spring.stDB")
//	public DataSource getStDataSource() {
//		return DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
//	}

}
