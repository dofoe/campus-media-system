package com.campus.media.repository;

import com.campus.media.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department LEFT JOIN FETCH u.role WHERE u.id = :id")
    Optional<User> findByIdWithDeptAndRole(@Param("id") Long id);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department LEFT JOIN FETCH u.role")
    List<User> findAllWithDeptAndRole();
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department LEFT JOIN FETCH u.role WHERE " +
           "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.name LIKE %:keyword%) AND " +
           "(:roleCode IS NULL OR u.role.code = :roleCode) AND " +
           "(:deptName IS NULL OR u.department.name = :deptName)")
    Page<User> findByConditions(@Param("keyword") String keyword, 
                                @Param("roleCode") String roleCode,
                                @Param("deptName") String deptName,
                                Pageable pageable);
}
