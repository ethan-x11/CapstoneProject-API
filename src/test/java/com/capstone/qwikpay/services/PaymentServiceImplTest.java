package com.capstone.qwikpay.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;
import com.capstone.qwikpay.exceptions.PaymentFailedException;
import com.capstone.qwikpay.repositories.BillRepository;
import com.capstone.qwikpay.repositories.PaymentRepository;

class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BillRepository billRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() throws PaymentFailedException {
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setAmount(100);
        bill.setBillStatus("Pending");

        Payment payment = new Payment();
        payment.setAmount(100.0);
        payment.setBill(bill);

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment processedPayment = paymentService.processPayment(payment);

        assertNotNull(processedPayment);
        assertEquals("Paid", processedPayment.getPaymentStatus());
        assertEquals("Paid", bill.getBillStatus());
        verify(billRepository, times(1)).save(bill);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testProcessPayment_BillNotFound() {
        Payment payment = new Payment();
        Bill bill = new Bill();
        bill.setBillId(1);
        payment.setBill(bill);

        when(billRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PaymentFailedException.class, () -> paymentService.processPayment(payment));
        verify(billRepository, times(1)).findById(1);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testValidatePayment_Success() throws PaymentFailedException {
        Payment payment = new Payment();
        payment.setPmtId(1);
        payment.setPaymentStatus("Paid");

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));

        boolean isValid = paymentService.validatePayment(1);

        assertTrue(isValid);
        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    void testValidatePayment_NotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PaymentFailedException.class, () -> paymentService.validatePayment(1));
        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    void testGetPaymentById_Success() {
        Payment payment = new Payment();
        payment.setPmtId(1);

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById(1);

        assertNotNull(result);
        assertEquals(1, result.getPmtId());
        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(1));
        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testUpdatePayment_Success() throws PaymentFailedException {
        Payment existingPayment = new Payment();
        existingPayment.setPmtId(1);
        existingPayment.setPaymentStatus("Pending");

        Payment updatedPayment = new Payment();
        updatedPayment.setPaymentStatus("PAID"); // Set to match the actual implementation

        Bill bill = new Bill();
        bill.setBillStatus("Pending");
        existingPayment.setBill(bill);

        // Mock repository behavior
        when(paymentRepository.findById(1)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        // Call the service method
        Payment result = paymentService.updatePayment(1, updatedPayment);

        // Assertions
        assertNotNull(result);
        assertEquals("PAID", result.getPaymentStatus()); // Match expected value
        assertEquals("PAID", bill.getBillStatus());      // Match expected value
        verify(billRepository, times(1)).save(bill);
        verify(paymentRepository, times(1)).save(existingPayment);
    }


    @Test
    void testUpdatePayment_CompletedPayment() {
        Payment existingPayment = new Payment();
        existingPayment.setPmtId(1);
        existingPayment.setPaymentStatus("COMPLETED");

        Payment updatedPayment = new Payment();
        updatedPayment.setPaymentStatus("Paid");

        when(paymentRepository.findById(1)).thenReturn(Optional.of(existingPayment));

        assertThrows(PaymentFailedException.class, () -> paymentService.updatePayment(1, updatedPayment));
        verify(paymentRepository, times(1)).findById(1);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testDeletePayment_Success() {
        Payment payment = new Payment();
        payment.setPmtId(1);

        Bill bill = new Bill();
        bill.setBillStatus("Paid");
        payment.setBill(bill);

        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));

        paymentService.deletePayment(1);

        assertEquals("Unpaid", bill.getBillStatus());
        verify(billRepository, times(1)).save(bill);
        verify(paymentRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePayment_NotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.deletePayment(1));
        verify(paymentRepository, times(1)).findById(1);
    }
}
