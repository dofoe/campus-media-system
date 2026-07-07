package com.campus.media.repository;

import com.campus.media.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    @Query("SELECT ol FROM OperationLog ol LEFT JOIN FETCH ol.user WHERE " +
           "(:keyword IS NULL OR ol.user.username LIKE %:keyword% OR ol.target LIKE %:keyword%) AND " +
           "(:operation IS NULL OR ol.operation = :operation)")
    Page<OperationLog> search(@Param("keyword") String keyword, 
                              @Param("operation") String operation,
                              Pageable pageable);
}
