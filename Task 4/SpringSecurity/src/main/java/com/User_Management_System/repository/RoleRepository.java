package com.User_Management_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.User_Management_System.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRole(String role);
}
