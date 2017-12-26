package com.example.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.example.dao.UserDao;
import com.example.model.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Serializable>implements UserDao{
	
}
