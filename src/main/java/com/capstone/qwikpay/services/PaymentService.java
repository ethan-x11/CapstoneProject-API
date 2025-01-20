package com.capstone.qwikpay.services;

import java.util.List;

import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;

public interface PaymentService {

    // Process a new payment
    Payment processPayment(Payment payment) throws PaymentFailedException;

    // Validate a payment by ID
    boolean validatePayment(Integer paymentId) throws PaymentFailedException;

    // Retrieve a payment by ID
    Payment getPaymentById(Integer paymentId);
    
    //Retrieve payments by user Id
    List<Payment> getPaymentsByUserId(int userId);
    
    List<Payment> getPaymentByStatus(String paymentStatus);

    // Retrieve all payments
    List<Payment> getAllPayments();

    // Update an existing payment
    Payment updatePayment(Integer paymentId, Payment updatedPayment) throws PaymentFailedException;

    // Delete a payment by ID
    void deletePayment(Integer paymentId);
}
