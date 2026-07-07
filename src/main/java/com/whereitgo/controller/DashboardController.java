package com.whereitgo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.whereitgo.service.DashboardService;
import com.whereitgo.utility.httpEntity.DashboardFilterDTO;
import com.whereitgo.utility.httpEntity.TransactionDTO;
import com.whereitgo.utility.httpEntity.WIGResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PostMapping("/transactions")
    public WIGResponse<List<TransactionDTO>> getTransactions(
            @RequestBody DashboardFilterDTO filter) {

        List<TransactionDTO> transactions =
                dashboardService.getAllTransactions(filter);

        return WIGResponse.success(
                transactions,
                200,
                "Transactions fetched successfully");
    }
}