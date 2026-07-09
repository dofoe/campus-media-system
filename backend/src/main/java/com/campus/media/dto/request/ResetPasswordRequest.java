package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
    
    public ResetPasswordRequest() {
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}