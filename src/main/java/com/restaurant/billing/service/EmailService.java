package com.restaurant.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;
	
	@Transactional
	public void sendBill(String toEmail, byte[] pdf) throws MailException {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			helper.setTo(toEmail);
			helper.setSubject("Your Restaurant Bill");
			helper.setText("Thank you for visiting. Please find your bill attached.");
			
			helper.addAttachment("bill.pdf", new ByteArrayResource(pdf));
			
			sender.send(message);
		} catch (MailException e) {
			throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Error preparing email: " + e.getMessage(), e);
		}
	}
}
