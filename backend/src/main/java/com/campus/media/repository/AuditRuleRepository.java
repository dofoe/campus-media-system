package com.campus.media.repository;

import com.campus.media.entity.AuditRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRuleRepository extends JpaRepository<AuditRule, Long> {
    
    boolean existsByName(String name);
    
    List<AuditRule> findByStatusTrue();
    
    Page<AuditRule> findByStatus(Boolean status, Pageable pageable);
}
