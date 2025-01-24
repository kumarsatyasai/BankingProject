package com.nt.service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;

import com.nt.dto.TransactionDto;
import com.nt.entity.Transaction;
import com.nt.repository.ITransactionRepository;

@Component
public class TransactionServiceMgmtImpl implements ITransactionService {

	@Autowired
	private ITransactionRepository transRepo;

	@Override
	public void saveTransaction(TransactionDto transactionDto) {
 
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.amount(transactionDto.getAmount())
				.status("Success")
				.build();
		transRepo.save(transaction);
		
		
	}
	
	

}
