package com.campus.media.repository;

import com.campus.media.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    Optional<Media> findByMd5Hash(String md5Hash);
    
    @Query("SELECT m FROM Media m LEFT JOIN FETCH m.category LEFT JOIN FETCH m.uploadUser WHERE m.id = :id")
    Optional<Media> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT m FROM Media m LEFT JOIN FETCH m.category LEFT JOIN FETCH m.uploadUser WHERE " +
           "(:keyword IS NULL OR m.fileName LIKE %:keyword% OR m.description LIKE %:keyword%) AND " +
           "(:categoryId IS NULL OR m.categoryId = :categoryId) AND " +
           "(:fileType IS NULL OR m.fileType = :fileType) AND " +
           "(:status IS NULL OR m.status = :status)")
    Page<Media> search(@Param("keyword") String keyword,
                       @Param("categoryId") Long categoryId,
                       @Param("fileType") String fileType,
                       @Param("status") String status,
                       Pageable pageable);
    
    @Query("SELECT m FROM Media m WHERE m.auditStatus = :auditStatus")
    Page<Media> findByAuditStatus(@Param("auditStatus") String auditStatus, Pageable pageable);
    
    @Query("SELECT m FROM Media m WHERE m.uploadTime >= :startTime")
    List<Media> findByUploadTimeAfter(@Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT m FROM Media m ORDER BY m.downloadCount DESC")
    Page<Media> findTopByDownloadCount(Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Media m WHERE m.uploadTime >= :startTime")
    Long countByUploadTimeAfter(@Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT SUM(m.fileSize) FROM Media m")
    Long sumFileSize();
    
    @Query("SELECT m.category.name, COUNT(m) FROM Media m GROUP BY m.category.name")
    List<Object[]> countByCategory();
    
    @Query("SELECT m.fileType, COUNT(m) FROM Media m GROUP BY m.fileType")
    List<Object[]> countByFileType();
    
    @Query("SELECT u.department.name, COUNT(m) FROM Media m JOIN m.uploadUser u GROUP BY u.department.name")
    List<Object[]> countByDept();
}
