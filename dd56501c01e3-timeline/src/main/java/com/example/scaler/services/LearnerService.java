package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidLearnerException;
import com.example.scaler.models.ScheduledLecture;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LearnerService {
    public List<ScheduledLecture> fetchTimeline(long learnerId) throws InvalidLearnerException;
}
