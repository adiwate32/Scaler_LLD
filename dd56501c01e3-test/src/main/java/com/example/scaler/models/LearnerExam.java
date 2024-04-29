package com.example.scaler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;
@Data
@Entity(name = "learner_exams")
public class LearnerExam extends BaseModel{
    @ManyToOne
    private Learner learner;
    @ManyToOne
    private Exam exam;

    private Date startedAt;

    private Date endedAt;

    @Enumerated(EnumType.ORDINAL)
    private ExamStatus status;

    private int scoreObtained;
}
