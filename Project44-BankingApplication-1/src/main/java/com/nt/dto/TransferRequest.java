package com.nt.dto;

import java.math.BigDecimal; 

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
	
	private String sourceAccountNumber;
	
	private String destinationAccountNumber;
	
	private BigDecimal amount;

}
