package com.example.dao.impl;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.dao.RoleDao;
import com.example.model.Role;

@Repository
@Transactional
public class RoleDaoImpl extends BaseDaoImpl<Role, Serializable> implements RoleDao{
	
	
	
	
	
/*	@PersistenceContext
	EntityManager entityManager;
	@Override
	public void save(Role r) {
		entityManager.persist(r);
		entityManager.flush();
	}
	@Override
	public Role get(Integer id) {
		return entityManager.find(Role.class, id);
	}*/
}
