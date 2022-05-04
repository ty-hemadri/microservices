package com.te.departmentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.departmentservice.entity.Department;
import com.te.departmentservice.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repository;

	public Department saveDepartment(Department department) {
		return repository.save(department);
	}

	public Department getDepartment(long departmentId) {
		return repository.findById(departmentId).get();
	}

}
