package com.example.scaler.repositories;

import com.example.scaler.models.Batch;
import com.example.scaler.models.ScheduledLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduledLectureRepository extends JpaRepository<ScheduledLecture, Long> {
    @Query(value = "Select * from scheduled_lectures sl where sl.batch_id = :batchId " +
            "and sl.lecture_start_time <= :exitDate " +
            "and sl.lecture_start_time >= :entryDate", nativeQuery = true)
    List<ScheduledLecture> findAllLecturesByPrevBatch(
            @Param("batchId") Long batchId,
            @Param("entryDate") Date entryDate,
            @Param("exitDate") Date exitDate
        );

    @Query(value = "Select * from scheduled_lectures sl where sl.batch_id = :batchId " +
            "and sl.lecture_start_time >= :entryDate", nativeQuery = true)
    List<ScheduledLecture> findAllLecturesByCurrBatch(
            @Param("batchId") Long batchId,
            @Param("entryDate") Date entryDate
    );
}
