package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidScheduledLectureException;
import com.example.scaler.models.Batch;
import com.example.scaler.models.ScheduledLecture;
import com.example.scaler.repositories.ScheduledLectureRepository;
import com.example.scaler.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleLectureServiceImpl implements ScheduledLectureService{
    private ScheduledLectureRepository scheduledLectureRepository;

    ScheduleLectureServiceImpl(ScheduledLectureRepository scheduledLectureRepository){
        this.scheduledLectureRepository = scheduledLectureRepository;
    }

    @Override
    public List<ScheduledLecture> rescheduleScheduledLecture(long scheduledLectureId) throws InvalidScheduledLectureException {
        Optional<ScheduledLecture> optionalScheduledLecture = scheduledLectureRepository.findById(scheduledLectureId);
        if(optionalScheduledLecture.isEmpty()){
            throw new InvalidScheduledLectureException("Invalid scheduled lecture");
        }
        ScheduledLecture scheduledLecture = optionalScheduledLecture.get();
        Date currStartDate = scheduledLecture.getLectureStartTime();
        Batch batch = scheduledLecture.getBatch();

        List<ScheduledLecture> filterScheduledLectures = scheduledLectureRepository.findAllByBatch(batch).stream()
                .filter(scheduledLecture1 -> scheduledLecture1.getLectureStartTime().equals(currStartDate) || scheduledLecture1.
                        getLectureStartTime().after(currStartDate)).toList();

        List<ScheduledLecture> scheduledLectures = new ArrayList<>();

        for(ScheduledLecture lecture: filterScheduledLectures){
            int day = DateUtils.getDayfromDate(lecture.getLectureStartTime());
            Date lectureStartTime;
            if(day == 6 || day == 7) {
                lectureStartTime =  DateUtils.find_next_lecture_start_date(currStartDate, 3);
            }
            else {
                lectureStartTime = DateUtils.find_next_lecture_start_date(currStartDate, 2);
            }
            Date lectureEndTime = DateUtils.find_next_lecture_end_date(lectureStartTime);
            lecture.setLectureStartTime(lectureStartTime);
            lecture.setLectureEndTime(lectureEndTime);
            lecture = scheduledLectureRepository.save(lecture);
            scheduledLectures.add(lecture);
        }
        return scheduledLectures;
    }
}
