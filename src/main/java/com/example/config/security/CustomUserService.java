package com.example.config.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;

//@Service
public class CustomUserService implements UserDetailsService {
	@Autowired
	UserService userService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
        for(Role role:user.getRoles())
        {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
        //数据库存储的原文,此处需要加密
       return new org.springframework.security.core.userdetails.User(user.getUsername(),
    		   passwordEncoder.encode(user.getPassword()), authorities);
   /*    return new org.springframework.security.core.userdetails.User(user.getUsername(),
    		   user.getPassword(), authorities);*/
    
	}
}
