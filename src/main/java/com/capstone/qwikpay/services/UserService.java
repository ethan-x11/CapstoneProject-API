package com.capstone.qwikpay.services;

import java.util.List;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;


public interface UserService {
	public UserEntity addNewUser(UserEntity user);
	//Retrieve
	public UserEntity getUser(Integer userId) throws UserNotFoundException;
	//Update
	public UserEntity updateUser(UserEntity user) throws UserNotFoundException ;
	//Delete
	public String deleteUser(Integer userId) throws UserNotFoundException ;
	
	//Retrieve All
	public List<UserEntity> getUsers();
}