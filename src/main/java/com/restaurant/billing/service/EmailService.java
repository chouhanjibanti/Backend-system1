package com.restaurant.billing.service;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;
	
	public void sendBill(String toEmail, byte[] pdf) {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			
			helper.setTo(toEmail);
			helper.setSubject("Your Restaurant Bill");
			helper.setText("Thank You for visiting. Bill attached.");
			
			helper .addAttachment("bill.pdf", new ByteArrayResource(pdf));
			
			sender.send(message);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Email failed but order completed.");

		}
	}
}
