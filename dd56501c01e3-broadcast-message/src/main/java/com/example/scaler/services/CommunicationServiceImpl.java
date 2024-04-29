package com.example.scaler.services;

import com.example.scaler.adapters.EmailAdapter;
import com.example.scaler.adapters.WhatsappAdapter;
import com.example.scaler.exceptions.InvalidBatchException;
import com.example.scaler.exceptions.InvalidUserException;
import com.example.scaler.exceptions.UnAuthorizedAccessException;
import com.example.scaler.models.Batch;
import com.example.scaler.models.BatchLearner;
import com.example.scaler.models.Communication;
import com.example.scaler.models.CommunicationLearner;
import com.example.scaler.models.Learner;
import com.example.scaler.models.User;
import com.example.scaler.models.UserType;
import com.example.scaler.repositories.BatchLearnerRepository;
import com.example.scaler.repositories.BatchRepository;
import com.example.scaler.repositories.CommunicationLearnerRepository;
import com.example.scaler.repositories.CommunicationRepository;
import com.example.scaler.repositories.LearnerRepository;
import com.example.scaler.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunicationServiceImpl implements CommunicationService{
    private BatchLearnerRepository batchLearnerRepository;
    private BatchRepository batchRepository;
    private CommunicationLearnerRepository communicationLearnerRepository;
    private CommunicationRepository communicationRepository;
    private UserRepository userRepository;
    private EmailAdapter emailAdapter;
    private WhatsappAdapter whatsappAdapter;

    public CommunicationServiceImpl(BatchRepository batchRepository, BatchLearnerRepository batchLearnerRepository,
                                    CommunicationLearnerRepository communicationLearnerRepository, CommunicationRepository
                                            communicationRepository, UserRepository
                                    userRepository, EmailAdapter emailAdapter, WhatsappAdapter whatsappAdapter){
        this.batchRepository = batchRepository;
        this.batchLearnerRepository = batchLearnerRepository;
        this.communicationRepository = communicationRepository;
        this.communicationLearnerRepository= communicationLearnerRepository;
        this.userRepository = userRepository;
        this.emailAdapter = emailAdapter;
        this.whatsappAdapter = whatsappAdapter;
    }
    @Override
    public Communication broadcastMessage(long batchId, long userId, String message) throws InvalidBatchException, InvalidUserException, UnAuthorizedAccessException {
        Optional<Batch> optionalBatch = batchRepository.findById(batchId);
        if(optionalBatch.isEmpty()){
            throw new InvalidBatchException("Batch Id is not valid");
        }
        Batch batch = optionalBatch.get();

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new InvalidUserException("User Id is not valid");
        }
        User user = optionalUser.get();

        if(!user.getUserType().equals(UserType.ADMIN)){
            throw new UnAuthorizedAccessException("User is not authorized to perform above operation");
        }

        Communication communication = new Communication();
        communication.setMessage(message);
        communication.setBatch(batch);
        communication.setSentBy(user);

        communication = communicationRepository.save(communication);

        List<BatchLearner> batchCurrentLearnerList = batchLearnerRepository.getLearnersByCurrentBatch(batchId);
        for(BatchLearner batchLearner: batchCurrentLearnerList){
            Learner learner = batchLearner.getLearner();
            CommunicationLearner communicationLearner = new CommunicationLearner();
            communicationLearner.setCommunication(communication);
            communicationLearner.setLearner(learner);

            try {
                emailAdapter.sendEmail(learner.getEmail(), message);
                communicationLearner.setEmailDelivered(true);
            }catch (Exception e){
                communicationLearner.setEmailDelivered(false);
            }

            try {
                whatsappAdapter.sendWhatsappMessage(learner.getPhoneNumber(), message);
                communicationLearner.setWhatsappDelivered(true);
            }catch (Exception e){
                communicationLearner.setWhatsappDelivered(false);
            }
            communicationLearnerRepository.save(communicationLearner);
        }
        return communication;
    }
}
