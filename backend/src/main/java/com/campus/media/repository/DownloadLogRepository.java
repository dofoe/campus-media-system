package com.campus.media.repository;

import com.campus.media.entity.DownloadLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {
    
    @Query("SELECT dl FROM DownloadLog dl LEFT JOIN FETCH dl.media LEFT JOIN FETCH dl.user WHERE " +
           "(:keyword IS NULL OR dl.media.fileName LIKE %:keyword% OR dl.user.username LIKE %:keyword%)")
    Page<DownloadLog> search(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(dl) FROM DownloadLog dl WHERE dl.downloadTime >= :startTime")
    Long countByDownloadTimeAfter(@Param("startTime") LocalDateTime startTime);
}
