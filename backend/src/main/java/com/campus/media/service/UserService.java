package com.campus.media.service;

import com.campus.media.dto.request.UserCreateRequest;
import com.campus.media.dto.request.UserUpdateRequest;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.User;
import com.campus.media.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public PageResponse<User> getUsers(String keyword, String role, String dept, 
                                        Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> page = userRepository.findByConditions(keyword, role, dept, pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
    
    @Transactional
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setDeptId(request.getDeptId());
        user.setRoleId(request.getRoleId());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDataScope(request.getDataScope());
        user.setStatus(true);
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setName(request.getName());
        user.setDeptId(request.getDeptId());
        user.setRoleId(request.getRoleId());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getDataScope() != null) {
            user.setDataScope(request.getDataScope());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(id);
    }
    
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
