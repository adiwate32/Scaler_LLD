package com.example.scaler.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "lectures")
public class Lecture extends BaseModel{

    private String name;
    private String description;
}
