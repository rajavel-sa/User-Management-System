package com.User_Management_System.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name= "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

//	@Column(nullable = false, unique = true)//use this in presentation
    private String userName;
    private String password;
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "cust_id", referencedColumnName = "id"),			//change col name
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
	Set<Role> roles = new HashSet<>();                                                              					//	Set<Role> roles = new HashSet<Role>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Role> getRole() {
		return roles;
	}
	public void setRole(Role role) {
		this.roles.add(role);
	}
}
