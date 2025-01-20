package com.capstone.qwikpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.capstone.qwikpay.exceptions.BillNotFoundException;
import com.capstone.qwikpay.services.BillService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
public class BillController {

    @Autowired
    private BillService billService;

    // Create new bill
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.ok(createdBill);
    }

    // Get all bills
    @GetMapping("/retrievAll")
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    // Get bills by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Integer userId) {
        List<Bill> bills = billService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }

    // Get bill by billId
    @GetMapping("retrieveBillById/{billId}")
    public ResponseEntity<Bill> getBillByBillId(@PathVariable Integer billId) throws BillNotFoundException {
        Bill bill = billService.getBillById(billId);
        return ResponseEntity.ok(bill);
    }

    // Get bills by status
    @GetMapping("/{status}")
    public ResponseEntity<List<Bill>> getBillsByStatus(@PathVariable String status) {
        List<Bill> bills = billService.getBillsByStatus(status);
        return ResponseEntity.ok(bills);
    }

    // Update bill by billId
    @PutMapping("update/{billId}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer billId, @RequestBody Bill updatedBill) throws BillNotFoundException {
        Bill bill = billService.updateBillById(billId, updatedBill);
        return ResponseEntity.ok(bill);
    }

    // Delete bill by billId
    @DeleteMapping("delete/{billId}")
    public ResponseEntity<Void> deleteBillById(@PathVariable Integer billId) throws BillNotFoundException {
        billService.deleteBillById(billId);
        return ResponseEntity.noContent().build();
    }
}
