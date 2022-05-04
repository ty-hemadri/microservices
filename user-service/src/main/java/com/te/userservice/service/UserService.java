package com.te.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.userservice.entity.User;
import com.te.userservice.repository.FeignClientConsumer;
import com.te.userservice.repository.UserRepository;
import com.te.userservice.vo.Department;
import com.te.userservice.vo.UserDepartmentVo;

@Service
public class UserService  {

	@Autowired
	private UserRepository userRepository;
	
//	@Autowired
//	private RestTemplate restTemplate;
	
	@Autowired
	private FeignClientConsumer consumer;
	


	public User saveUser(User user) {
		
		return userRepository.save(user);
	}

	public UserDepartmentVo getUser(long userId,String token) {
		
		User user = userRepository.findById(userId).get();
//		Department department = restTemplate.getForObject("http://DEPARTMENT-SERVICE/department/"+user.getDepartmentId(), Department.class);
		Department department = consumer.getDepartment(token,userId);
		UserDepartmentVo userDepartmentVo=new UserDepartmentVo(user, department);
		return userDepartmentVo;
	}


	
}
