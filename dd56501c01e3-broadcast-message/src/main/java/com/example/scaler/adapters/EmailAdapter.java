package com.example.scaler.adapters;

import org.springframework.stereotype.Component;

@Component
public interface EmailAdapter {

    void sendEmail(String email, String message) throws Exception;
}
