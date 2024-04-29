package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidScheduledLectureException;
import com.example.scaler.models.ScheduledLecture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduledLectureService {

    public List<ScheduledLecture> rescheduleScheduledLecture(long scheduledLectureId) throws InvalidScheduledLectureException;

}
