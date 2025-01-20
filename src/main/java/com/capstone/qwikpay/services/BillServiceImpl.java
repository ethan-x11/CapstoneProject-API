package com.capstone.qwikpay.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.exceptions.BillNotFoundException;
import com.capstone.qwikpay.repositories.BillRepository;

import jakarta.transaction.Transactional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    // Create new bill
    @Override
    @Transactional
    public Bill createBill(Bill bill) {
        bill.setCreatedAt(LocalDateTime.now());
        return billRepository.save(bill);
    }

    // Retrieve all bills
    @Override
    public List<Bill> getAllBills() {
        return (List<Bill>) billRepository.findAll();
    }

    // Retrieve bills by User ID
    @Override
    public List<Bill> getBillsByUserId(Integer userId) {
        return billRepository.findByUserId(userId);
    }

    // Retrieve bill by Bill ID
    @Override
    public Bill getBillById(Integer billId) throws BillNotFoundException {
        Optional<Bill> bill = billRepository.findById(billId);
        return bill.orElseThrow(() -> new BillNotFoundException("Bill not found with id: " + billId));
    }

    // Retrieve bills by Status
    @Override
    public List<Bill> getBillsByStatus(String status) {
        return billRepository.findByBillStatus(status);
    }

    // Update bill by its ID
    @Override
    public Bill updateBillById(Integer billId, Bill updatedBill) throws BillNotFoundException {
        Bill bill = getBillById(billId);
//        bill.setUserId(updatedBill.getUserId());
        bill.setAmount(updatedBill.getAmount());
        bill.setBillStatus(updatedBill.getBillStatus());
        bill.setDescription(updatedBill.getDescription());
        bill.setDueDate(updatedBill.getDueDate());
//        bill.setPaymentDate(updatedBill.getPaymentDate());
        bill.setUpdatedAt(java.time.LocalDateTime.now());
        return billRepository.save(bill);
    }

    // Delete bill by its ID
    @Override
    public void deleteBillById(Integer billId) throws BillNotFoundException {
        Bill bill = getBillById(billId);
        billRepository.delete(bill);
    }
}
