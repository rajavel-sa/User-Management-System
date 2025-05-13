package com.User_Management_System.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.User_Management_System.model.UserDTO;

public interface DefaultUserService extends UserDetailsService {
	ResponseEntity<Object> createUser(UserDTO userRegisteredDTO);
	ResponseEntity<Object> deleteUser(int id);
	ResponseEntity<Object> updateCurrentUser(String username, UserDTO userDto);
	ResponseEntity<Object> getCurrentUser(String username);
}