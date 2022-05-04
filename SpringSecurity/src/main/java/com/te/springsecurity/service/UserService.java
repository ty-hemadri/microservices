package com.te.springsecurity.service;

import java.util.List;

import com.te.springsecurity.entity.Role;
import com.te.springsecurity.entity.User;


public interface UserService {
	
	User saveUser(User user);
	
	Role saveRole(Role role);
	
	void addRoleToUser(String username,String roleName);
	
	User getUser(String username);
	
	List<User> getUsers();

}
