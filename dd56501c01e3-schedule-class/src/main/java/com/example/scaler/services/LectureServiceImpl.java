package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidBatchException;
import com.example.scaler.exceptions.InvalidInstructorException;
import com.example.scaler.exceptions.InvalidLectureException;
import com.example.scaler.models.Batch;
import com.example.scaler.models.Instructor;
import com.example.scaler.models.Lecture;
import com.example.scaler.models.ScheduledLecture;
import com.example.scaler.repositories.BatchRepository;
import com.example.scaler.repositories.InstructorRepository;
import com.example.scaler.repositories.LectureRepository;
import com.example.scaler.repositories.ScheduledLectureRepository;
import com.example.scaler.utils.DateUtils;
import com.example.scaler.utils.DronaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService{
    private BatchRepository batchRepository;
    private InstructorRepository instructorRepository;
    private LectureRepository lectureRepository;
    private ScheduledLectureRepository scheduledLectureRepository;

    public LectureServiceImpl(BatchRepository batchRepository, InstructorRepository instructorRepository, LectureRepository lectureRepository, ScheduledLectureRepository scheduledLectureRepository){
        this.batchRepository = batchRepository;
        this.lectureRepository = lectureRepository;
        this.instructorRepository = instructorRepository;
        this.scheduledLectureRepository = scheduledLectureRepository;
    }

    @Override
    public List<ScheduledLecture> scheduleLectures(List<Long> lectureIds, Long instructorId, Long batchId) throws InvalidLectureException, InvalidInstructorException, InvalidBatchException {
        Optional<Batch> optionalBatch = batchRepository.findById(batchId);
        if(optionalBatch.isEmpty()){
            throw new InvalidBatchException("Batch id not found");
        }
        Batch batch = optionalBatch.get();

        Optional<Instructor> optionalInstructor = instructorRepository.findById(instructorId);
        if(optionalInstructor.isEmpty()){
            throw new InvalidInstructorException("Instructor not found");
        }
        Instructor instructor = optionalInstructor.get();

        List<Lecture> validLectures = new ArrayList<>();
        for(long lectureId: lectureIds){
            Optional<Lecture> optionalLecture = lectureRepository.findById(lectureId);
            if(optionalLecture.isEmpty()) {
                throw new InvalidLectureException("Lecture Id not found");
            }
            validLectures.add(optionalLecture.get());
        }

        List<ScheduledLecture> scheduledLectureList = scheduledLectureRepository.findByBatch_Id(batchId);
        Date lastScheduledLecture = scheduledLectureList.stream().
                max(Comparator.comparing(ScheduledLecture::getLectureStartTime)).get().getLectureStartTime();

        List<ScheduledLecture> scheduledLectures = new ArrayList<>();

        for(Lecture lecture: validLectures){
            ScheduledLecture scheduledLecture = new ScheduledLecture();
            scheduledLecture.setInstructor(instructor);
            scheduledLecture.setBatch(batch);
            scheduledLecture.setLecture(lecture);
            scheduledLecture.setLectureLink(DronaUtils.generateUniqueLectureLink());
            int day = DateUtils.getDayfromDate(lastScheduledLecture);

            Date nextLectureStartTime;
            if(day == 6 || day == 7) {
               nextLectureStartTime =  DateUtils.find_next_lecture_start_date(lastScheduledLecture, 3);
            }
            else {
              nextLectureStartTime = DateUtils.find_next_lecture_start_date(lastScheduledLecture, 2);
            }
            Date nextLectureEndTime = DateUtils.find_next_lecture_end_date(nextLectureStartTime);

            scheduledLecture.setLectureStartTime(nextLectureStartTime);
            scheduledLecture.setLectureEndTime(nextLectureEndTime);

            lastScheduledLecture = nextLectureStartTime;
            scheduledLecture = scheduledLectureRepository.save(scheduledLecture);
            scheduledLectures.add(scheduledLecture);
        }
        return scheduledLectures;
    }
}
