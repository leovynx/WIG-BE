package com.whereitgo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.whereitgo.utility.enums.ParsingStatus;
import com.whereitgo.utility.enums.TransactionStatus;
import com.whereitgo.utility.enums.TransactionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many transactions belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many transactions can come from one SMS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_sms_id")
    private TransactionRawSMS rawSms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.SUCCESS;

    @Column(name = "notes")
    private String notes;

    private BigDecimal amount;

    private String currency = "INR";

    private String bankName;

    private String counterpartyName;

    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    private ParsingStatus parsingStatus = ParsingStatus.PARSED;

    private Boolean isUserConfirmed = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
