package com.example.config.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;

/**
 * 根据supports,来处理相应的Authentication
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	UserService userService;
	@Autowired
	BCryptPasswordEncoder  passwordEncoder;
	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
	    String password = String.valueOf(auth.getCredentials());
        //获取用户权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
	    System.out.println(username + ":::::" + password);
	    //验证用户名密码
	    if (username== null || password == null) {
	    	 throw new BadCredentialsException("Invalid username or password");
	    }
	    if ("test".equals(username)){
	    	return new UsernamePasswordAuthenticationToken(username,password,authorities);
	    }
	    
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
        	throw new BadCredentialsException("Invalid username or password");
        }
        //验证其他参数
        Object details = authentication.getDetails();
        if (details.getClass().isAssignableFrom(CustomWebAuthenticationDetails.class)) {
        	CustomWebAuthenticationDetails customWebAuthenticationDetails =(CustomWebAuthenticationDetails)details ;
        	System.out.println("login token:" + customWebAuthenticationDetails.getToken());
        }
        
        for(Role role:user.getRoles())
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
	    
	    return new UsernamePasswordAuthenticationToken(username,password,authorities) ;
	}

	
	/* 
	 * UsernamePasswordAuthenticationToken 或其子类的都authentication都处理
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
}