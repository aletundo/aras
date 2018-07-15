package it.unimib.disco.aras.notificationsservice.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * The Class EmailService.
 */
@Service
public class EmailService {

	/** The email sender. */
	@Autowired
	private JavaMailSender emailSender;

	/**
	 * Send simple message.
	 *
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 */
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
	
	/**
	 * Send attachment message.
	 *
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param text
	 *            the text
	 * @param attachment
	 *            the attachment
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public void sendAttachmentMessage(String to, String subject, String text, InputStreamSource attachment) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	     
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(text);
	    helper.addAttachment("Report.pdf", attachment);
	 
	    emailSender.send(message);
	}
}
