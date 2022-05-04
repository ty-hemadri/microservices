package com.te.userservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.te.userservice.entity.UserVo;

@FeignClient(name = "SECURITY-SERVICE")
public interface FeignClientUserComsumer {

	@GetMapping("/get/user/{username}")
	public ResponseEntity<UserVo> getUser(String username);
}
