/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.action;

import com.trouble.entities.Songs;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Max
 */
@Named
@RequestScoped
public class SendEmailReceipt {

    public SendEmailReceipt() {
    }

    /**
     * Sending an email to the client with their receipt
     * @param email
     * @param songs
     * @param total 
     */
    public void sendMail(String email, ArrayList<Songs> songs, Double total) {
         final String username = "receipt.trouble.media@gmail.com";
		final String password = "Tr0ubleMed1a";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Receipt");
                        StringBuilder sb = new StringBuilder();
                        for(Songs s: songs){
                            sb.append(s.toString()).append("\n");
                            sb.append("\n");
                            
                        }
                        sb.append("\n\t"+"TOTAL: $").append(total);
			message.setText(sb.toString());

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}