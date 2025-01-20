package com.capstone.qwikpay.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Bill;

@Repository
public interface BillRepository extends CrudRepository<Bill, Integer> {

    List<Bill> findByUserId(Integer userId);
    List<Bill> findByBillStatus(String status);
}
