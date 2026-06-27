package com.whereitgo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whereitgo.model.TransactionMetaData;

@Repository
public interface TransactionMetaDataRepo  extends JpaRepository<TransactionMetaData, String> {

}
