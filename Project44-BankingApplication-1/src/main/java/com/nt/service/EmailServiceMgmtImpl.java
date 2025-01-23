package com.nt.service;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nt.dto.EmailDetails;

@Service
public class EmailServiceMgmtImpl implements IEmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String senderEmail;
	
	
	@Override
	public void sendEmailAlert(EmailDetails emailDetails){
		
		try
		{
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(senderEmail);
			simpleMailMessage.setTo(emailDetails.getRecipient());
			simpleMailMessage.setText(emailDetails.getMessageBody());
			simpleMailMessage.setSubject(emailDetails.getSubject());
			javaMailSender.send(simpleMailMessage);
			System.out.println("Mail Sent Successfully.");
		}
		catch(MailException e)
		{
			throw new RuntimeException(e);
		}

		
	}

}
