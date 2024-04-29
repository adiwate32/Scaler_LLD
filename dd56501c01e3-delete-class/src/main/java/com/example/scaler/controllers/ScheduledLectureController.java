package com.example.scaler.controllers;

import com.example.scaler.dtos.RescheduleScheduledLectureRequestDto;
import com.example.scaler.dtos.RescheduleScheduledLectureResponseDto;
import com.example.scaler.dtos.ResponseStatus;
import com.example.scaler.exceptions.InvalidScheduledLectureException;
import com.example.scaler.models.ScheduledLecture;
import com.example.scaler.services.ScheduledLectureService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScheduledLectureController {
    private ScheduledLectureService scheduledLectureService;
    public ScheduledLectureController(ScheduledLectureService scheduledLectureService){
        this.scheduledLectureService = scheduledLectureService;
    }

    public RescheduleScheduledLectureResponseDto rescheduleScheduledLecture(RescheduleScheduledLectureRequestDto requestDto) {
        RescheduleScheduledLectureResponseDto responseDto = new RescheduleScheduledLectureResponseDto();
        try{
            List<ScheduledLecture> scheduledLectureList = scheduledLectureService.rescheduleScheduledLecture(requestDto.getScheduledLectureId());
            responseDto.setScheduledLectures(scheduledLectureList);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
