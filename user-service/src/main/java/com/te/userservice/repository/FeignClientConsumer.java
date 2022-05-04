package com.te.userservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.te.userservice.vo.Department;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface FeignClientConsumer {

	@GetMapping("/department/{id}")
	public Department getDepartment(@RequestHeader("Authorization") String token,@PathVariable("id") long departmentId);
}
