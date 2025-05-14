package com.User_Management_System.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.User_Management_System.config.JwtGeneratorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.User_Management_System.model.Role;
import com.User_Management_System.model.User;
import com.User_Management_System.model.UserDTO;
import com.User_Management_System.repository.RoleRepository;
import com.User_Management_System.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService {
	@Autowired private UserRepository userRepo;
	@Autowired private RoleRepository roleRepo;
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authManager;
	@Autowired private JwtGeneratorValidator jwtGenVal;
	@Autowired private DefaultUserService userService;

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
public ResponseEntity<Object> generateToken(UserDTO userDto) {
	try {
		User user = userRepo.findByUserName(userDto.getUserName());
		if (user == null) {
			return generateRespose("Username does not exist", HttpStatus.NOT_FOUND, null);
		}

		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenVal.generateToken(authentication);

		return generateRespose("Login successful", HttpStatus.OK, token);

	} catch (Exception e) {
		return generateRespose("Password is wrong", HttpStatus.UNAUTHORIZED, null);
	}
}

	///////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ResponseEntity<Object> createUser(UserDTO userRegisteredDTO) {
		try {
			if (userRepo.findByUserName(userRegisteredDTO.getUserName()) != null) {
				return generateRespose("Username already exists", HttpStatus.CONFLICT, null);
			}
			
			if (userRepo.findByEmail(userRegisteredDTO.getEmail()) != null) {
				return generateRespose("Email already registered", HttpStatus.CONFLICT, null);
			}

			if (!userRegisteredDTO.getEmail().toLowerCase().endsWith("@pentafox.in")) {
				return generateRespose("Provie your @pentafox.in mail id", HttpStatus.BAD_REQUEST, null);
			}

			Role role = roleRepo.findByRole("ROLE_" + userRegisteredDTO.getRole());
			if (role == null) {
				return generateRespose("Invalid role specified", HttpStatus.BAD_REQUEST, null);
			}

			User user = new User();
			user.setEmail(userRegisteredDTO.getEmail());
			user.setUserName(userRegisteredDTO.getUserName());
			user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
			user.setRole(role);

			User savedUser = userRepo.save(user);
			return generateRespose("User created successfully", HttpStatus.CREATED, savedUser);

		} catch (Exception e) {
			return generateRespose("Error creating user: " + e.getMessage(), HttpStatus.BAD_REQUEST, null);
		}
	}

	@Override
	public ResponseEntity<Object> deleteUser(int id) {
		if (!userRepo.existsById(id)) {
			return generateResponse("User not found", HttpStatus.NOT_FOUND, null);
		}
		userRepo.deleteById(id);
		return generateResponse("User deleted successfully", HttpStatus.OK, null);
	}

	@Override
	public ResponseEntity<Object> updateCurrentUser(String username, UserDTO userDto) {
		User existingUser = userRepo.findByUserName(username);
		if (userDto.getEmail() != null) {
			existingUser.setEmail(userDto.getEmail());
		}
		if (userDto.getPassword() != null) {
			existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}

		User updatedUser = userRepo.save(existingUser);
		return generateResponse("User updated successfully", HttpStatus.OK, updatedUser);
	}

	@Override
	public ResponseEntity<Object> getCurrentUser(String username) {
		User user = userRepo.findByUserName(username);
		return generateResponse("User Details Fetched", HttpStatus.OK, user);
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRole()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole()))
				.collect(Collectors.toList());
	}

	private ResponseEntity<Object> generateResponse(String message, HttpStatus st, Object responseobj) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("Status", st.value());
		map.put("data", responseobj);
		return new ResponseEntity<>(map, st);
	}
}
