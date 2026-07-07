package com.campus.media.repository;

import com.campus.media.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Category> findByParentIdIsNullOrderBySortOrderAsc();
    
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
    
    List<Category> findByStatusTrueOrderBySortOrderAsc();
}
