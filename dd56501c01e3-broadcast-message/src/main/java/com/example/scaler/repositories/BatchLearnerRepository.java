package com.example.scaler.repositories;

import com.example.scaler.models.BatchLearner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchLearnerRepository extends JpaRepository<BatchLearner, Long> {
    @Query(value = "SELECT * FROM batch_learners bl WHERE bl.batch_id = :batchId AND bl.exit_date IS NULL", nativeQuery = true)
    List<BatchLearner> getLearnersByCurrentBatch(@Param("batchId") Long batchId);
}
