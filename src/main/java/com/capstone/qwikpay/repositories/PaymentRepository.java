package com.capstone.qwikpay.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capstone.qwikpay.entities.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    //List<Payment> findByBillId(Integer billId);

    List<Payment> findByPaymentStatus(String paymentStatus);
    
//    List<Payment> findByUserId(int userId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.bill.billId = :billId")
    Double getTotalPaidAmountForBill(@Param("billId") Integer billId);
}
