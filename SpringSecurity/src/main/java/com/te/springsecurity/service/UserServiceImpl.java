package com.te.springsecurity.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.te.springsecurity.entity.Role;
import com.te.springsecurity.entity.User;
import com.te.springsecurity.repository.RoleRepository;
import com.te.springsecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService ,UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User saveUser(User user) {
		log.info("Save User ");
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Save Role");
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		log.info("Adding Role To User");
		User user = userRepository.findByUsername(username);
		Role role = roleRepository.findByName(roleName);
		user.getRoles().add(role);
		
		
	}

	@Override
	public User getUser(String username) {
		log.info("get user");
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		log.info("get list of users");
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user==null) {
			log.error("User is not Found");
			throw new UsernameNotFoundException("User is not Found");
		}else {
			log.info("User is Found.");
		}
		List<SimpleGrantedAuthority> authorities=new ArrayList<>();
		user.getRoles().forEach(x -> authorities.add(new SimpleGrantedAuthority(x.getName())));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
