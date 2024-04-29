package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidBatchException;
import com.example.scaler.exceptions.InvalidUserException;
import com.example.scaler.exceptions.UnAuthorizedAccessException;
import com.example.scaler.models.Communication;
import org.springframework.stereotype.Service;

@Service
public interface CommunicationService {

    public Communication broadcastMessage(long batchId, long userId, String message) throws InvalidBatchException, InvalidUserException, UnAuthorizedAccessException;
}
