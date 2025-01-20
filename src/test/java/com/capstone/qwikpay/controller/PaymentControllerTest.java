package com.capstone.qwikpay.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.services.PaymentService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private BillRepository billRepository;

    public PaymentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() throws PaymentFailedException {
        Payment payment = new Payment();
        payment.setBillId(1);
        Bill bill = new Bill();
        when(billRepository.findById(1)).thenReturn(Optional.of(bill));
        when(paymentService.processPayment(payment)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.processPayment(payment);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(payment, response.getBody());
    }

    @Test
    void testProcessPayment_BillNotFound() {
        Payment payment = new Payment();
        payment.setBillId(1);

        when(billRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PaymentFailedException.class, () -> {
            paymentController.processPayment(payment);
        });

        assertEquals("Bill not found with ID: 1", exception.getMessage());
    }

    @Test
    void testGetPaymentByStatus_Found() {
        String status = "PAID";
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());

        when(paymentService.getPaymentByStatus(status)).thenReturn(payments);

        ResponseEntity<?> response = paymentController.getPaymentByStatus(status);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payments, response.getBody());
    }

    @Test
    void testGetPaymentByStatus_NotFound() {
        String status = "PENDING";

        when(paymentService.getPaymentByStatus(status)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = paymentController.getPaymentByStatus(status);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No payments found with status: PENDING", response.getBody());
    }

    @Test
    void testGetPaymentById() {
        int paymentId = 1;
        Payment payment = new Payment();
        when(paymentService.getPaymentById(paymentId)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.getPaymentById(paymentId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payment, response.getBody());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());

        when(paymentService.getAllPayments()).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payments, response.getBody());
    }

    @Test
    void testUpdatePayment_Success() throws PaymentFailedException {
        int paymentId = 1;
        Payment updatedPayment = new Payment();
        Payment payment = new Payment();
        when(paymentService.updatePayment(paymentId, updatedPayment)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.updatePayment(paymentId, updatedPayment);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payment, response.getBody());
    }

    @Test
    void testUpdatePayment_Failure() throws PaymentFailedException {
        int paymentId = 1;
        Payment updatedPayment = new Payment();

        when(paymentService.updatePayment(paymentId, updatedPayment)).thenThrow(new PaymentFailedException("Update failed"));

        Exception exception = assertThrows(PaymentFailedException.class, () -> {
            paymentController.updatePayment(paymentId, updatedPayment);
        });

        assertEquals("Update failed", exception.getMessage());
    }

    @Test
    void testDeletePayment_Success() {
        int paymentId = 1;

        doNothing().when(paymentService).deletePayment(paymentId);

        ResponseEntity<String> response = paymentController.deletePayment(paymentId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Payment deleted successfully.", response.getBody());
    }

    @Test
    void testDeletePayment_Failure() {
        int paymentId = 1;

        doThrow(new RuntimeException("Delete failed")).when(paymentService).deletePayment(paymentId);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentController.deletePayment(paymentId);
        });

        assertEquals("Delete failed", exception.getMessage());
    }
}
