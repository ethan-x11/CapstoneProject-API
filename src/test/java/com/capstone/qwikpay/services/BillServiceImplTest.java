package com.capstone.qwikpay.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.capstone.qwikpay.exceptions.BillNotFoundException;
import com.capstone.qwikpay.repositories.BillRepository;

class BillServiceImplTest {

    @InjectMocks
    private BillServiceImpl billService;

    @Mock
    private BillRepository billRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBill() {
        Bill bill = new Bill();
        bill.setAmount(100);
        bill.setDescription("Test Bill");

        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill createdBill = billService.createBill(bill);

        assertNotNull(createdBill);
        assertEquals(100, createdBill.getAmount());
        assertEquals("Test Bill", createdBill.getDescription());
        verify(billRepository, times(1)).save(bill);
    }

    @Test
    void testGetAllBills() {
        List<Bill> bills = Arrays.asList(new Bill(), new Bill());
        when(billRepository.findAll()).thenReturn(bills);

        List<Bill> result = billService.getAllBills();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(billRepository, times(1)).findAll();
    }

    @Test
    void testGetBillsByUserId() {
        Integer userId = 1;
        List<Bill> bills = Arrays.asList(new Bill(), new Bill());
        when(billRepository.findByUserId(userId)).thenReturn(bills);

        List<Bill> result = billService.getBillsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(billRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetBillById_Success() throws BillNotFoundException {
        Integer billId = 1;
        Bill bill = new Bill();
        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));

        Bill result = billService.getBillById(billId);

        assertNotNull(result);
        verify(billRepository, times(1)).findById(billId);
    }

    @Test
    void testGetBillById_NotFound() {
        Integer billId = 1;
        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.getBillById(billId));
        verify(billRepository, times(1)).findById(billId);
    }

    @Test
    void testGetBillsByStatus() {
        String status = "PAID";
        List<Bill> bills = Arrays.asList(new Bill(), new Bill());
        when(billRepository.findByBillStatus(status)).thenReturn(bills);

        List<Bill> result = billService.getBillsByStatus(status);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(billRepository, times(1)).findByBillStatus(status);
    }

    @Test
    void testUpdateBillById_Success() throws BillNotFoundException {
        Integer billId = 1;
        Bill existingBill = new Bill();
        existingBill.setAmount(100);
        Bill updatedBill = new Bill();
        updatedBill.setAmount(200);

        when(billRepository.findById(billId)).thenReturn(Optional.of(existingBill));
        when(billRepository.save(existingBill)).thenReturn(existingBill);

        Bill result = billService.updateBillById(billId, updatedBill);

        assertNotNull(result);
        assertEquals(200, result.getAmount());
        verify(billRepository, times(1)).findById(billId);
        verify(billRepository, times(1)).save(existingBill);
    }

    @Test
    void testUpdateBillById_NotFound() {
        Integer billId = 1;
        Bill updatedBill = new Bill();
        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.updateBillById(billId, updatedBill));
        verify(billRepository, times(1)).findById(billId);
    }

    @Test
    void testDeleteBillById_Success() throws BillNotFoundException {
        Integer billId = 1;
        Bill bill = new Bill();
        when(billRepository.findById(billId)).thenReturn(Optional.of(bill));

        billService.deleteBillById(billId);

        verify(billRepository, times(1)).findById(billId);
        verify(billRepository, times(1)).delete(bill);
    }

    @Test
    void testDeleteBillById_NotFound() {
        Integer billId = 1;
        when(billRepository.findById(billId)).thenReturn(Optional.empty());

        assertThrows(BillNotFoundException.class, () -> billService.deleteBillById(billId));
        verify(billRepository, times(1)).findById(billId);
    }
}
