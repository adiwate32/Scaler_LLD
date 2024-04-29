package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidLearnerException;
import com.example.scaler.models.Batch;
import com.example.scaler.models.BatchLearner;
import com.example.scaler.models.Learner;
import com.example.scaler.models.ScheduledLecture;
import com.example.scaler.repositories.BatchLearnerRepository;
import com.example.scaler.repositories.LearnerRepository;
import com.example.scaler.repositories.ScheduledLectureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LearnerServiceImpl implements LearnerService{
    private LearnerRepository learnerRepository;
    private BatchLearnerRepository batchLearnerRepository;
    private ScheduledLectureRepository scheduledLectureRepository;
    public LearnerServiceImpl(LearnerRepository learnerRepository, BatchLearnerRepository batchLearnerRepository,
                       ScheduledLectureRepository scheduledLectureRepository){
        this.learnerRepository = learnerRepository;
        this.batchLearnerRepository = batchLearnerRepository;
        this.scheduledLectureRepository = scheduledLectureRepository;
    }
    @Override
    public List<ScheduledLecture> fetchTimeline(long learnerId) throws InvalidLearnerException {
        Optional<Learner> optionalLearner = learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty()){
            throw new InvalidLearnerException("Learner Id is not valid");
        }

        List<BatchLearner> batchLearnerList = batchLearnerRepository.findAllByLearner_Id(learnerId);

        List<ScheduledLecture> lectureList = new ArrayList<>();
        for(BatchLearner batchLearner: batchLearnerList){
            Batch batch = batchLearner.getBatch();
            Date exitDate = batchLearner.getExitDate();
            Date entryDate = batchLearner.getEntryDate();

            List<ScheduledLecture> scheduledLectureList;
            if(exitDate != null) {
                scheduledLectureList = scheduledLectureRepository.findAllLecturesByPrevBatch(batch.getId(),
                        entryDate, exitDate);
            }else{
                scheduledLectureList = scheduledLectureRepository.findAllLecturesByCurrBatch(batch.getId(),
                        entryDate);
            }

            lectureList.addAll(scheduledLectureList);
        }
        return lectureList;
    }
}
