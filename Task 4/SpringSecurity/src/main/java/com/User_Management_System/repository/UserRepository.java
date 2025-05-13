package com.User_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.User_Management_System.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByUserName(String username);
	User findByEmail(String username);
}