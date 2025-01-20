package com.capstone.qwikpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.services.UserService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	/*
	 * //Create user
	 * 
	 * @PreAuthorize("hasRole('ADMIN')")
	 * 
	 * @PostMapping("/new") public UserEntity addNewUser(@RequestBody UserEntity
	 * user) { //Check for roles if it's null then add default role USER
	 * if(user.getRoles() == null) {
	 * user.setRoles(roleUtility.getRolesFromDB(user.getRoles())); }
	 * 
	 * return service.addNewUser(user); }
	 */
	
	
	//Get user by Id
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get/{id}")
	public UserEntity getUser(@PathVariable("id")Integer userId) throws UserNotFoundException {
		return service.getUser(userId);
	}
	
	//Update user by Id
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public UserEntity updateUser(@RequestBody UserEntity user) throws UserNotFoundException {
		return service.updateUser(user);
	}
	
	//Delete user by id
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer userId) throws UserNotFoundException {
		return service.deleteUser(userId);
	}
	
	// committed the code
	//Get all users 
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public List<UserEntity> getUsers(){
		return service.getUsers();
	}
	
}