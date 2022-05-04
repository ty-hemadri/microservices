package com.te.springsecurity.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.springsecurity.entity.Role;
import com.te.springsecurity.entity.User;
import com.te.springsecurity.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/save/user")
	public ResponseEntity<User> saveUser(@RequestBody User user){
		return ResponseEntity.ok().body(userService.saveUser(user));
	}
	
	@PostMapping("/save/role")
	public ResponseEntity<Role> saveRole(@RequestBody Role role){
		return ResponseEntity.ok().body(userService.saveRole(role));
	}
	
	@GetMapping("/get/user/{username}")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> getUser(String username){
		return ResponseEntity.ok().body(userService.getUser(username));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/get/users")
	public ResponseEntity<List<User>> getUsers(){
		return ResponseEntity.ok().body(userService.getUsers());
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String header = request.getHeader("Authorization");
		if(header!=null && header.startsWith("Bearer ")) {
			try {
				String refresh_token = header.substring("Bearer ".length());
				Algorithm algorithm= Algorithm.HMAC256("SECRETKEYHKR".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = userService.getUser(username);
				String access_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() +10*60*1000))
				.withIssuer(request.getRequestURI().toString())
				.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
				.sign(algorithm);
				Map<String, String> tokens=new HashMap<String, String>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
				
				response.setHeader("error", e.getMessage());
				response.setStatus(404);
				Map<String, String> map=new HashMap<String, String>();
				map.put("error_message", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), map);
			}
		}else {
			throw new RuntimeException("Refresh Token is Missing");
		}
	}

}
