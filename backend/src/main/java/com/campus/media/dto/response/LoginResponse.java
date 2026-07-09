package com.campus.media.dto.response;

public class LoginResponse {
    
    private String token;
    
    private UserInfo userInfo;
    
    public LoginResponse() {
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
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
        
        public UserInfo() {
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDept() {
            return dept;
        }
        
        public void setDept(String dept) {
            this.dept = dept;
        }
        
        public Long getDeptId() {
            return deptId;
        }
        
        public void setDeptId(Long deptId) {
            this.deptId = deptId;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getRoleName() {
            return roleName;
        }
        
        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        public Boolean getStatus() {
            return status;
        }
        
        public void setStatus(Boolean status) {
            this.status = status;
        }
        
        public String getDataScope() {
            return dataScope;
        }
        
        public void setDataScope(String dataScope) {
            this.dataScope = dataScope;
        }
        
        public String getCreatedAt() {
            return createdAt;
        }
        
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
        
        public String getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}