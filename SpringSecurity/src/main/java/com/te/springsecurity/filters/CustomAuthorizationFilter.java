package com.te.springsecurity.filters;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/api/login")) {
			filterChain.doFilter(request, response);
		}else {
			String header = request.getHeader("Authorization");
			if(header!=null && header.startsWith("Bearer ")) {
				try {
					String token = header.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("SECRETKEYHKR".getBytes());
					 JWTVerifier verifier = JWT.require(algorithm).build();
					 DecodedJWT decodedJWT = verifier.verify(token);
					 String username = decodedJWT.getSubject();
					  List<String> asList = decodedJWT.getClaim("roles").asList(String.class);
					 List<SimpleGrantedAuthority> authorities=new ArrayList<>();
					 asList.forEach(role -> {
						 authorities.add(new SimpleGrantedAuthority(role));
					 });
					 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					 filterChain.doFilter(request, response);
				} catch (Exception e) {
					log.error(e.getMessage());
					response.setHeader("error", e.getMessage());
					response.setStatus(404);
					Map<String, String> map=new HashMap<String, String>();
					map.put("error_message", e.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), map);
				}
				
			}else {
				filterChain.doFilter(request, response);
			}
		}
		
	}

}
