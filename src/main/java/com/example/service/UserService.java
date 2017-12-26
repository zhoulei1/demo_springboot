package com.example.service;

import java.io.Serializable;

import com.example.model.User;

public interface UserService extends BaseService<User, Serializable>{

	User findByUsername(String username);
	
	/**
	 * 持久化实例对象
	 */
	Integer save(User t);
}
