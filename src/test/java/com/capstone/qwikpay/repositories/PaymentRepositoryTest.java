package com.capstone.qwikpay.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.Payment;

public class PaymentRepositoryTest {

    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testFindByPaymentStatus() {
        // Arrange
        Payment payment1 = new Payment();
        payment1.setPmtId(1);
        payment1.setAmount(500.0);
        payment1.setPaymentStatus("Completed");
        payment1.setPaymentDate(LocalDateTime.now());

        Payment payment2 = new Payment();
        payment2.setPmtId(2);
        payment2.setAmount(200.0);
        payment2.setPaymentStatus("Completed");
        payment2.setPaymentDate(LocalDateTime.now());

        when(paymentRepository.findByPaymentStatus("Completed"))
                .thenReturn(Arrays.asList(payment1, payment2));

        // Act
        List<Payment> completedPayments = paymentRepository.findByPaymentStatus("Completed");

        // Assert
        assertThat(completedPayments).hasSize(2);
        assertThat(completedPayments).allMatch(p -> p.getPaymentStatus().equals("Completed"));

        // Verify that the method was called once
        verify(paymentRepository, times(1)).findByPaymentStatus("Completed");
    }

    @Test
    public void testGetTotalPaidAmountForBill() {
        // Arrange
        Bill bill = new Bill();
        bill.setBillId(1);

        when(paymentRepository.getTotalPaidAmountForBill(1)).thenReturn(700.0);

        // Act
        Double totalPaid = paymentRepository.getTotalPaidAmountForBill(1);

        // Assert
        assertThat(totalPaid).isNotNull();
        assertThat(totalPaid).isEqualTo(700.0);

        // Verify that the method was called once
        verify(paymentRepository, times(1)).getTotalPaidAmountForBill(1);
    }
}
