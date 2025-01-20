package com.capstone.qwikpay.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	public Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);


	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);

	public Boolean existsByMobile(String mobile);

}
