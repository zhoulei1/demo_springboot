package com.example.service.impl;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dao.UserDao;
import com.example.model.User;
import com.example.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Serializable>implements UserService {
    @Autowired
	BCryptPasswordEncoder  passwordEncoder;
	@Autowired
    public void setBaseDao(UserDao userDao) {
    	super.setBaseDao(userDao);
    }

	@Override
	@Cacheable("userCache")
	//@CachePut  
	//@CacheEvict 
	public User findByUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		return findByHql("FROM User u where u.username=?", username).get(0);
	}

	/**
	 * 持久化实例对象
	 */
	@Override
	public Integer save(User t){
		passwordEncoder.encode(t.getPassword());
		return (Integer) super.save(t);
	};

}
