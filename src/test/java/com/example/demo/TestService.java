package com.example.demo;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.DemoApplication;
import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.service.UserService;  
  
  
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(DemoApplication.class)
//@Transactional
@Rollback(false)
public class TestService {  
  
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;
      
    @Test 
    public void testShow() {  
    	System.out.println(userDao.get(1).getUsername());
    	System.out.println(userDao.get(1).getUsername());
    	;
    }  
  
}  
