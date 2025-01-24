package com.nt.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.entity.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, String> {
	
    @Query("FROM Transaction t WHERE t.accountNumber = :accountNumber AND DATE(t.createdAt) BETWEEN :startDate AND :endDate")
    public List<Transaction> findTransactionsByAccountNumberAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}


