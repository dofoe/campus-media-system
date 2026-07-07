package com.campus.media.service;

import com.campus.media.dto.request.LoginRequest;
import com.campus.media.dto.response.LoginResponse;
import com.campus.media.entity.User;
import com.campus.media.repository.UserRepository;
import com.campus.media.security.JwtTokenProvider;
import com.campus.media.entity.Role;
import com.campus.media.entity.Department;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (!user.getStatus()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        user.setLastLoginTime(java.time.LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtTokenProvider.generateToken(user);
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(buildUserInfo(user));
        
        return response;
    }
    
    public List<Menu> getMenus(User user) {
        return List.of(
            new Menu("/home", "首页", "House"),
            new Menu("/upload", "上传素材", "UploadFilled"),
            new Menu("/dashboard", "管理后台", "Grid", List.of(
                new Menu("/dashboard", "数据概览"),
                new Menu("/admin/media", "素材管理"),
                new Menu("/admin/rules", "规则配置"),
                new Menu("/admin/users", "用户管理"),
                new Menu("/admin/audit", "审计日志")
            ))
        );
    }
    
    private LoginResponse.UserInfo buildUserInfo(User user) {
        LoginResponse.UserInfo info = new LoginResponse.UserInfo();
        info.setId(user.getId());
        info.setUsername(user.getUsername());
        info.setName(user.getName());
        info.setDept(user.getDepartment() != null ? user.getDepartment().getName() : "");
        info.setDeptId(user.getDeptId());
        info.setRole(user.getRole().getCode());
        info.setRoleName(user.getRole().getName());
        info.setEmail(user.getEmail());
        info.setPhone(user.getPhone());
        info.setStatus(user.getStatus());
        info.setDataScope(user.getDataScope());
        info.setCreatedAt(user.getCreatedAt() != null ? 
            user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
        info.setCreateTime(info.getCreatedAt());
        return info;
    }
    
    public record Menu(String path, String name, String icon, List<Menu> children) {
        public Menu(String path, String name, String icon) {
            this(path, name, icon, null);
        }
        public Menu(String path, String name) {
            this(path, name, null, null);
        }
    }
}
