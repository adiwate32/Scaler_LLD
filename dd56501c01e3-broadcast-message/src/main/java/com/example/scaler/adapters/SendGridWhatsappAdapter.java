package com.example.scaler.adapters;

import com.example.scaler.library.sendgrid.Sendgrid;
import org.springframework.stereotype.Component;

@Component
public class SendGridWhatsappAdapter implements WhatsappAdapter{
    private Sendgrid sendgrid = new Sendgrid();
    @Override
    public void sendWhatsappMessage(String phoneNumber, String message) throws Exception {
       sendgrid.sendWhatsApp(phoneNumber, message);
    }
}
