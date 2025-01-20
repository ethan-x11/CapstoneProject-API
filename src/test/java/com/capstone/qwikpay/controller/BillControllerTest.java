package com.capstone.qwikpay.controller;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.services.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

class BillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
    }

    // 1. Test Create Bill
    @Test
    void testCreateBill_Success() throws Exception {
        Bill mockBill = new Bill();
        mockBill.setAmount(100);
        mockBill.setDescription("New bill");

        when(billService.createBill(any(Bill.class))).thenReturn(mockBill);

        mockMvc.perform(post("/api/bills/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 100.0, \"description\": \"New bill\"}"))
                .andExpect(status().isOk())  // Changed from CREATED to OK
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.description").value("New bill"));
    }

    // 2. Test Get Bill by ID
    @Test
    void testGetBillById_Success() throws Exception {
        Bill mockBill = new Bill();
        mockBill.setAmount(100);
        mockBill.setDescription("Test bill");

        when(billService.getBillById(1)).thenReturn(mockBill);

        mockMvc.perform(get("/api/bills/retrieveBillById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.description").value("Test bill"));
    }

    // 3. Test Update Bill by ID
    @Test
    void testUpdateBillById_Success() throws Exception {
        Bill mockBill = new Bill();
        mockBill.setAmount(150);
        mockBill.setDescription("Updated bill");

        when(billService.updateBillById(eq(1), any(Bill.class))).thenReturn(mockBill);

        mockMvc.perform(put("/api/bills/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 150.0, \"description\": \"Updated bill\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.description").value("Updated bill"));
    }

    // 4. Test Delete Bill by ID
    @Test
    void testDeleteBill_Success() throws Exception {
        doNothing().when(billService).deleteBillById(1);

        mockMvc.perform(delete("/api/bills/delete/1"))
                .andExpect(status().isNoContent());

        verify(billService, times(1)).deleteBillById(1);
    }

    // 5. Test Get Bills by User ID
    @Test
    void testGetBillsByUserId_Success() throws Exception {
        Bill mockBill1 = new Bill();
        mockBill1.setAmount(100);
        mockBill1.setDescription("User bill 1");

        Bill mockBill2 = new Bill();
        mockBill2.setAmount(200);
        mockBill2.setDescription("User bill 2");

        List<Bill> mockBills = Arrays.asList(mockBill1, mockBill2);

        when(billService.getBillsByUserId(1)).thenReturn(mockBills);

        mockMvc.perform(get("/api/bills/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].description").value("User bill 1"))
                .andExpect(jsonPath("$[1].amount").value(200.0))
                .andExpect(jsonPath("$[1].description").value("User bill 2"));
    }
    
    // 6. Test Get Bills by Status
    @Test
    void testGetBillsByStatus_Success() throws Exception {
        Bill mockBill1 = new Bill();
        mockBill1.setAmount(100);
        mockBill1.setDescription("Paid bill 1");
        mockBill1.setBillStatus("paid");

        Bill mockBill2 = new Bill();
        mockBill2.setAmount(200);
        mockBill2.setDescription("Paid bill 2");
        mockBill2.setBillStatus("pending");

        List<Bill> mockBills = Arrays.asList(mockBill1, mockBill2);

        when(billService.getBillsByStatus("paid")).thenReturn(mockBills);
        when(billService.getBillsByStatus("pending")).thenReturn(mockBills);

        mockMvc.perform(get("/api/bills/paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].description").value("Paid bill 1"))
                .andExpect(jsonPath("$[0].billStatus").value("paid"));

        mockMvc.perform(get("/api/bills/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].amount").value(200.0))
                .andExpect(jsonPath("$[1].description").value("Paid bill 2"))
                .andExpect(jsonPath("$[1].billStatus").value("pending"));
    }

}
