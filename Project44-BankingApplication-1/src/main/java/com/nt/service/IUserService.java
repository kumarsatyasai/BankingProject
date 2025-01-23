package com.nt.service;

import com.nt.dto.BankResponse;
import com.nt.dto.CreditDebitRequest;
import com.nt.dto.EnquiryRequest;
import com.nt.dto.TransferRequest;
import com.nt.dto.UserRequest;

public interface IUserService {
	
	public BankResponse createAccount(UserRequest userRequest);
	
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	
	public String nameEnquiry(EnquiryRequest enquiryRequest);
	
	public BankResponse creditAccount(CreditDebitRequest credetDebitRequest);
	
	public BankResponse debitAccount(CreditDebitRequest credetDebitRequest);
	
	public BankResponse transfer(TransferRequest transferRequest);

}
