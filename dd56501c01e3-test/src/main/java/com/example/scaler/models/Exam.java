package com.example.scaler.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "exams")
public class Exam extends BaseModel{
    private String name;
    private int totalScore;
}
