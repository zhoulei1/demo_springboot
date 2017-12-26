package com.example.config;

import javax.persistence.EntityManager;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass=true) // 作用同于<tx:annotation-driven>
/**
 * 注:此处没有@EnableTransactionManagement事物也能生效 spring-boot-starter-data-jpa
 * 自动注入一个JpaTransactionManager 已经声明的@EnableTransactionManagement,
 * 遇到其他事物,可能会进行重用(详见springboot自动注解
 * spring-boot-autoconfigure,配置文件中以spring.jpa.xxx)
 * https://stackoverflow.com/questions/40724100/enabletransactionmanagement-in-
 * spring-boot
 */
@ConfigurationProperties(prefix = "spring")
// @PropertySource("") 制定配置文件,没有使用默认配置文件
public class HibernateSessionFactory {
	@Autowired
	DatasourceConfig datasourceConfig;

	// 提供set获取配置里的值,获取方式 spring.hibernate.xxx,其中xxx对应HibernateProperties的属性值
	private HibernateProperties hibernate;

	public HibernateProperties getHibernate() {
		return hibernate;
	}

	public void setHibernate(HibernateProperties hibernate) {
		this.hibernate = hibernate;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(datasourceConfig.getDataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.example.model" });
		sessionFactory.setHibernateProperties(hibernate.getProperties());
		return sessionFactory;
	}

	/**
	 * 使用hibernateTemplate必须使用事物(绑定session到当前线程,结束自动关闭)
	 * hibernate的对象操作基本都是通过session进行,而hibernate实现jpa,则是通过entitymanager实现
	 * 其基本对应关系如下: SessionFactory(线程安全) <--> EntityManagerFactory (线程安全)
	 * Session(线程不安全) <--> EntityManager (线程安全)
	 */
	@Bean(name = "hibernateTemplate")
	public HibernateTemplate hibernateTemplate() {
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		//hibernateTemplate.setCacheQueries(true);是否开启查询缓存
		hibernateTemplate.setSessionFactory(sessionFactory().getObject());
		return hibernateTemplate;
	}

	// 事物管理器
	@Bean(name = "transactionManager")
	@Primary // 优先,多个同类bean时配置
	// @ConditionalOnMissingBean 条件创建,有则不进行创建
	public HibernateTransactionManager transactionManager() throws Throwable {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	/*********** 自定义entitymanager ****************/
    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
   }
	
	@Bean(name = "entityFactory")
	public LocalContainerEntityManagerFactoryBean entityFactory() {
		LocalContainerEntityManagerFactoryBean build = new LocalContainerEntityManagerFactoryBean();
		build.setDataSource(datasourceConfig.getDataSource());
		build.setPackagesToScan("com.example.model");
		build.setJpaVendorAdapter(vendorAdaptor());
		build.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		build.setJpaProperties(hibernate.getProperties());
		return build;
	}

	@Bean(name = "entityManager")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityFactory().getObject().createEntityManager();
	}

	@Bean(name = "jpaTransactionManager")
	public JpaTransactionManager transactionManagerSecondary() {
		return new JpaTransactionManager(entityFactory().getObject());
	}

}