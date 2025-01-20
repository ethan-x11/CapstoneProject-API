package com.capstone.qwikpay.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repo;
	
	//Add new user
	@Override
	public UserEntity addNewUser(UserEntity user) {
		return repo.save(user);
	}
	//Get user by ID with exception handling
	@Override
	public UserEntity getUser(Integer userId) throws UserNotFoundException {
		Optional<UserEntity> user = repo.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}else {
			//throw exception if user not found
			throw new UserNotFoundException("User with ID " + userId + " not found");
		}
	
	}

	@Override
    public UserEntity updateUser(UserEntity user) throws UserNotFoundException {
        // Check if the user exists
        if (repo.existsById(user.getId())) {
            // Save and update user
            return repo.save(user);
        } else {
            // Throw exception if user does not exist
            throw new UserNotFoundException("User with ID " + user.getId() + " not found");
        }
    }

	@Override
    public String deleteUser(Integer userId) throws UserNotFoundException {
        if (repo.existsById(userId)) {
            repo.deleteById(userId);
            return "User with ID " + userId + " deleted successfully";
        } else {
            // Throw exception if user to delete doesn't exist
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

	@Override
	public List<UserEntity> getUsers() {
		// TODO Auto-generated method stub
		return (List<UserEntity>) repo.findAll();
	}
	
}
