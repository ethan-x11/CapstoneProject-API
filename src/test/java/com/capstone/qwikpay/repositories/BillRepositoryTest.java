package com.capstone.qwikpay.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.entities.UserEntity;

public class BillRepositoryTest {

    @Mock
    private BillRepository billRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUserId() {
        // Arrange: Mock repository behavior
        UserEntity user1 = new UserEntity();
        user1.setId(1);

        Bill bill1 = new Bill();
        bill1.setBillId(101);
        bill1.setUser(user1);

        Bill bill2 = new Bill();
        bill2.setBillId(102);
        bill2.setUser(user1);

        when(billRepository.findByUserId(1)).thenReturn(Arrays.asList(bill1, bill2));

        // Act
        List<Bill> result = billRepository.findByUserId(1);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(bill -> bill.getUser().getId().equals(1));

        // Verify interaction
        verify(billRepository, times(1)).findByUserId(1);
    }

    @Test
    public void testFindByBillStatus() {
        // Arrange: Mock repository behavior
        Bill bill1 = new Bill();
        bill1.setBillId(101);
        bill1.setBillStatus("Paid");

        Bill bill2 = new Bill();
        bill2.setBillId(102);
        bill2.setBillStatus("Paid");

        when(billRepository.findByBillStatus("Paid")).thenReturn(Arrays.asList(bill1, bill2));

        // Acts
        List<Bill> result = billRepository.findByBillStatus("Paid");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(bill -> bill.getBillStatus().equals("Paid"));

        // Verify interaction
        verify(billRepository, times(1)).findByBillStatus("Paid");
    }
}
