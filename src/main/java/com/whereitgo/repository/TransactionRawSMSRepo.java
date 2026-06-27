package com.whereitgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whereitgo.model.TransactionRawSMS;

@Repository
public interface TransactionRawSMSRepo extends JpaRepository<TransactionRawSMS, String> {

}
