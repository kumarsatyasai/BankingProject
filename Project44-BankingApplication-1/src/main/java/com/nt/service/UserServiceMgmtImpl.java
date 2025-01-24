package com.nt.service;



import java.math.BigDecimal;    
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nt.dto.AccountInfo;
import com.nt.dto.BankResponse;
import com.nt.dto.CreditDebitRequest;
import com.nt.dto.EmailDetails;
import com.nt.dto.EnquiryRequest;
import com.nt.dto.TransactionDto;
import com.nt.dto.TransferRequest;
import com.nt.dto.UserRequest;
import com.nt.entity.User;
import com.nt.repository.IUserRepository;
import com.nt.utils.AccountUtils;

@Service
public class UserServiceMgmtImpl implements IUserService {
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private ITransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		
		if(userRepository.existsByUserEmail(userRequest.getEmail()))
		{
			BankResponse response = BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
			return response;
		}

		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.email(userRequest.getEmail())
				.accountBalance(BigDecimal.ZERO)
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.build();
		User savedUser = userRepository.save(newUser);
		
		//Send Email Alert.
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(savedUser.getEmail())
				.subject("Welcome to SBI - Your Account is Successfully Created!\r\n"
						+ "\r\n"
						+ "")
				.messageBody("Dear "+savedUser.getFirstName()+",\r\n"
						+ "\r\n"
						+ "We are excited to welcome you to SBI! \r\n"
						+ "\r\n"
						+ "Your account has been successfully created, and we want to ensure you have all the information you need to get started.\r\n"
						+ "\r\n"
						+ "**Your Account Number : "+savedUser.getAccountNumber()+" \r\n"
						+ "Please keep this number safe, as you will need it for future transactions and inquiries.\r\n"
						+ "\r\n"
						+ "If you have any questions or need assistance, feel free to reach out to our support team.\r\n"
						+ "\r\n"
						+ "Thank you for choosing SBI!\r\n"
						+ "\r\n"
						+ "Best regards,  \r\n"
						+ "State Bank Of India Team  \r\n"
						+ "123-456-789")
				.build();
		emailService.sendEmailAlert(emailDetails);
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.build())
				.build();
		
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		
		boolean existsByAccountNumber = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if(!existsByAccountNumber)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(foundUser.getAccountBalance())
						.accountName(foundUser.getFirstName())
						.accountNumber(foundUser.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		
		boolean existsByAccountNumber = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if(!existsByAccountNumber)
		{
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
		}

		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		
		return foundUser.getFirstName()+" "+foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest credetDebitRequest) {


		boolean existsByAccountNumber = userRepository.existsByAccountNumber(credetDebitRequest.getAccountNumber());
		if(!existsByAccountNumber)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToCredit = userRepository.findByAccountNumber(credetDebitRequest.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(credetDebitRequest.getAmount()));
		User creditedUser = userRepository.save(userToCredit);
		
		//Transaction Details
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(creditedUser.getAccountNumber())
				.amount(credetDebitRequest.getAmount())
				.transactionType("CREDIT")
				.build();
		transactionService.saveTransaction(transactionDto);
		
		//Send Email Alert.
				EmailDetails emailDetails = EmailDetails.builder()
						.recipient(creditedUser.getEmail())
						.subject("Amount Credited to Your SBI Account\r\n"
								+ "\r\n"
								+ "")
						.messageBody("Dear "+creditedUser.getFirstName()+",\r\n"
								+ "\r\n"
								+ "We are pleased to inform you that an amount has been credited to your account.\r\n"
								+ "\r\n"
								+ "Transaction Details:\r\n"
								+ "\r\n"
								+ "Date: "+LocalDateTime.now()+"\r\n"
								+ "Amount Credited: ₹"+credetDebitRequest.getAmount()+"\r\n"
								+ "Account Number: XXX-XXX-XXXX-"+creditedUser.getAccountNumber()
								.substring(creditedUser.getAccountNumber().length()-4)+"\r\n"
								+ "Description: Salary Deposit\r\n"
								+ "Thank you for banking with us!\r\n"
								+ "\r\n"
								+ "Best Regards,\r\n"
								+ "State Bank of India\r\n"
								+ "\r\n"
								+ "")
						.build();
				emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.AMOUTN_CREDIT_SUCCESS_CODE)
				.responseMessage(AccountUtils.AMOUTN_CREDIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(creditedUser.getFirstName())
						.accountNumber(creditedUser.getAccountNumber())
						.accountBalance(creditedUser.getAccountBalance())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest credetDebitRequest) {
		boolean existsByAccountNumber = userRepository.existsByAccountNumber(credetDebitRequest.getAccountNumber());
		if(!existsByAccountNumber)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User userToDebit = userRepository.findByAccountNumber(credetDebitRequest.getAccountNumber());
		int accountBalance = userToDebit.getAccountBalance().intValue();
		int amount = credetDebitRequest.getAmount().intValue();
		if(accountBalance<amount)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.DEBIT_AMOUNT_EXCEEDS_BALANCE_CODE)
					.responseMessage(AccountUtils.DEBIT_AMOUNT_EXCEEDS_BALANCE_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountName(userToDebit.getFirstName())
							.accountNumber(userToDebit.getAccountNumber())
							.accountBalance(userToDebit.getAccountBalance())
							.build())
					.build();
		}
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(credetDebitRequest.getAmount()));
		User debitedUser = userRepository.save(userToDebit);
		
		//Transaction Details
				TransactionDto transactionDto = TransactionDto.builder()
						.accountNumber(debitedUser.getAccountNumber())
						.amount(credetDebitRequest.getAmount())
						.transactionType("Debit")
						.build();
				transactionService.saveTransaction(transactionDto);
				
		//Send Email Alert.
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(debitedUser.getEmail())
				.subject("Amount Debited from Your SBI Account\r\n"
						+ "\r\n"
						+ "")
				.messageBody("Dear "+debitedUser.getFirstName()+",\r\n"
						+ "\r\n"
						+ "We would like to inform you that an amount has been debited from your account. Please find the transaction details below:\r\n"
						+ "\r\n"
						+ "Transaction Details:\r\n"
						+ "\r\n"
						+ "Transaction Date: "+LocalDateTime.now()+"\r\n"
						+ "Amount Debited: ₹"+credetDebitRequest.getAmount()+"\r\n"
						+ "Account Number: XXX-XXX-"+debitedUser.getAccountNumber()
						.substring(debitedUser.getAccountNumber().length()-4)+"\r\n"
						+ "Transaction Description: ATM Withdrawal\r\n"
						+ "If you did not authorize this transaction or if you have any questions, please contact our customer service immediately.\r\n"
						+ "\r\n"
						+ "Customer Support:\r\n"
						+ "Phone: 1800 123 4567\r\n"
						+ "Email: support@sbi.co.in\r\n"
						+ "Website: www.sbi.co.in\r\n"
						+ "\r\n"
						+ "Thank you for banking with us!\r\n"
						+ "\r\n"
						+ "Best Regards,\r\n"
						+ "State Bank of India\r\n"
						+ "\r\n"
						+ "")
				.build();
		emailService.sendEmailAlert(emailDetails);

		
		return BankResponse.builder()
				.responseCode(AccountUtils.AMOUTN_DEBIT_SUCCESS_CODE)
				.responseMessage(AccountUtils.AMOUTN_DEBIT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(debitedUser.getFirstName())
						.accountNumber(debitedUser.getAccountNumber())
						.accountBalance(debitedUser.getAccountBalance())
						.build())
				.build();
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
		
		
		if(!userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber())||
				!userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber()))
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.AMOUTN_TRANSFER_FAILURE_CODE)
					.responseMessage(AccountUtils.AMOUTN_TRANSFER_FAILURE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User sourceUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
		User destinationUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		
		if(transferRequest.getAmount().intValue()>sourceUser.getAccountBalance().intValue())
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.AMOUTN_TRANSFER_FAILURE_CODE)
					.responseMessage(AccountUtils.AMOUTN_TRANSFER_FAILURE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferRequest.getAmount()));
		userRepository.save(sourceUser);
		
		//Transaction Details
				TransactionDto transactionDto = TransactionDto.builder()
						.accountNumber(sourceUser.getAccountNumber())
						.amount(transferRequest.getAmount())
						.transactionType("DEBIT")
						.build();
				transactionService.saveTransaction(transactionDto);
				
		//Send Email Alert.
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(sourceUser.getEmail())
				.subject("Successful Fund Transfer Notification.")
				.messageBody("Dear "+sourceUser.getFirstName()+",\r\n"
						+ "\r\n"
						+ "We are pleased to inform you that your fund transfer has been successfully completed.\r\n"
						+ "\r\n"
						+ "Details of the Transaction:\r\n"
						+ "\r\n"
						+ "Amount Transferred : ₹"+transferRequest.getAmount()+"\r\n"
						+ "Your Account : "+sourceUser.getAccountNumber()+"\r\n"
						+ "To Account : "+destinationUser.getAccountNumber()+"\r\n"
						+ "Date and Time of Transaction: "+LocalDateTime.now()+"\r\n"
						+ "\r\n"
						+ "If you have any questions or require further assistance, please contact our customer service.\r\n"
						+ "\r\n"
						+ "Thank you for banking with us.")
				.build();
		emailService.sendEmailAlert(emailDetails);

		destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
		userRepository.save(destinationUser);
		
		//Transaction Details
				TransactionDto transactionDto1 = TransactionDto.builder()
						.accountNumber(destinationUser.getAccountNumber())
						.amount(transferRequest.getAmount())
						.transactionType("CREDIT")
						.build();
				transactionService.saveTransaction(transactionDto1);
				
		//Send Email Alert.
		EmailDetails emailDetail = EmailDetails.builder()
				.recipient(destinationUser.getEmail())
				.subject("Fund Transfer Received Notification\r\n"
						+ "\r\n"
						+ "")
				.messageBody("Dear "+destinationUser.getFirstName()+",\r\n"
						+ "\r\n"
						+ "We are happy to inform you that a fund transfer has been successfully credited to your account.\r\n"
						+ "\r\n"
						+ "Details of the Transaction:\r\n"
						+ "\r\n"
						+ "Amount Credited : ₹"+transferRequest.getAmount()+"\r\n"
						+ "From Account : "+sourceUser.getAccountNumber()+"\r\n"
						+ "Your Account : "+destinationUser.getAccountNumber()+"\r\n"
					    + "\r\n"
						+ "Date and Time of Credit: "+LocalDateTime.now()+"\r\n"
					    + "\r\n"
						+ "If you have any questions or need further assistance, please reach out to our customer service.\r\n"
						+ "\r\n"
						+ "Thank you for choosing SBI for your banking needs.")
				.build();
		emailService.sendEmailAlert(emailDetail);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.AMOUTN_TRANSFER_SUCCESS_CODE)
				.responseMessage(AccountUtils.AMOUTN_TRANSFER_SUCCESS_MESSAGE)
				.accountInfo(null)
				.build();
	}

}




