package com.example.scaler.controllers;

import com.example.scaler.dtos.FetchTimelineRequestDto;
import com.example.scaler.dtos.FetchTimelineResponseDto;
import com.example.scaler.dtos.ResponseStatus;
import com.example.scaler.models.ScheduledLecture;
import com.example.scaler.services.LearnerService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LearnerController {
    private LearnerService learnerService;
    public LearnerController(LearnerService learnerService){
        this.learnerService = learnerService;
    }

    public FetchTimelineResponseDto fetchTimeline(FetchTimelineRequestDto requestDto){
        FetchTimelineResponseDto responseDto = new FetchTimelineResponseDto();
        try{
            List<ScheduledLecture> scheduledLectures = learnerService.fetchTimeline(requestDto.getLearnerId());
            responseDto.setLectures(scheduledLectures);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }catch (Exception e){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
