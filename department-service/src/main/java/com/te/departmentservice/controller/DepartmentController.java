package com.te.departmentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.departmentservice.entity.Department;
import com.te.departmentservice.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService service;

	@PostMapping("/")
	public Department saveDepartment(@RequestBody Department department) {
		return service.saveDepartment(department);

	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public Department getDepartment(@PathVariable("id") long departmentId) {
		return service.getDepartment(departmentId);
	}

}
