package com.example.scaler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "scheduled_lectures")
public class ScheduledLecture extends BaseModel{
    @OneToOne
    private Lecture lecture;
    @ManyToOne
    private Batch batch;
    @ManyToOne
    private Instructor instructor;
    private Date lectureStartTime;
    private Date lectureEndTime;
    private String lectureLink;
}
