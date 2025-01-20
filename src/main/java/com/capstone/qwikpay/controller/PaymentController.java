package com.capstone.qwikpay.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired BillRepository billRepository;

    // Process a new payment
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) throws PaymentFailedException {
        if (payment.getBillId() == null) {
            throw new PaymentFailedException("Bill ID must not be null");
        }

        // Manually set the bill using the bill_id
        Bill bill = billRepository.findById(payment.getBillId())
                .orElseThrow(() -> new PaymentFailedException("Bill not found with ID: " + payment.getBillId()));

        // Set the Bill object in the Payment entity manually
        payment.setBill(bill);

        // Process the payment
        Payment processedPayment = paymentService.processPayment(payment);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }

    
    //Retrieve the payment details by the status
    
    @GetMapping("/retrieveByStatus/{status}")
    public ResponseEntity<?> getPaymentByStatus(@PathVariable("status") String status) {
        List<Payment> payments = paymentService.getPaymentByStatus(status);
        
        // Check if the result is empty
        if (payments == null || payments.isEmpty()) {
            // Return a custom message with HTTP 404 status
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No payments found with status: " + status);
        }
        
        return ResponseEntity.ok(payments);
    }

    

    // Get a payment by ID
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("retrieveById/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") int paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    // Get all payments
    @GetMapping("/retrieveAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Update a payment by ID
    @PutMapping("update/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") int paymentId, @RequestBody Payment updatedPayment) throws PaymentFailedException {
        Payment payment = paymentService.updatePayment(paymentId, updatedPayment);
        return ResponseEntity.ok(payment);
    }

    // Delete a payment by ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable("id") int paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok("Payment deleted successfully.");
    }
}
