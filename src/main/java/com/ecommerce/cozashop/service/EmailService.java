package com.ecommerce.cozashop.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.config.MailSender;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private MailSender mailConfig;

    public void sendSimpleMessage(String to, String subject, String text) {
 

    	 final String username = "admin@iantila.com";
         final String password = "Madagascar2025!";

         Properties prop = new Properties();
 		prop.put("mail.smtp.host", "smtp.ionos.fr");
         prop.put("mail.smtp.port", "465");
         prop.put("mail.smtp.auth", "true");
         prop.put("mail.smtp.socketFactory.port", "465");
         prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         
         Session session = Session.getInstance(prop,
                 new jakarta.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() {
                         return new PasswordAuthentication(username, password);
                     }
                 });
         
         try {
        	 
             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("admin@iantila.com"));
             message.setRecipients(
                     Message.RecipientType.TO,
                     InternetAddress.parse(to)
             );
             message.setSubject(subject);
             message.setContent(text, "text/html; charset=utf-8");
             Transport.send(message);

         } catch (MessagingException e) {
        	 //TODO Log
             System.out.println(e.getMessage());
         }
    	
    }
}
