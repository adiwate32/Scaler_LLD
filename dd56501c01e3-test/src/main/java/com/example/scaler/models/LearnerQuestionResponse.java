package com.example.scaler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity(name = "leaner_responses")
public class LearnerQuestionResponse extends BaseModel{
    @ManyToOne
    private Learner learner;
    @ManyToOne
    private Question question;
    @OneToOne
    private Option option;
}
