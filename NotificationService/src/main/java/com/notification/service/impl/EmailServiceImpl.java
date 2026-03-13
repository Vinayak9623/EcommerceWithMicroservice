package com.notification.service.impl;

import com.notification.dto.EmailRequest;
import com.notification.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendOrderConfirmationEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("customerName", emailRequest.getCustomerName());
        context.setVariable("orderId", emailRequest.getOrderId());
        context.setVariable("totalAmount", emailRequest.getTotalAmount());
        context.setVariable("orderStatus", emailRequest.getOrderStatus());

        String htmlContent = templateEngine.process("order-confirmation", context);

        helper.setTo(emailRequest.getToEmail());
        helper.setSubject(emailRequest.getSubject());
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
