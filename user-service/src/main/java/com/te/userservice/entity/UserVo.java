package com.te.userservice.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

	
	private long id;
	private String name;
	private String username;
	private String password;
	
	
	private List<Role> roles;
}
