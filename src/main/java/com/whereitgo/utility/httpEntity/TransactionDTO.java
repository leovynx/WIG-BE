package com.whereitgo.utility.httpEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.whereitgo.utility.enums.TransactionStatus;
import com.whereitgo.utility.enums.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    private String rawMessage;
    private Long transactionId;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;

    private BigDecimal amount;
    private String currency;

    private String bankName;
    private String counterpartyName;

    private LocalDateTime transactionTime;

    private Boolean isUserConfirmed;

    private String transactionNotes;
}