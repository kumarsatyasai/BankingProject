package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, String> {

}
