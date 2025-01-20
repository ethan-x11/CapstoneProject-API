package com.capstone.qwikpay.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Role;
import com.capstone.qwikpay.enums.ERole;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
  Optional<Role> findByName(ERole name);
}