package com.example.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.example.service.UserService;
import com.example.util.CustomSecurityContext;

@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	public static Collection<ConfigAttribute> extraMetadataSource ;
	@Autowired
	UserService userService;
	static {
		extraMetadataSource = new ArrayList<>();
		extraMetadataSource.add(new SecurityConfig("permitAll"));
	}
	/* 获取当前访问url所需要的权限
	 * 返回null 表示不需要权限,后续也不需要进行个人权限认证(包括登录)
	 * 返回非空,后续进行个人权限认证
	 * 	 */
	@Override
	@Transactional
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		//获取需要权限验证
        Map<String, Collection<ConfigAttribute>> metadataSource = CustomSecurityContext.getMetadataSource();

        for (Map.Entry<String, Collection<ConfigAttribute>> entry : metadataSource.entrySet()) {
        	System.out.print(entry.getKey()+"::");
        	for (ConfigAttribute c : entry.getValue()) {
        		System.out.print(c.getAttribute()+",");
				
			}
            String uri = entry.getKey();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
            if (requestMatcher.matches(request)) {
                return entry.getValue();
            }
        }
        
        //不需要验证
        return null;	
	
	}
	
	/* 
	 * This is used by the AbstractSecurityInterceptor to 
	 * perform startup time validation of each ConfigAttribute configured against it.
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
}