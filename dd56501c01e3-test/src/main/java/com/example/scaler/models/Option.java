package com.example.scaler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "options")
public class Option extends BaseModel{
    private String text;
}
