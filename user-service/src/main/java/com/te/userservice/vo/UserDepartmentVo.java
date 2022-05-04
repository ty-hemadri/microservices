package com.te.userservice.vo;

import com.te.userservice.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDepartmentVo {
	
	private User user;
	
	private Department department;

}
