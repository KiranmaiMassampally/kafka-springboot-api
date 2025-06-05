package com.springKafka.email_service.service;

import com.springKafka.base_domains.dto.OrderEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(OrderEvent event) {
        try {
            String toEmail = event.getOrder().getCustomerEmail();
            if (toEmail == null || toEmail.isBlank()) {
                logger.warn("Customer email is missing. Cannot send email.");
                return;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String subject = "Order Confirmation - Order ID: " + event.getOrder().getOrderId();
            String text = String.format(
                    "Hello,\n\nThank you for your order!\n\nDetails:\n" +
                            "Order ID: %s\nProduct: %s\nQuantity: %d\nPrice: $%.2f\n\n" +
                            "We'll notify you once it's shipped.\n\nThanks!",
                    event.getOrder().getOrderId(),
                    event.getOrder().getName(),
                    event.getOrder().getQty(),
                    event.getOrder().getPrice()
            );

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, false);

            mailSender.send(message);
            logger.info("Email sent to customer: {}", toEmail);
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
        }
    }
}
