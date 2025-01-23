package com.nt.utils;

import java.time.LocalDate;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE = "001";
	
	public static final String ACCOUNT_EXISTS_MESSAGE = "This User Have Already An Account!";
	
	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	
	public static final String ACCOUNT_CREATION_MESSAGE = "Account Created Successfully";
	
	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
	
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "No Such Account Exists.";
	
	public static final String AMOUTN_CREDIT_SUCCESS_CODE = "004";
	
	public static final String AMOUTN_CREDIT_SUCCESS_MESSAGE = "Credit Successful";
	
	public static final String  DEBIT_AMOUNT_EXCEEDS_BALANCE_CODE = "005";
	
	public static final String  DEBIT_AMOUNT_EXCEEDS_BALANCE_MESSAGE = "Insufficient funds.";
	
    public static final String AMOUTN_DEBIT_SUCCESS_CODE = "006";
	
	public static final String AMOUTN_DEBIT_SUCCESS_MESSAGE = "Debit Successful";
	
	public static final String AMOUTN_TRANSFER_FAILURE_CODE = "007";
	
	public static final String AMOUTN_TRANSFER_FAILURE_MESSAGE = "Transfer Can't Be Done.";
	
	public static final String AMOUTN_TRANSFER_SUCCESS_CODE = "008";
	
	public static final String AMOUTN_TRANSFER_SUCCESS_MESSAGE = "Transfer Success.";

	
	
	public static String generateAccountNumber()
	{
		 String year = LocalDate.now().getYear()+"";
		 // Generates a number between 100000 and 999999
		 int randomNumber = (int) (Math.random() * 900000) + 100000; 
         return year+randomNumber;
	}

}
