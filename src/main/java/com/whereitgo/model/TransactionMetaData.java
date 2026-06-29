package com.whereitgo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.whereitgo.utility.enums.LocationSource;

@Entity
@Table(name = "transaction_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(columnDefinition = "TEXT")
    private String userNotes;

    private String category;

    private Double latitude;

    private Double longitude;

    private Double locationAccuracy;

    @Enumerated(EnumType.STRING)
    private LocationSource locationSource;

    private LocalDateTime createdAt = LocalDateTime.now();
} 

