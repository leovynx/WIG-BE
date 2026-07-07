package com.whereitgo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whereitgo.service.TransactionService;
import com.whereitgo.utility.httpEntity.TransactionDTO;
import com.whereitgo.utility.httpEntity.WIGResponse;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    String sampleRawMessage = "ICICI Bank Acct XX528 debited for Rs 12.00 on 24-Jun-26; JUICE POPS credited. UPI:654113534722. Call 18002662 for dispute. SMS BLOCK 528 to 9215676766.";

    @PostMapping("/process-message")
    public WIGResponse<TransactionDTO> processRawTransactionMessage(@RequestBody TransactionDTO rawMessage) {
        TransactionDTO transaction = transactionService.processTransactionRawMessage(rawMessage);
        return WIGResponse.success(
                transaction,
                200,
                "Transaction parsered successfully");
    }

    @PatchMapping("/save-message")
    public WIGResponse<String> saveTransactionMessage(@RequestBody TransactionDTO message) {
        String response = transactionService.saveTransactionMessage(message);
        return WIGResponse.success(
                response,
                200,
                "Success");
    }

    @DeleteMapping("/delete-message")
    public WIGResponse<String> deleteTransactionMessage(@RequestBody List<Long> transactionIds) {
        String response = transactionService.deleteTransactionMessage(transactionIds);
        return WIGResponse.success(
                response,
                200,
                "Success");
    }

}
