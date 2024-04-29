package com.example.scaler.adapters;

import com.example.scaler.library.sendgrid.Sendgrid;
import org.springframework.stereotype.Component;

@Component
public class SendGridEmailAdapter implements EmailAdapter{
    private Sendgrid sendgrid = new Sendgrid();
    @Override
    public void sendEmail(String email, String message) throws Exception {
        sendgrid.sendEmailAsync(email, message);
    }
}
