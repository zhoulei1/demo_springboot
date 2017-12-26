package com.example.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	  @Autowired
	 CustomAuthenticationProvider authenticationProvider;
	  @Autowired
	  OpenSessionInViewFilter openSessionInViewFilter; 
	  
	  @Autowired
	  CustomAccessDecisionManager accessDecisionManager;
	  @Autowired
	  CustomSecurityMetadataSource customSecurityMetadataSource;
	  @Autowired
	  CustomAuthenticationDetailsSource customAuthenticationDetailsSource;
	  @Autowired
	  AuthenticationManager authenticationManager;
	  
	  @Autowired
	  AuthenticationEntryPoint authenticationEntryPoint;
	
	 
/*	自定义分支	
 * public void init(final WebSecurity web) throws Exception {
			final HttpSecurity http = getHttp();
			web.addSecurityFilterChainBuilder(http).postBuildAction(new Runnable() {
				public void run() {
					FilterSecurityInterceptor securityInterceptor = http
							.getSharedObject(FilterSecurityInterceptor.class);
					web.securityInterceptor(securityInterceptor);
				}
			});
		}*/
	 
    /* 
     * configure path
     */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
		http.anonymous().disable().
		authorizeRequests()
				// 除'/','/home',其他url均需要登录认证 (给资源赋权限)
				.antMatchers("/", "/home")// 控制资源
				.permitAll()// 授权 ，细粒度 hasAnyRole hasRole
				.anyRequest().authenticated()
				// 认证
				.and().formLogin()
				// 登录设置
				.loginPage("/login").defaultSuccessUrl("/hello")
			    //.loginProcessingUrl("/loginsuccess")
				.permitAll().authenticationDetailsSource(customAuthenticationDetailsSource).and().logout()
				// 登出设置
				.logoutSuccessUrl("/home").permitAll().
				and().authorizeRequests().anyRequest().authenticated()
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
						fsi.setAccessDecisionManager(accessDecisionManager);
						fsi.setSecurityMetadataSource(customSecurityMetadataSource);
						return fsi;
					}
				})
				//异常
				.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.and().addFilterBefore(openSessionInViewFilter, FilterSecurityInterceptor.class);
                ;
    }
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("test").password("1");
    }*/
    
    
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
/*        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        builder.authenticationProvider(authenticationProvider);*/
    	builder.authenticationProvider(authenticationProvider);
    }
    
    
    
    
}