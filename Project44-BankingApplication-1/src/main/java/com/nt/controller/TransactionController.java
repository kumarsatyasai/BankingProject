package com.nt.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.service.BankStatement;

@RestController
@RequestMapping("/bank-api")
public class TransactionController {
	
	@Autowired
	private BankStatement bankStatement;
	

    @GetMapping("/bankStatement")
    public void getTransactions(
            @RequestParam String accountNumber,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
         bankStatement.generateStatement(accountNumber, startDate, endDate);
         
    }
    

}
