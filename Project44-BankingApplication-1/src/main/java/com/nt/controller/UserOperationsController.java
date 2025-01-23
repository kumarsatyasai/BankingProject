package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.BankResponse;
import com.nt.dto.CreditDebitRequest;
import com.nt.dto.EnquiryRequest;
import com.nt.dto.TransferRequest;
import com.nt.dto.UserRequest;
import com.nt.service.IUserService;

@RestController
@RequestMapping("/user-api")
public class UserOperationsController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/saveUser")
	public ResponseEntity<BankResponse> createUserAccount(@RequestBody UserRequest userRequest)
	{
		BankResponse bankResponse = userService.createAccount(userRequest);
		return new ResponseEntity<BankResponse>(bankResponse,HttpStatus.CREATED);
		
	}

	@GetMapping("/balanceEnquiry")
	public ResponseEntity<BankResponse> userBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
	{
		BankResponse bankResponse = userService.balanceEnquiry(enquiryRequest);
		return new ResponseEntity<BankResponse>(bankResponse,HttpStatus.OK);
		
	}
	
	@GetMapping("/nameEnquiry")
	public ResponseEntity<String> userNameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
	{
		String nameEnquiryResponse = userService.nameEnquiry(enquiryRequest);
		return new ResponseEntity<String>(nameEnquiryResponse,HttpStatus.OK);
	}
	
	@PatchMapping("/amountCredit")
	public ResponseEntity<BankResponse> userCreditAmount(@RequestBody CreditDebitRequest creditDebitRequest)
	{
		BankResponse bankResponse = userService.creditAccount(creditDebitRequest);
		return new ResponseEntity<BankResponse>(bankResponse,HttpStatus.OK);
		
	}
	
	@PatchMapping("/amountDebit")
	public ResponseEntity<BankResponse> userDebitAmount(@RequestBody CreditDebitRequest creditDebitRequest)
	{
		BankResponse bankResponse = userService.debitAccount(creditDebitRequest);
		return new ResponseEntity<BankResponse>(bankResponse,HttpStatus.OK);
		
	}
	
	@PatchMapping("/amountTransfer")
	public ResponseEntity<BankResponse> amountTransfer(@RequestBody TransferRequest transferRequest)
	{
		BankResponse bankResponse = userService.transfer(transferRequest);
		return new ResponseEntity<BankResponse>(bankResponse,HttpStatus.OK);
		
	}
	

}
