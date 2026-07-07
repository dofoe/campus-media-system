package com.campus.media.repository;

import com.campus.media.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    List<Department> findByParentIdIsNullOrderBySortOrderAsc();
    
    List<Department> findByStatusTrueOrderBySortOrderAsc();
}
