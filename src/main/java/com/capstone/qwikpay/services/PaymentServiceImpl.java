package com.capstone.qwikpay.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.repositories.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

   
    @Transactional
    @Override
    public Payment processPayment(Payment payment) throws PaymentFailedException {
        // Ensure bill_id is provided
        if (payment.getBill() == null || payment.getBill().getBillId() == null) {
            throw new PaymentFailedException("Bill information is missing or Bill ID is null for the payment.");
        }

        // Fetch the Bill using the provided Bill ID
        Bill bill = billRepository.findById(payment.getBill().getBillId())
                .orElseThrow(() -> new PaymentFailedException("Bill not found with ID: " + payment.getBill().getBillId()));

        // Associate the Bill with the Payment
        payment.setBill(bill);

        // Update payment status
        payment.setPaymentStatus("Paid");
        payment.setPaymentDate(LocalDateTime.now());

        // Update the bill status based on payment status
        if ("Paid".equals(payment.getPaymentStatus())) {
            bill.setBillStatus("Paid");
        } else {
            bill.setBillStatus("Pending");
        }

        // Save the updated bill and payment
        billRepository.save(bill);
        return paymentRepository.save(payment);
    }



    @Override
    public boolean validatePayment(Integer paymentId) throws PaymentFailedException {
        return paymentRepository.findById(paymentId)
                .map(payment -> "PAID".equalsIgnoreCase(payment.getPaymentStatus()))
                .orElseThrow(() -> new PaymentFailedException("Payment not found for ID: " + paymentId));
    }

    @Override
    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentId));
    }

    @Override
    public List<Payment> getAllPayments() {
        return (List<Payment>) paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment updatePayment(Integer paymentId, Payment updatedPayment) throws PaymentFailedException {
        // Retrieve the existing payment
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentId));

        // Check if the payment is already completed and prevent modification
        if ("COMPLETED".equalsIgnoreCase(existingPayment.getPaymentStatus())) {
            throw new PaymentFailedException("Cannot update a completed payment.");
        }

        // Update the payment details
        existingPayment.setAmount(updatedPayment.getAmount());
        existingPayment.setPaymentStatus(updatedPayment.getPaymentStatus());
        existingPayment.setPaymentDate(LocalDateTime.now());

        // Update the associated bill status based on the payment status
        Bill bill = existingPayment.getBill();
        if (bill != null) {
            if ("PAID".equalsIgnoreCase(updatedPayment.getPaymentStatus())) {
                bill.setBillStatus("PAID");  // Set the bill status to Paid when payment is completed
            } else {
                bill.setBillStatus("PARTIALLY PAID");  // Set to partially paid if not fully completed
            }
            bill.setUpdatedAt(LocalDateTime.now()); // Update the updated timestamp
            billRepository.save(bill);  // Save the updated bill
        }

        // Save the updated payment
        return paymentRepository.save(existingPayment);
    }



    @Override
    public void deletePayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentId));

        Bill bill = payment.getBill();
        if (bill != null) {
            bill.setPayment(null);
            bill.setBillStatus("Unpaid");
            billRepository.save(bill);
        }

        paymentRepository.deleteById(paymentId);
    }

    private void updateBillAfterPayment(Bill bill, Payment payment) {
        bill.setPayment(payment);
        bill.setBillStatus(payment.getAmount() >= bill.getAmount() ? "PAID" : "PARTIALLY PAID");
        bill.setUpdatedAt(LocalDateTime.now());
        billRepository.save(bill);
    }



	@Override
	public List<Payment> getPaymentsByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
    public List<Payment> getPaymentByStatus(String paymentStatus) {
        if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment status cannot be null or empty");
        }
        return paymentRepository.findByPaymentStatus(paymentStatus);
    }



//    @Override
//    public List<Payment> getPaymentsByUserId(int userId) {
//        // Assuming you have a user relationship with payments, fetch payments by userId
//        return paymentRepository.findByUserId(userId);
//    }
}
