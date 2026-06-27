package com.whereitgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whereitgo.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {

}
