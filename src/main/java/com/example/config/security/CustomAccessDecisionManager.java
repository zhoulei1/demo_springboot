package com.example.config.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

/**
 * authorization
 *
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		//过滤测试账号
		if ("test".equals(authentication.getPrincipal()))return;
		int vote = vote(authentication, object, configAttributes);
		if (vote <= 0) {
			throw new AccessDeniedException("CustomAccessDecisionManager.accessDenied : Access is denied");
		}
		//or use many voters in  different strategies :AffirmativeBased,ConsensusBased,ConsensusBased...
	}
	
	private int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		if(authentication == null) {
			return -1;
		}
		int result = 0;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = -1;
				// Attempt to find a matching granted authority
				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equalsIgnoreCase(authority.getAuthority())) {
						return 1;
					}
				}
			}
		}
		return result;
	}
	
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}
	@Override
	public boolean supports(Class<?> clazz) {
		 return FilterInvocation.class.isAssignableFrom(clazz);
	}
}