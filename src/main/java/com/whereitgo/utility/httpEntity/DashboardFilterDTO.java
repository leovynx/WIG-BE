package com.whereitgo.utility.httpEntity;

import java.time.LocalDateTime;

import com.whereitgo.utility.enums.TransactionStatus;
import com.whereitgo.utility.enums.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardFilterDTO {

    private TransactionType transactionType;

    private TransactionStatus transactionStatus;

    private String bankName;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;
}