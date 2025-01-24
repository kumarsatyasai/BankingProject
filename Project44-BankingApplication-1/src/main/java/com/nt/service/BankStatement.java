package com.nt.service;



import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nt.entity.Transaction;
import com.nt.entity.User;
import com.nt.repository.ITransactionRepository;
import com.nt.repository.IUserRepository;

import jakarta.mail.MessagingException;

@Component
public class BankStatement {
	
	@Autowired
	private ITransactionRepository transRepo;
	
	
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private EmailService emailService;
	
	
	

    public void generateStatement(String accountNumber, LocalDate startDate, LocalDate endDate) {
    	
         List<Transaction> transactions = transRepo.findTransactionsByAccountNumberAndDateRange(accountNumber, startDate, endDate);

         User user = userRepo.findByAccountNumber(accountNumber) ;
         String email = user.getEmail();
         // Step 2: Generate PDF
         ByteArrayOutputStream pdfOutputStream = pdfService.generateTransactionPdf(transactions, accountNumber);

         // Step 3: Send email with PDF attachment
         try {
             emailService.sendEmailWithAttachment(email, "Your Bank Statement", "Please find attached your bank statement.", pdfOutputStream, "bank_statement_" + accountNumber + ".pdf");
         } catch (MessagingException e) {
             e.printStackTrace();
         }
    }
    

}
