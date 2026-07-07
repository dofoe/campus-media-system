package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    private Long deptId;
    
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    
    private String email;
    
    private String phone;
    
    private String dataScope = "self";
}
