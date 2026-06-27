package com.whereitgo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_raw_sms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRawSMS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String sender;

    @Column(name = "sms_text", nullable = false, columnDefinition = "TEXT")
    private String smsText;

    @Column(name = "received_at")
    private LocalDateTime receivedAt = LocalDateTime.now();

    @Column(name = "sms_hash", unique = true, length = 64)
    private String smsHash;

    @Column(name = "processed")
    private Boolean processed = false;
}
