package com.User_Management_System.controller;

import com.User_Management_System.service.DefaultUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.User_Management_System.config.JwtGeneratorValidator;
import com.User_Management_System.model.UserDTO;
import com.User_Management_System.service.DefaultUserService;

@RestController
@RequestMapping("/users")
public class RestAppController {

	@Autowired private AuthenticationManager authManager;
	@Autowired private JwtGeneratorValidator jwtGenVal;
	@Autowired private DefaultUserService userService;
	@Autowired private DefaultUserServiceImpl sir;

	@PostMapping("/login")
	public String generateJwtToken(@RequestBody UserDTO userDto) throws Exception {
		return sir.generateToken(userDto);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> createUser(@RequestBody UserDTO userDto) {
		return userService.createUser(userDto);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteUser(@PathVariable int id) {
		return userService.deleteUser(id);
	}

	@PutMapping("/me")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
	public ResponseEntity<Object> updateCurrentUser(@RequestBody UserDTO userDto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.updateCurrentUser(username, userDto);
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
	public ResponseEntity<Object> getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.getCurrentUser(username);
	}
}
