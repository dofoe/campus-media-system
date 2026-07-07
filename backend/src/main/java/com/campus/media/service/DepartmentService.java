package com.campus.media.service;

import com.campus.media.entity.Department;
import com.campus.media.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    public List<Department> getDepartments() {
        return departmentRepository.findByStatusTrueOrderBySortOrderAsc();
    }
}
