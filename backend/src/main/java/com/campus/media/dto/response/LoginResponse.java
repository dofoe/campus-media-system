package com.campus.media.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    
    private String token;
    
    private UserInfo userInfo;
    
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String name;
        private String dept;
        private Long deptId;
        private String role;
        private String roleName;
        private String email;
        private String phone;
        private Boolean status;
        private String dataScope;
        private String createdAt;
        private String createTime;
    }
}
