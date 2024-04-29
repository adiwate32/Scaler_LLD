package com.example.scaler.repositories;

import com.example.scaler.models.CommunicationLearner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationLearnerRepository extends JpaRepository<CommunicationLearner, Long> {
}
