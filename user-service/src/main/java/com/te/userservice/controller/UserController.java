package com.te.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.userservice.entity.User;
import com.te.userservice.service.UserService;
import com.te.userservice.vo.Department;
import com.te.userservice.vo.UserDepartmentVo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public User saveUser(@RequestBody User user) {

		return userService.saveUser(user);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserDepartmentVo getUser(@PathVariable("id") long userId,@RequestHeader(value="Authorization") String authorizationHeader) {
		return userService.getUser(userId,authorizationHeader);
	}

	// Rate limiter Resilience4j example

	@GetMapping("/msg")
//	@RateLimiter(name = "getMsg", fallbackMethod = "getMessageFallbackMethod")
	public String getMessage() {
		return "Welcome to the Developers World.........";

	}

	public String getMessageFallbackMethod(RequestNotPermitted e) {
		return "Too many requests : No further request will be accepted. Please try after sometime";

	}

	// Retry Resilience4J example

	@GetMapping("/retry/{id}")
	@Retry(name = "getUserByUsingRetry", fallbackMethod = "retryFallbackMethod")
	public UserDepartmentVo getUserByUsingRetry(@PathVariable("id") long userId,@RequestHeader(value="Authorization") String authorizationHeader) {
		log.info("inside getUserByUsingRetry method");
		return userService.getUser(userId,authorizationHeader);
	}

	public UserDepartmentVo retryFallbackMethod(Exception e) {

		return null;
	}

	//Circuit Breaker Resilience4J example
	@GetMapping("/circuitbreaker/{id}")
	@CircuitBreaker(name = "getUserByUsingCB", fallbackMethod = "cBFallbackMethod")
	public UserDepartmentVo getUserByUsingCB(@PathVariable("id") long userId,@RequestHeader(value="Authorization") String authorizationHeader) {

		return userService.getUser(userId,authorizationHeader);
	}

	
	public UserDepartmentVo cBFallbackMethod(Exception e) {

		return new UserDepartmentVo(new User(1, "Dummy", "Dummy", "Dummy", 1), new Department(1, "Dummy", "Dummy", "Dummy"));
	}

}
