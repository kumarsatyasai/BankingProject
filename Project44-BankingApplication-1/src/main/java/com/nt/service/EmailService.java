package com.nt.service;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	 @Autowired
	    private JavaMailSender emailSender;

	    public void sendEmailWithAttachment(String to, String subject, String text, ByteArrayOutputStream attachment, String attachmentName) throws MessagingException {
	        MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(text);
	        
	        // Attach the PDF
	        ByteArrayResource resource = new ByteArrayResource(attachment.toByteArray());
	        helper.addAttachment(attachmentName, resource);

	        emailSender.send(message);
	    }

}
