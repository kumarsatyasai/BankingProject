package com.nt.service;

import com.nt.dto.EmailDetails;

public interface IEmailService {
	
	public void sendEmailAlert(EmailDetails emailDetails);

}
